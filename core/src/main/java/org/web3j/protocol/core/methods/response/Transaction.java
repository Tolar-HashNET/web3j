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
package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Transaction object used by both {@link TolTransaction} and {@link TolBlock}. */
public class Transaction {
    @JsonProperty("block_hash")
    private String blockHash;

    @JsonProperty("transaction_index")
    private BigInteger transactionIndex;

    @JsonProperty("sender_address")
    private String senderAddress;

    @JsonProperty("receiver_address")
    private String receiverAddress;

    private BigInteger value;
    private BigInteger gas;

    @JsonProperty("gas_price")
    private BigInteger gasPrice;

    private String data;
    private BigInteger nonce;

    @JsonProperty("gas_used")
    private BigInteger gasUsed;

    @JsonProperty("gas_refunded")
    private BigInteger gasRefunded;

    @JsonProperty("new_address")
    private String newAddress;

    private String output;
    private boolean excepted;

    @JsonProperty("confirmation_timestamp")
    private BigInteger confirmationTimestamp;

    public Transaction() {}

    public Transaction(
            String blockHash,
            BigInteger transactionIndex,
            String senderAddress,
            String receiverAddress,
            BigInteger value,
            BigInteger gas,
            BigInteger gasPrice,
            String data,
            BigInteger nonce,
            BigInteger gasUsed,
            BigInteger gasRefunded,
            String newAddress,
            String output,
            boolean excepted,
            BigInteger confirmationTimestamp) {
        this.blockHash = blockHash;
        this.transactionIndex = transactionIndex;
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.value = value;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.data = data;
        this.nonce = nonce;
        this.gasUsed = gasUsed;
        this.gasRefunded = gasRefunded;
        this.newAddress = newAddress;
        this.output = output;
        this.excepted = excepted;
        this.confirmationTimestamp = confirmationTimestamp;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public BigInteger getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(BigInteger transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public BigInteger getGas() {
        return gas;
    }

    public void setGas(BigInteger gas) {
        this.gas = gas;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public BigInteger getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
    }

    public BigInteger getGasRefunded() {
        return gasRefunded;
    }

    public void setGasRefunded(BigInteger gasRefunded) {
        this.gasRefunded = gasRefunded;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean isExcepted() {
        return excepted;
    }

    public void setExcepted(boolean excepted) {
        this.excepted = excepted;
    }

    public BigInteger getConfirmationTimestamp() {
        return confirmationTimestamp;
    }

    public void setConfirmationTimestamp(BigInteger confirmationTimestamp) {
        this.confirmationTimestamp = confirmationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return excepted == that.excepted
                && confirmationTimestamp == that.confirmationTimestamp
                && Objects.equals(blockHash, that.blockHash)
                && Objects.equals(transactionIndex, that.transactionIndex)
                && Objects.equals(senderAddress, that.senderAddress)
                && Objects.equals(receiverAddress, that.receiverAddress)
                && Objects.equals(value, that.value)
                && Objects.equals(gas, that.gas)
                && Objects.equals(gasPrice, that.gasPrice)
                && Objects.equals(data, that.data)
                && Objects.equals(nonce, that.nonce)
                && Objects.equals(gasUsed, that.gasUsed)
                && Objects.equals(gasRefunded, that.gasRefunded)
                && Objects.equals(newAddress, that.newAddress)
                && Objects.equals(output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                blockHash,
                transactionIndex,
                senderAddress,
                receiverAddress,
                value,
                gas,
                gasPrice,
                data,
                nonce,
                gasUsed,
                gasRefunded,
                newAddress,
                output,
                excepted,
                confirmationTimestamp);
    }
}
