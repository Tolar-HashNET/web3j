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
package org.web3j.crypto;

import java.math.BigInteger;

import org.web3j.utils.Numeric;

/**
 * Transaction class used for signing transactions locally.<br>
 * For the specification, refer to p4 of the <a href="http://gavwood.com/paper.pdf">yellow
 * paper</a>.
 */
public class RawTransaction {

    private String senderAddress;
    private String receiverAddress;
    private BigInteger amount;
    private String senderAddressPassword;
    private BigInteger gas;
    private BigInteger gasPrice;
    private String data;
    private BigInteger nonce;

    protected RawTransaction(String senderAddress, String receiverAddress, BigInteger amount, String senderAddressPassword, BigInteger gas, BigInteger gasPrice, String data, BigInteger nonce) {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.amount = amount;
        this.senderAddressPassword = senderAddressPassword;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.data = data;
        this.nonce = nonce;
    }

    public static RawTransaction createDeployContractTransaction(
            String senderAddress,
            BigInteger amount,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce) {

        return new RawTransaction(
                senderAddress, null, amount, senderAddressPassword, gas, gasPrice, data, nonce);
    }

    public static RawTransaction createFundTransferTransaction(
            String senderAddress,
            String receiverAddress,
            BigInteger amount,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            BigInteger nonce) {

        return new RawTransaction(
                senderAddress,
                receiverAddress,
                amount,
                senderAddressPassword,
                gas,
                gasPrice,
                null,
                nonce);
    }

    public static RawTransaction createTransaction(String senderAddress, String receiverAddress, String senderAddressPassword, BigInteger gas, BigInteger gasPrice,
            String data, BigInteger nonce) {
        return createTransaction(senderAddress, receiverAddress, BigInteger.ZERO, senderAddressPassword, gas, gasPrice, data, nonce);
    }

    public static RawTransaction createTransaction(
            String senderAddress,
            String receiverAddress,
            BigInteger amount,
            String senderAddressPassword,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce) {

        return new RawTransaction(senderAddress, receiverAddress, amount, senderAddressPassword, gas, gasPrice, data, nonce);
    }

    public static RawTransaction createTransaction(String receiverAddress, BigInteger amount, BigInteger gas, BigInteger gasPrice, String data, BigInteger nonce) {
        return createTransaction(null, receiverAddress, amount, "", gas, gasPrice, data, nonce);
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public String getSenderAddressPassword() {
        return senderAddressPassword;
    }

    public BigInteger getGas() {
        return gas;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public String getData() {
        return data;
    }

    public BigInteger getNonce() {
        return nonce;
    }
}
