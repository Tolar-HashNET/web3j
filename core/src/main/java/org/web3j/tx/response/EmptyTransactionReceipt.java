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
package org.web3j.tx.response;

import java.math.BigInteger;
import java.util.List;

import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * An empty transaction receipt object containing only the transaction hash. This is to support the
 * {@link QueuingTransactionReceiptProcessor} and {@link NoOpProcessor}.
 */
public class EmptyTransactionReceipt extends TransactionReceipt {

    public EmptyTransactionReceipt(String transactionHash) {
        super();
        this.setHash(transactionHash);
    }

    @Override
    public String getHash() {
        return super.getHash();
    }

    @Override
    public void setHash(String hash) {
        super.setHash(hash);
    }

    private UnsupportedOperationException unsupportedOperation() {
        return new UnsupportedOperationException(
                "Empty transaction receipt, only transaction hash is available");
    }

    @Override
    public String getTransactionIndex() {
        throw unsupportedOperation();
    }

    @Override
    public void setTransactionIndex(String transactionIndex) {
        throw unsupportedOperation();
    }

    @Override
    public String getBlockHash() {
        throw unsupportedOperation();
    }

    @Override
    public void setBlockHash(String blockHash) {
        throw unsupportedOperation();
    }

    @Override
    public BigInteger getBlockNumber() {
        throw unsupportedOperation();
    }

    @Override
    public void setBlockNumber(BigInteger blockNumber) {
        throw unsupportedOperation();
    }

    @Override
    public BigInteger getGasUsed() {
        throw unsupportedOperation();
    }

    @Override
    public void setGasUsed(BigInteger gasUsed) {
        throw unsupportedOperation();
    }

    @Override
    public String getNewAddress() {
        throw unsupportedOperation();
    }

    @Override
    public void setNewAddress(String newAddress) {
        throw unsupportedOperation();
    }

    @Override
    public String getSenderAddress() {
        throw unsupportedOperation();
    }

    @Override
    public void setSenderAddress(String senderAddress) {
        throw unsupportedOperation();
    }

    @Override
    public String getReceiverAddress() {
        throw unsupportedOperation();
    }

    @Override
    public void setReceiverAddress(String receiverAddress) {
        throw unsupportedOperation();
    }

    @Override
    public List<Log> getLogs() {
        throw unsupportedOperation();
    }

    @Override
    public void setLogs(List<Log> logs) {
        throw unsupportedOperation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionReceipt)) {
            return false;
        }

        TransactionReceipt that = (TransactionReceipt) o;

        return getHash() != null ? getHash().equals(that.getHash()) : that.getHash() == null;
    }

    @Override
    public int hashCode() {
        return getHash() != null ? getHash().hashCode() : 0;
    }
}
