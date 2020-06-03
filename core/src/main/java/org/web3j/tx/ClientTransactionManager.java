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
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.AccountSendRawTransaction;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.TolGetNonce;
import org.web3j.protocol.core.methods.response.TolTryCallTransaction;
import org.web3j.tx.response.TransactionReceiptProcessor;

/**
 * TransactionManager implementation for using an Tolar node to transact.
 *
 * <p><b>Note</b>: accounts must be unlocked on the node for transactions to be successful.
 */
public class ClientTransactionManager extends TransactionManager {

    private final Web3j web3j;

    public ClientTransactionManager(Web3j web3j, String senderAddress) {
        super(web3j, senderAddress);
        this.web3j = web3j;
    }

    public ClientTransactionManager(
            Web3j web3j, String senderAddress, int attempts, int sleepDuration) {
        super(web3j, attempts, sleepDuration, senderAddress);
        this.web3j = web3j;
    }

    public ClientTransactionManager(
            Web3j web3j,
            String senderAddress,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, senderAddress);
        this.web3j = web3j;
    }

    public AccountSendRawTransaction sendTransaction(
            String receiverAddress,
            BigInteger amount,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data)
            throws IOException {
        BigInteger nonce = getNonce();

        Transaction transaction =
                new Transaction(
                        getSenderAddress(),
                        nonce,
                        gasPrice,
                        gas,
                        receiverAddress,
                        amount,
                        data,
                        senderAddressPassword);

        return web3j.accountSendRawTransaction(transaction).send();
    }

    @Override
    public AccountSendRawTransaction sendTransaction(
            String receiverAddress,
            BigInteger amount,
            BigInteger gas,
            BigInteger gasPrice,
            String data)
            throws IOException {

        BigInteger nonce = getNonce();

        Transaction transaction =
                new Transaction(
                        getSenderAddress(),
                        nonce,
                        gasPrice,
                        gas,
                        receiverAddress,
                        amount,
                        data,
                        "");

        return web3j.accountSendRawTransaction(transaction).send();
    }

    @Override
    public String sendCall(String receiverAddress, String data, BigInteger gas, BigInteger gasPrice)
            throws IOException {
        TolTryCallTransaction tolTryCallTransaction =
                web3j.tolTryCallTransaction(
                                Transaction.createTryCallTransaction(
                                        getSenderAddress(), receiverAddress, gas, gasPrice, data))
                        .send();

        assertCallNotReverted(tolTryCallTransaction);
        return tolTryCallTransaction.getOutput();
    }

    @Override
    public EthGetCode getCode(
            final String contractAddress, final DefaultBlockParameter defaultBlockParameter) {
        throw new UnsupportedOperationException();
    }

    protected BigInteger getNonce() throws IOException {
        TolGetNonce tolGetNonce = web3j.tolGetNonce(getSenderAddress()).send();

        return tolGetNonce.getNonce();
    }
}
