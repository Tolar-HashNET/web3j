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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Transaction object used by both {@link TolTransaction} and {@link TolBlock}. */
public class Transaction {
    @JsonProperty("block_hash")
    private String blockHash;

    @JsonProperty("transaction_index")
    private String transactionIndex;

    @JsonProperty("sender_address")
    private String senderAddress;

    @JsonProperty("receiver_address")
    private String receiverAddress;

    private String value;
    private String gas;

    @JsonProperty("gas_price")
    private String gasPrice;

    private String data;
    private String nonce;

    @JsonProperty("gas_used")
    private String gasUsed;

    @JsonProperty("gas_refunded")
    private String gasRefunded;

    @JsonProperty("new_address")
    private String newAddress;

    private String output;
    private boolean excepted;

    @JsonProperty("confirmation_timestamp")
    private long confirmationTimestamp;

    public Transaction() {}

    public Transaction(
            String blockHash,
            String transactionIndex,
            String senderAddress,
            String receiverAddress,
            String value,
            String gas,
            String gasPrice,
            String data,
            String nonce,
            String gasUsed,
            String gasRefunded,
            String newAddress,
            String output,
            boolean excepted,
            long confirmationTimestamp) {
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

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(String transactionIndex) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getGasRefunded() {
        return gasRefunded;
    }

    public void setGasRefunded(String gasRefunded) {
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

    public long getConfirmationTimestamp() {
        return confirmationTimestamp;
    }

    public void setConfirmationTimestamp(long confirmationTimestamp) {
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
