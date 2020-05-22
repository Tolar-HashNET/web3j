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
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/** TransactionReceipt object used by {@link TolGetTransactionReceipt}. */
public class TransactionReceipt {
    @JsonProperty("block_hash")
    private String blockHash;

    @JsonProperty("transaction_index")
    private String transactionIndex;

    @JsonProperty("sender_address")
    private String senderAddress;

    @JsonProperty("receiver_address")
    private String receiverAddress;

    @JsonProperty("gas_used")
    private BigInteger gasUsed;

    @JsonProperty("new_address")
    private String newAddress;

    private boolean excepted;

    @JsonProperty("block_number")
    private BigInteger blockNumber;

    private String hash;
    private List<Log> logs;

    public TransactionReceipt() {}

    public TransactionReceipt(
            String blockHash,
            String transactionIndex,
            String senderAddress,
            String receiverAddress,
            BigInteger gasUsed,
            String newAddress,
            boolean excepted,
            BigInteger blockNumber,
            String hash,
            List<Log> logs) {
        this.blockHash = blockHash;
        this.transactionIndex = transactionIndex;
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.gasUsed = gasUsed;
        this.newAddress = newAddress;
        this.excepted = excepted;
        this.blockNumber = blockNumber;
        this.hash = hash;
        this.logs = logs;
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

    public BigInteger getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public boolean isExcepted() {
        return excepted;
    }

    public void setExcepted(boolean excepted) {
        this.excepted = excepted;
    }

    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionReceipt that = (TransactionReceipt) o;
        return excepted == that.excepted
                && Objects.equals(blockHash, that.blockHash)
                && Objects.equals(transactionIndex, that.transactionIndex)
                && Objects.equals(senderAddress, that.senderAddress)
                && Objects.equals(receiverAddress, that.receiverAddress)
                && Objects.equals(gasUsed, that.gasUsed)
                && Objects.equals(newAddress, that.newAddress)
                && Objects.equals(blockNumber, that.blockNumber)
                && Objects.equals(hash, that.hash)
                && Objects.equals(logs, that.logs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                blockHash,
                transactionIndex,
                senderAddress,
                receiverAddress,
                gasUsed,
                newAddress,
                excepted,
                blockNumber,
                hash,
                logs);
    }

    @Override
    public String toString() {
        return "TransactionReceipt{"
                + "blockHash='"
                + blockHash
                + '\''
                + ", transactionIndex='"
                + transactionIndex
                + '\''
                + ", senderAddress='"
                + senderAddress
                + '\''
                + ", receiverAddress='"
                + receiverAddress
                + '\''
                + ", gasUsed='"
                + gasUsed
                + '\''
                + ", newAddress='"
                + newAddress
                + '\''
                + ", excepted="
                + excepted
                + ", blockNumber="
                + blockNumber
                + ", hash='"
                + hash
                + '\''
                + ", logs="
                + logs
                + '}';
    }
}
