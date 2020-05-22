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
import com.fasterxml.jackson.annotation.JsonProperty;

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
    public static final BigInteger DEFAULT_GAS = BigInteger.valueOf(9000);

    @JsonProperty("sender_address")
    private String senderAddress;

    @JsonProperty("receiver_address")
    private String receiverAddress;

    private BigInteger gas;

    @JsonProperty("gas_price")
    private BigInteger gasPrice;

    private BigInteger amount;
    private String data;
    private BigInteger nonce;

    private String senderAddressPassword;

    public Transaction(
            String senderAddress,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gas,
            String receiverAddress,
            BigInteger amount,
            String data) {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.amount = amount;

        if (data != null) {
            this.data = Numeric.prependHexPrefix(data);
        }

        this.nonce = nonce;
    }

    public Transaction(
            String senderAddress,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gas,
            String receiverAddress,
            BigInteger amount,
            String data,
            String senderAddressPassword) {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.amount = amount;
        this.data = data;
        this.nonce = nonce;
        this.senderAddressPassword = senderAddressPassword;
    }

    public static Transaction createDeployContractTransaction(
            String senderAddress,
            BigInteger amount,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce) {

        return new Transaction(
                senderAddress, nonce, gasPrice, gas, null, amount, data, senderAddressPassword);
    }

    public static Transaction createDeployContractTransaction(
            String senderAddress,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce) {

        return createDeployContractTransaction(
                senderAddress, BigInteger.ZERO, senderAddressPassword, gas, gasPrice, data, nonce);
    }

    public static Transaction createDeployContractTransaction(
            String senderAddress,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce) {

        return createDeployContractTransaction(senderAddress, "", gas, gasPrice, data, nonce);
    }

    public static Transaction createFundTransferTransaction(
            String from,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            BigInteger value) {

        return new Transaction(from, nonce, gasPrice, gasLimit, to, value, null);
    }

    public static Transaction createExecuteFunctionTransaction(
            String senderAddress,
            String receiverAddress,
            BigInteger amount,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce) {

        return new Transaction(
                senderAddress,
                nonce,
                gasPrice,
                gas,
                receiverAddress,
                amount,
                data,
                senderAddressPassword);
    }

    public static Transaction createExecuteFunctionTransaction(
            String senderAddress,
            String receiverAddress,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce) {

        return createExecuteFunctionTransaction(
                senderAddress,
                receiverAddress,
                BigInteger.ZERO,
                senderAddressPassword,
                gas,
                gasPrice,
                data,
                nonce);
    }

    public static Transaction createExecuteFunctionTransaction(
            String senderAddress,
            String receiverAddress,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce) {

        return createExecuteFunctionTransaction(
                senderAddress, receiverAddress, BigInteger.ZERO, "", gas, gasPrice, data, nonce);
    }

    public static Transaction createTryCallTransaction(String from, String to, String data) {

        return new Transaction(from, null, null, null, to, null, data);
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public BigInteger getGas() {
        return gas;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public String getData() {
        return data;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public String getSenderAddressPassword() {
        return senderAddressPassword;
    }
}
