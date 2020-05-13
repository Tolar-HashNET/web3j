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
package org.web3j.protocol.core;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

import io.reactivex.Flowable;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.methods.request.ShhFilter;
import org.web3j.protocol.core.methods.request.ShhPost;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.core.methods.response.admin.AdminNodeInfo;
import org.web3j.protocol.core.methods.response.admin.AdminPeers;
import org.web3j.protocol.rx.JsonRpc2_0Rx;
import org.web3j.protocol.websocket.events.LogNotification;
import org.web3j.protocol.websocket.events.NewHeadsNotification;
import org.web3j.utils.Async;

/** JSON-RPC 2.0 factory implementation. */
public class JsonRpc2_0Web3j implements Web3j {

    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

    protected final Web3jService web3jService;
    private final JsonRpc2_0Rx web3jRx;
    private final long blockTime;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0Web3j(Web3jService web3jService) {
        this(web3jService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0Web3j(
            Web3jService web3jService,
            long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        this.web3jService = web3jService;
        this.web3jRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public Request<?, Web3ClientVersion> web3ClientVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, Web3Sha3> web3Sha3(String data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, NetVersion> netVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, NetListening> netListening() {
        throw new UnsupportedOperationException();
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, NetPeerCount> netPeerCount() {
        return new Request<>(
                "net_peerCount", Collections.<String>emptyList(), web3jService, NetPeerCount.class);
    }

    @Override
    public Request<?, AdminNodeInfo> adminNodeInfo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, AdminPeers> adminPeers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthProtocolVersion> ethProtocolVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthChainId> ethChainId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthCoinbase> ethCoinbase() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthSyncing> ethSyncing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthMining> ethMining() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthHashrate> ethHashrate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthGasPrice> ethGasPrice() {
        throw new UnsupportedOperationException();
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, TolAddresses> accountListAddresses() {
        return new Request<>(
                "account_listAddresses",
                Collections.<String>emptyList(),
                web3jService,
                TolAddresses.class);
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, EthBlockNumber> ethBlockNumber() {
        return new Request<>(
                "tol_getBlockCount",
                Collections.<String>emptyList(),
                web3jService,
                EthBlockNumber.class);
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, EthGetBalance> ethGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "tol_getBalance",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3jService,
                EthGetBalance.class);
    }

    @Override
    public Request<?, EthGetStorageAt> ethGetStorageAt(
            String address, BigInteger position, DefaultBlockParameter defaultBlockParameter) {
        throw new UnsupportedOperationException();
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, EthGetTransactionCount> ethGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "tol_getNonce",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3jService,
                EthGetTransactionCount.class);
    }

    @Override
    public Request<?, EthGetBlockTransactionCountByHash> ethGetBlockTransactionCountByHash(
            String blockHash) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthGetBlockTransactionCountByNumber> ethGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthGetUncleCountByBlockHash> ethGetUncleCountByBlockHash(String blockHash) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthGetUncleCountByBlockNumber> ethGetUncleCountByBlockNumber(
            DefaultBlockParameter defaultBlockParameter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthGetCode> ethGetCode(
            String address, DefaultBlockParameter defaultBlockParameter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthSign> ethSign(String address, String sha3HashOfDataToSign) {
        throw new UnsupportedOperationException();
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction>
            ethSendTransaction(Transaction transaction) {
        return new Request<>(
                "tx_sendSignedTransaction",
                Arrays.asList(transaction),
                web3jService,
                org.web3j.protocol.core.methods.response.EthSendTransaction.class);
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction>
            ethSendRawTransaction(String signedTransactionData) {
        return new Request<>(
                "account_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                web3jService,
                org.web3j.protocol.core.methods.response.EthSendTransaction.class);
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, org.web3j.protocol.core.methods.response.EthCall> ethCall(
            Transaction transaction, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "tol_tryCallTransaction",
                Arrays.asList(transaction, defaultBlockParameter),
                web3jService,
                org.web3j.protocol.core.methods.response.EthCall.class);
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, EthEstimateGas> ethEstimateGas(Transaction transaction) {
        return new Request<>(
                "tol_getGasEstimate",
                Arrays.asList(transaction),
                web3jService,
                EthEstimateGas.class);
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, EthBlock> ethGetBlockByHash(
            String blockHash, boolean returnFullTransactionObjects) {
        return new Request<>(
                "tol_getBlockByHash",
                Arrays.asList(blockHash, returnFullTransactionObjects),
                web3jService,
                EthBlock.class);
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, EthBlock> ethGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter, boolean returnFullTransactionObjects) {
        return new Request<>(
                "tol_getBlockByIndex",
                Arrays.asList(defaultBlockParameter.getValue(), returnFullTransactionObjects),
                web3jService,
                EthBlock.class);
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, EthTransaction> ethGetTransactionByHash(String transactionHash) {
        return new Request<>(
                "tol_getTransaction",
                Arrays.asList(transactionHash),
                web3jService,
                EthTransaction.class);
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthTransaction> ethGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        throw new UnsupportedOperationException();
    }

    // TODO: This method is in Tolar API
    @Override
    public Request<?, EthGetTransactionReceipt> ethGetTransactionReceipt(String transactionHash) {
        return new Request<>(
                "tol_getTransactionReceipt",
                Arrays.asList(transactionHash),
                web3jService,
                EthGetTransactionReceipt.class);
    }

    @Override
    public Request<?, EthBlock> ethGetUncleByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthBlock> ethGetUncleByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger uncleIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthGetCompilers> ethGetCompilers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthCompileLLL> ethCompileLLL(String sourceCode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthCompileSolidity> ethCompileSolidity(String sourceCode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthCompileSerpent> ethCompileSerpent(String sourceCode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthFilter> ethNewFilter(
            org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthFilter> ethNewBlockFilter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthFilter> ethNewPendingTransactionFilter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthUninstallFilter> ethUninstallFilter(BigInteger filterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthLog> ethGetFilterChanges(BigInteger filterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthLog> ethGetFilterLogs(BigInteger filterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthLog> ethGetLogs(
            org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthGetWork> ethGetWork() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthSubmitWork> ethSubmitWork(
            String nonce, String headerPowHash, String mixDigest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, EthSubmitHashrate> ethSubmitHashrate(String hashrate, String clientId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, DbPutString> dbPutString(
            String databaseName, String keyName, String stringToStore) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, DbGetString> dbGetString(String databaseName, String keyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, DbGetHex> dbGetHex(String databaseName, String keyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, org.web3j.protocol.core.methods.response.ShhPost> shhPost(ShhPost shhPost) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, ShhVersion> shhVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, ShhNewIdentity> shhNewIdentity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(BigInteger filterId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Flowable<NewHeadsNotification> newHeadsNotifications() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Flowable<LogNotification> logsNotifications(
            List<String> addresses, List<String> topics) {
        throw new UnsupportedOperationException();
    }

    private Map<String, Object> createLogsParams(List<String> addresses, List<String> topics) {
        Map<String, Object> params = new HashMap<>();
        if (!addresses.isEmpty()) {
            params.put("address", addresses);
        }
        if (!topics.isEmpty()) {
            params.put("topics", topics);
        }
        return params;
    }

    @Override
    public Flowable<String> ethBlockHashFlowable() {
        return web3jRx.ethBlockHashFlowable(blockTime);
    }

    @Override
    public Flowable<String> ethPendingTransactionHashFlowable() {
        return web3jRx.ethPendingTransactionHashFlowable(blockTime);
    }

    @Override
    public Flowable<Log> ethLogFlowable(
            org.web3j.protocol.core.methods.request.EthFilter ethFilter) {
        return web3jRx.ethLogFlowable(ethFilter, blockTime);
    }

    @Override
    public Flowable<org.web3j.protocol.core.methods.response.Transaction> transactionFlowable() {
        return web3jRx.transactionFlowable(blockTime);
    }

    @Override
    public Flowable<org.web3j.protocol.core.methods.response.Transaction>
            pendingTransactionFlowable() {
        return web3jRx.pendingTransactionFlowable(blockTime);
    }

    @Override
    public Flowable<EthBlock> blockFlowable(boolean fullTransactionObjects) {
        return web3jRx.blockFlowable(fullTransactionObjects, blockTime);
    }

    @Override
    public Flowable<EthBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return web3jRx.replayBlocksFlowable(startBlock, endBlock, fullTransactionObjects);
    }

    @Override
    public Flowable<EthBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock,
            boolean fullTransactionObjects,
            boolean ascending) {
        return web3jRx.replayBlocksFlowable(
                startBlock, endBlock, fullTransactionObjects, ascending);
    }

    @Override
    public Flowable<EthBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock,
            boolean fullTransactionObjects,
            Flowable<EthBlock> onCompleteFlowable) {
        return web3jRx.replayPastBlocksFlowable(
                startBlock, fullTransactionObjects, onCompleteFlowable);
    }

    @Override
    public Flowable<EthBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return web3jRx.replayPastBlocksFlowable(startBlock, fullTransactionObjects);
    }

    @Override
    public Flowable<org.web3j.protocol.core.methods.response.Transaction>
            replayPastTransactionsFlowable(
                    DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return web3jRx.replayTransactionsFlowable(startBlock, endBlock);
    }

    @Override
    public Flowable<org.web3j.protocol.core.methods.response.Transaction>
            replayPastTransactionsFlowable(DefaultBlockParameter startBlock) {
        return web3jRx.replayPastTransactionsFlowable(startBlock);
    }

    @Override
    public Flowable<EthBlock> replayPastAndFutureBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return web3jRx.replayPastAndFutureBlocksFlowable(
                startBlock, fullTransactionObjects, blockTime);
    }

    @Override
    public Flowable<org.web3j.protocol.core.methods.response.Transaction>
            replayPastAndFutureTransactionsFlowable(DefaultBlockParameter startBlock) {
        return web3jRx.replayPastAndFutureTransactionsFlowable(startBlock, blockTime);
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        try {
            web3jService.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close web3j service", e);
        }
    }

    @Override
    public BatchRequest newBatch() {
        return new BatchRequest(web3jService);
    }
}
