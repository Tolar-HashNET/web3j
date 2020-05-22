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
package org.web3j.protocol.core.methods.request;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.web3j.utils.Numeric;

/**
 * Transaction request object used the below methods.
 *
 * <ol>
 *   <li>eth_call
 *   <li>eth_sendTransaction
 *   <li>eth_estimateGas
 * </ol>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {
    // default as per https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendtransaction
    public static final BigInteger DEFAULT_GAS = BigInteger.valueOf(9000);

    private String senderAddress;
    private String receiverAddress;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger amount;
    private String data;
    private BigInteger nonce; // nonce field is not present on eth_call/eth_estimateGas

    public Transaction(
            String senderAddress,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String receiverAddress,
            BigInteger amount,
            String data) {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.gas = gasLimit;
        this.gasPrice = gasPrice;
        this.amount = amount;

        if (data != null) {
            this.data = Numeric.prependHexPrefix(data);
        }

        this.nonce = nonce;
    }

    public static Transaction createContractTransaction(
            String from,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            BigInteger value,
            String init) {

        return new Transaction(from, nonce, gasPrice, gasLimit, null, value, init);
    }

    public static Transaction createContractTransaction(
            String from, BigInteger nonce, BigInteger gasPrice, String init) {

        return createContractTransaction(from, nonce, gasPrice, null, null, init);
    }

    public static Transaction createEtherTransaction(
            String from,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            BigInteger value) {

        return new Transaction(from, nonce, gasPrice, gasLimit, to, value, null);
    }

    public static Transaction createFunctionCallTransaction(
            String from,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            BigInteger value,
            String data) {

        return new Transaction(from, nonce, gasPrice, gasLimit, to, value, data);
    }

    public static Transaction createFunctionCallTransaction(
            String from,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data) {

        return new Transaction(from, nonce, gasPrice, gasLimit, to, null, data);
    }

    public static Transaction createEthCallTransaction(String from, String to, String data) {

        return new Transaction(from, null, null, null, to, null, data);
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public String getGas() {
        return convert(gas);
    }

    public String getGasPrice() {
        return convert(gasPrice);
    }

    public String getAmount() {
        return convert(amount);
    }

    public String getData() {
        return data;
    }

    public String getNonce() {
        return convert(nonce);
    }

    private static String convert(BigInteger value) {
        if (value != null) {
            return Numeric.encodeQuantity(value);
        } else {
            return null; // we don't want the field to be encoded if not present
        }
    }
}
