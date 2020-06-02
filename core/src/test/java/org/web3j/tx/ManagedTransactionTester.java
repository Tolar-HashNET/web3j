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
package org.web3j.tx;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.core.methods.response.TolGetNonce;
import org.web3j.utils.TxHashVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class ManagedTransactionTester {

    static final String ADDRESS = "0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a";
    static final String TRANSACTION_HASH = "0xHASH";
    protected Web3j web3j;
    protected TxHashVerifier txHashVerifier;

    @BeforeEach
    public void setUp() throws Exception {
        web3j = mock(Web3j.class);
        txHashVerifier = mock(TxHashVerifier.class);
        when(txHashVerifier.verify(any(), any())).thenReturn(true);
    }

    public TransactionManager getVerifiedTransactionManager(
            Credentials credentials, int attempts, int sleepDuration) {
        RawTransactionManager transactionManager =
                new RawTransactionManager(web3j, credentials, attempts, sleepDuration);
        transactionManager.setTxHashVerifier(txHashVerifier);
        return transactionManager;
    }

    public TransactionManager getVerifiedTransactionManager(Credentials credentials) {
        RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        transactionManager.setTxHashVerifier(txHashVerifier);
        return transactionManager;
    }

    void prepareTransaction(TransactionReceipt transactionReceipt) throws IOException {
        prepareNonceRequest();
        prepareTransactionRequest();
        prepareTransactionReceipt(transactionReceipt);
    }

    @SuppressWarnings("unchecked")
    void prepareNonceRequest() throws IOException {
        TolGetNonce tolGetNonce = new TolGetNonce();
        // tolGetNonce.setResult("0x1");

        Request<?, TolGetNonce> transactionCountRequest = mock(Request.class);
        when(transactionCountRequest.send()).thenReturn(tolGetNonce);
        when(web3j.tolGetNonce(SampleKeys.ADDRESS)).thenReturn((Request) transactionCountRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionRequest() throws IOException {
        AccountSendRawTransaction accountSendRawTransaction = new AccountSendRawTransaction();
        accountSendRawTransaction.setResult(TRANSACTION_HASH);

        Request<?, AccountSendRawTransaction> rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.send()).thenReturn(accountSendRawTransaction);
        when(web3j.ethSendRawTransaction(any(String.class)))
                .thenReturn((Request) rawTransactionRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionReceipt(TransactionReceipt transactionReceipt) throws IOException {
        TolGetTransactionReceipt tolGetTransactionReceipt = new TolGetTransactionReceipt();
        tolGetTransactionReceipt.setResult(transactionReceipt);

        Request<?, TolGetTransactionReceipt> getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.send()).thenReturn(tolGetTransactionReceipt);
        when(web3j.tolGetTransactionReceipt(TRANSACTION_HASH))
                .thenReturn((Request) getTransactionReceiptRequest);
    }

    @SuppressWarnings("unchecked")
    protected TransactionReceipt prepareTransfer() throws IOException {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setHash(TRANSACTION_HASH);
        prepareTransaction(transactionReceipt);

        EthGasPrice ethGasPrice = new EthGasPrice();
        ethGasPrice.setResult("0x1");

        Request<?, EthGasPrice> gasPriceRequest = mock(Request.class);
        when(gasPriceRequest.send()).thenReturn(ethGasPrice);
        when(web3j.ethGasPrice()).thenReturn((Request) gasPriceRequest);

        return transactionReceipt;
    }
}
