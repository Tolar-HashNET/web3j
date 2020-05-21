/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.rx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.filters.BlockFilter;
import org.web3j.protocol.core.filters.LogFilter;
import org.web3j.protocol.core.filters.PendingTransactionFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TolBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Flowables;

/** web3j reactive API implementation. */
public class JsonRpc2_0Rx {

    private final Web3j web3j;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Scheduler scheduler;

    public JsonRpc2_0Rx(Web3j web3j, ScheduledExecutorService scheduledExecutorService) {
        this.web3j = web3j;
        this.scheduledExecutorService = scheduledExecutorService;
        this.scheduler = Schedulers.from(scheduledExecutorService);
    }

    public Flowable<String> ethBlockHashFlowable(long pollingInterval) {
        return Flowable.create(
                subscriber -> {
                    BlockFilter blockFilter = new BlockFilter(web3j, subscriber::onNext);
                    run(blockFilter, subscriber, pollingInterval);
                },
                BackpressureStrategy.BUFFER);
    }

    public Flowable<String> ethPendingTransactionHashFlowable(long pollingInterval) {
        return Flowable.create(
                subscriber -> {
                    PendingTransactionFilter pendingTransactionFilter =
                            new PendingTransactionFilter(web3j, subscriber::onNext);

                    run(pendingTransactionFilter, subscriber, pollingInterval);
                },
                BackpressureStrategy.BUFFER);
    }

    public Flowable<Log> ethLogFlowable(
            org.web3j.protocol.core.methods.request.EthFilter ethFilter, long pollingInterval) {
        return Flowable.create(
                subscriber -> {
                    LogFilter logFilter = new LogFilter(web3j, subscriber::onNext, ethFilter);

                    run(logFilter, subscriber, pollingInterval);
                },
                BackpressureStrategy.BUFFER);
    }

    private <T> void run(
            org.web3j.protocol.core.filters.Filter<T> filter,
            FlowableEmitter<? super T> emitter,
            long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        emitter.setCancellable(filter::cancel);
    }

    public Flowable<Transaction> transactionFlowable(long pollingInterval) {
        return blockFlowable(true, pollingInterval).flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Flowable<Transaction> pendingTransactionFlowable(long pollingInterval) {
        return ethPendingTransactionHashFlowable(pollingInterval)
                .flatMap(
                        transactionHash ->
                                web3j.ethGetTransactionByHash(transactionHash).flowable())
                .filter(ethTransaction -> ethTransaction.getTransaction().isPresent())
                .map(ethTransaction -> ethTransaction.getTransaction().get());
    }

    public Flowable<TolBlock> blockFlowable(boolean fullTransactionObjects, long pollingInterval) {
        return ethBlockHashFlowable(pollingInterval)
                .flatMap(
                        blockHash ->
                                web3j.ethGetBlockByHash(blockHash, fullTransactionObjects)
                                        .flowable());
    }

    public Flowable<TolBlock> replayBlocksFlowable(
            DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksFlowable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Flowable<TolBlock> replayBlocksFlowable(
            DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock,
            boolean fullTransactionObjects,
            boolean ascending) {
        // We use a scheduler to ensure this Flowable runs asynchronously for users to be
        // consistent with the other Flowables
        return replayBlocksFlowableSync(startBlock, endBlock, fullTransactionObjects, ascending)
                .subscribeOn(scheduler);
    }

    private Flowable<TolBlock> replayBlocksFlowableSync(
            DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksFlowableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Flowable<TolBlock> replayBlocksFlowableSync(
            DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock,
            boolean fullTransactionObjects,
            boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockIndex(startBlock);
            endBlockNumber = getBlockIndex(endBlock);
        } catch (IOException e) {
            Flowable.error(e);
        }

        if (ascending) {
            return Flowables.range(startBlockNumber, endBlockNumber)
                    .flatMap(
                            i ->
                                    web3j.ethGetBlockByNumber(
                                                    new DefaultBlockParameterNumber(i),
                                                    fullTransactionObjects)
                                            .flowable());
        } else {
            return Flowables.range(startBlockNumber, endBlockNumber, false)
                    .flatMap(
                            i ->
                                    web3j.ethGetBlockByNumber(
                                                    new DefaultBlockParameterNumber(i),
                                                    fullTransactionObjects)
                                            .flowable());
        }
    }

    public Flowable<Transaction> replayTransactionsFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return replayBlocksFlowable(startBlock, endBlock, true)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Flowable<TolBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock,
            boolean fullTransactionObjects,
            Flowable<TolBlock> onCompleteFlowable) {
        // We use a scheduler to ensure this Flowable runs asynchronously for users to be
        // consistent with the other Flowables
        return replayPastBlocksFlowableSync(startBlock, fullTransactionObjects, onCompleteFlowable)
                .subscribeOn(scheduler);
    }

    public Flowable<TolBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return replayPastBlocksFlowable(startBlock, fullTransactionObjects, Flowable.empty());
    }

    private Flowable<TolBlock> replayPastBlocksFlowableSync(
            DefaultBlockParameter startBlock,
            boolean fullTransactionObjects,
            Flowable<TolBlock> onCompleteFlowable) {

        BigInteger startBlockNumber;
        BigInteger latestBlockNumber;
        try {
            startBlockNumber = getBlockIndex(startBlock);
            latestBlockNumber = getLatestBlockIndex();
        } catch (IOException e) {
            return Flowable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteFlowable;
        } else {
            return Flowable.concat(
                    replayBlocksFlowableSync(
                            new DefaultBlockParameterNumber(startBlockNumber),
                            new DefaultBlockParameterNumber(latestBlockNumber),
                            fullTransactionObjects),
                    Flowable.defer(
                            () ->
                                    replayPastBlocksFlowableSync(
                                            new DefaultBlockParameterNumber(
                                                    latestBlockNumber.add(BigInteger.ONE)),
                                            fullTransactionObjects,
                                            onCompleteFlowable)));
        }
    }

    public Flowable<Transaction> replayPastTransactionsFlowable(DefaultBlockParameter startBlock) {
        return replayPastBlocksFlowable(startBlock, true, Flowable.empty())
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Flowable<TolBlock> replayPastAndFutureBlocksFlowable(
            DefaultBlockParameter startBlock,
            boolean fullTransactionObjects,
            long pollingInterval) {

        return replayPastBlocksFlowable(
                startBlock,
                fullTransactionObjects,
                blockFlowable(fullTransactionObjects, pollingInterval));
    }

    public Flowable<Transaction> replayPastAndFutureTransactionsFlowable(
            DefaultBlockParameter startBlock, long pollingInterval) {
        return replayPastAndFutureBlocksFlowable(startBlock, true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    private BigInteger getLatestBlockIndex() throws IOException {
        return getBlockIndex(DefaultBlockParameterName.LATEST);
    }

    private BigInteger getBlockIndex(DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        if (defaultBlockParameter instanceof DefaultBlockParameterNumber) {
            return ((DefaultBlockParameterNumber) defaultBlockParameter).getBlockNumber();
        } else {
            TolBlock latestTolBlock =
                    web3j.ethGetBlockByNumber(defaultBlockParameter, false).send();
            return latestTolBlock.getBlock().getBlockIndex();
        }
    }

    private static List<Transaction> toTransactions(TolBlock tolBlock) {
        // If you ever see an exception thrown here, it's probably due to an incomplete chain in
        // Geth/Parity. You should resync to solve.

        // TODO: See what to do here, not possible to get Transaction objects from Block in Tolar
        // API
        return Collections.emptyList();
    }
}
