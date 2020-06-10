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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.core.methods.response.TolBlock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled("Not needed for HashNet")
public class JsonRpc2_0RxTest {

    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    private Web3j web3j;

    private Web3jService web3jService;

    @BeforeEach
    public void setUp() {
        web3jService = mock(Web3jService.class);
        web3j = Web3j.build(web3jService, 1000, Executors.newSingleThreadScheduledExecutor());
    }

    @Test
    public void testReplayBlocksFlowable() throws Exception {

        List<TolBlock> tolBlocks = Arrays.asList(createBlock(0), createBlock(1), createBlock(2));

        OngoingStubbing<TolBlock> stubbing =
                when(web3jService.send(any(Request.class), eq(TolBlock.class)));
        for (TolBlock tolBlock : tolBlocks) {
            stubbing = stubbing.thenReturn(tolBlock);
        }

        Flowable<TolBlock> flowable =
                web3j.replayPastBlocksFlowable(
                        new DefaultBlockParameterNumber(BigInteger.ZERO),
                        new DefaultBlockParameterNumber(BigInteger.valueOf(2)),
                        false);

        CountDownLatch transactionLatch = new CountDownLatch(tolBlocks.size());
        CountDownLatch completedLatch = new CountDownLatch(1);

        List<TolBlock> results = new ArrayList<>(tolBlocks.size());
        Disposable subscription =
                flowable.subscribe(
                        result -> {
                            results.add(result);
                            transactionLatch.countDown();
                        },
                        throwable -> fail(throwable.getMessage()),
                        () -> completedLatch.countDown());

        transactionLatch.await(1, TimeUnit.SECONDS);
        assertEquals(results, (tolBlocks));

        subscription.dispose();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isDisposed());
    }

    @Test
    public void testReplayBlocksDescendingFlowable() throws Exception {

        List<TolBlock> tolBlocks = Arrays.asList(createBlock(2), createBlock(1), createBlock(0));

        OngoingStubbing<TolBlock> stubbing =
                when(web3jService.send(any(Request.class), eq(TolBlock.class)));
        for (TolBlock tolBlock : tolBlocks) {
            stubbing = stubbing.thenReturn(tolBlock);
        }

        Flowable<TolBlock> flowable =
                web3j.replayPastBlocksFlowable(
                        new DefaultBlockParameterNumber(BigInteger.ZERO),
                        new DefaultBlockParameterNumber(BigInteger.valueOf(2)),
                        false,
                        false);

        CountDownLatch transactionLatch = new CountDownLatch(tolBlocks.size());
        CountDownLatch completedLatch = new CountDownLatch(1);

        List<TolBlock> results = new ArrayList<>(tolBlocks.size());
        Disposable subscription =
                flowable.subscribe(
                        result -> {
                            results.add(result);
                            transactionLatch.countDown();
                        },
                        throwable -> fail(throwable.getMessage()),
                        () -> completedLatch.countDown());

        transactionLatch.await(1, TimeUnit.SECONDS);
        assertEquals(results, (tolBlocks));

        subscription.dispose();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isDisposed());
    }

    @Test
    public void testReplayPastBlocksFlowable() throws Exception {
        List<TolBlock> expected =
                Arrays.asList(
                        createBlock(0),
                        createBlock(1),
                        createBlock(2),
                        createBlock(3),
                        createBlock(4));

        List<TolBlock> tolBlocks =
                Arrays.asList(
                        expected.get(2), // greatest block
                        expected.get(0),
                        expected.get(1),
                        expected.get(2),
                        expected.get(4), // greatest block
                        expected.get(3),
                        expected.get(4),
                        expected.get(4)); // greatest block

        OngoingStubbing<TolBlock> stubbing =
                when(web3jService.send(any(Request.class), eq(TolBlock.class)));
        for (TolBlock tolBlock : tolBlocks) {
            stubbing = stubbing.thenReturn(tolBlock);
        }

        EthFilter ethFilter =
                objectMapper.readValue(
                        "{\n"
                                + "  \"id\":1,\n"
                                + "  \"jsonrpc\": \"2.0\",\n"
                                + "  \"result\": \"0x1\"\n"
                                + "}",
                        EthFilter.class);
        EthLog ethLog =
                objectMapper.readValue(
                        "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":["
                                + "\"0x31c2342b1e0b8ffda1507fbffddf213c4b3c1e819ff6a84b943faabb0ebf2403\""
                                + "]}",
                        EthLog.class);
        EthUninstallFilter ethUninstallFilter =
                objectMapper.readValue(
                        "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":true}", EthUninstallFilter.class);

        when(web3jService.send(any(Request.class), eq(EthFilter.class))).thenReturn(ethFilter);
        when(web3jService.send(any(Request.class), eq(EthLog.class))).thenReturn(ethLog);
        when(web3jService.send(any(Request.class), eq(EthUninstallFilter.class)))
                .thenReturn(ethUninstallFilter);

        Flowable<TolBlock> flowable =
                web3j.replayPastBlocksFlowable(
                        new DefaultBlockParameterNumber(BigInteger.ZERO), false);

        CountDownLatch transactionLatch = new CountDownLatch(expected.size());
        CountDownLatch completedLatch = new CountDownLatch(1);

        List<TolBlock> results = new ArrayList<>(expected.size());
        Disposable subscription =
                flowable.subscribe(
                        result -> {
                            results.add(result);
                            transactionLatch.countDown();
                        },
                        throwable -> fail(throwable.getMessage()),
                        () -> completedLatch.countDown());

        transactionLatch.await(1250, TimeUnit.MILLISECONDS);
        assertEquals(results, (expected));

        subscription.dispose();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isDisposed());
    }

    public void testReplayPastAndFutureBlocksFlowable() throws Exception {
        List<TolBlock> expected =
                Arrays.asList(
                        createBlock(0),
                        createBlock(1),
                        createBlock(2),
                        createBlock(3),
                        createBlock(4),
                        createBlock(5),
                        createBlock(6));

        List<TolBlock> tolBlocks =
                Arrays.asList(
                        expected.get(2), // greatest block
                        expected.get(0),
                        expected.get(1),
                        expected.get(2),
                        expected.get(4), // greatest block
                        expected.get(3),
                        expected.get(4),
                        expected.get(4), // greatest block
                        expected.get(5), // initial response from ethGetFilterLogs call
                        expected.get(6)); // subsequent block from new block flowable

        OngoingStubbing<TolBlock> stubbing =
                when(web3jService.send(any(Request.class), eq(TolBlock.class)));
        for (TolBlock tolBlock : tolBlocks) {
            stubbing = stubbing.thenReturn(tolBlock);
        }

        EthFilter ethFilter =
                objectMapper.readValue(
                        "{\n"
                                + "  \"id\":1,\n"
                                + "  \"jsonrpc\": \"2.0\",\n"
                                + "  \"result\": \"0x1\"\n"
                                + "}",
                        EthFilter.class);
        EthLog ethLog =
                objectMapper.readValue(
                        "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":["
                                + "\"0x31c2342b1e0b8ffda1507fbffddf213c4b3c1e819ff6a84b943faabb0ebf2403\""
                                + "]}",
                        EthLog.class);
        EthUninstallFilter ethUninstallFilter =
                objectMapper.readValue(
                        "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":true}", EthUninstallFilter.class);

        when(web3jService.send(any(Request.class), eq(EthFilter.class))).thenReturn(ethFilter);
        when(web3jService.send(any(Request.class), eq(EthLog.class))).thenReturn(ethLog);
        when(web3jService.send(any(Request.class), eq(EthUninstallFilter.class)))
                .thenReturn(ethUninstallFilter);

        Flowable<TolBlock> flowable =
                web3j.replayPastAndFutureBlocksFlowable(
                        new DefaultBlockParameterNumber(BigInteger.ZERO), false);

        CountDownLatch transactionLatch = new CountDownLatch(expected.size());
        CountDownLatch completedLatch = new CountDownLatch(1);

        List<TolBlock> results = new ArrayList<>(expected.size());
        Disposable subscription =
                flowable.subscribe(
                        result -> {
                            results.add(result);
                            transactionLatch.countDown();
                        },
                        throwable -> fail(throwable.getMessage()),
                        () -> completedLatch.countDown());

        transactionLatch.await(1250, TimeUnit.MILLISECONDS);
        assertEquals(results, (expected));

        subscription.dispose();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isDisposed());
    }

    @Test
    public void testReplayTransactionsFlowable() throws Exception {}

    private TolBlock createBlock(int number) {
        return null;
    }

    private TolBlock createBlockWithTransactions(int blockNumber, List<Transaction> transactions) {
        return null;
    }
}
