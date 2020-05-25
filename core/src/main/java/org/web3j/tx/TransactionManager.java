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
import org.web3j.protocol.core.methods.response.AccountSendRawTransaction;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.TolTryCallTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import static org.web3j.protocol.core.JsonRpc2_0Web3j.DEFAULT_BLOCK_TIME;

/**
 * Transaction manager abstraction for executing transactions with Tolar client via various
 * mechanisms.
 */
public abstract class TransactionManager {

    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    public static final long DEFAULT_POLLING_FREQUENCY = DEFAULT_BLOCK_TIME;
    public static final String REVERT_ERR_STR =
            "Contract Call has been reverted by the EVM with the reason: '%s'.";

    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final String fromAddress;

    protected TransactionManager(
            TransactionReceiptProcessor transactionReceiptProcessor, String fromAddress) {
        this.transactionReceiptProcessor = transactionReceiptProcessor;
        this.fromAddress = fromAddress;
    }

    protected TransactionManager(Web3j web3j, String fromAddress) {
        this(
                new PollingTransactionReceiptProcessor(
                        web3j, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH),
                fromAddress);
    }

    protected TransactionManager(
            Web3j web3j, int attempts, long sleepDuration, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(web3j, sleepDuration, attempts), fromAddress);
    }

    protected TransactionReceipt executeTransaction(
            String receiverAddress,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce)
            throws IOException, TransactionException {

        return executeTransaction(
                receiverAddress,
                BigInteger.ZERO,
                senderAddressPassword,
                gas,
                gasPrice,
                data,
                nonce);
    }

    protected TransactionReceipt executeTransaction(
            String receiverAddress,
            BigInteger amount,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce)
            throws IOException, TransactionException {

        AccountSendRawTransaction accountSendRawTransaction =
                sendTransaction(
                        receiverAddress, amount, senderAddressPassword, gas, gasPrice, data, nonce);
        return processResponse(accountSendRawTransaction);
    }

    public AccountSendRawTransaction sendTransaction(
            String receiverAddress,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce)
            throws IOException {
        return sendTransaction(
                receiverAddress,
                BigInteger.ZERO,
                senderAddressPassword,
                gas,
                gasPrice,
                data,
                nonce);
    }

    public abstract AccountSendRawTransaction sendTransaction(
            String receiverAddress,
            BigInteger amount,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce)
            throws IOException;

    public abstract String sendCall(
            String receiverAddress, String data, BigInteger gas, BigInteger gasPrice)
            throws IOException;

    public abstract EthGetCode getCode(
            String contractAddress, DefaultBlockParameter defaultBlockParameter) throws IOException;

    public String getFromAddress() {
        return fromAddress;
    }

    private TransactionReceipt processResponse(AccountSendRawTransaction transactionResponse)
            throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException(
                    "Error processing transaction request: "
                            + transactionResponse.getError().getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();

        return transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
    }

    static void assertCallNotReverted(TolTryCallTransaction tolTryCallTransaction) {
        if (tolTryCallTransaction.isReverted()) {
            throw new ContractCallException("Transaction is excepted.");
        }

        if (tolTryCallTransaction.hasError()) {
            throw new ContractCallException(tolTryCallTransaction.getError().getMessage());
        }
    }
}
