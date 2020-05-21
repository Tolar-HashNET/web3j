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

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;

/**
 * Block object returned by:
 *
 * <ul>
 *   <li>eth_getBlockByHash
 *   <li>eth_getBlockByNumber
 *   <li>eth_getUncleByBlockHashAndIndex
 *   <li>eth_getUncleByBlockNumberAndIndex
 * </ul>
 *
 * <p>See <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_gettransactionbyhash">docs</a>
 * for further details.
 *
 * <p>See the following <a href="https://github.com/ethcore/parity/issues/2401">issue</a> for
 * details on additional Parity fields present in TolBlock.
 */
public class TolBlock extends Response<TolBlock.Block> {

    @Override
    @JsonDeserialize(using = TolBlock.ResponseDeserialiser.class)
    public void setResult(Block result) {
        super.setResult(result);
    }

    public Block getBlock() {
        return getResult();
    }

    public static class Block {
        @JsonProperty("block_index")
        private BigInteger blockIndex;

        @JsonProperty("previous_block_hash")
        private String previousBlockHash;

        @JsonProperty("transaction_hashes")
        private List<String> transactionHashes;

        @JsonProperty("confirmation_timestamp")
        private long confirmationTimestamp;

        public Block() {}

        public Block(
                BigInteger blockIndex,
                String previousBlockHash,
                List<String> transactionHashes,
                long confirmationTimestamp) {
            this.blockIndex = blockIndex;
            this.previousBlockHash = previousBlockHash;
            this.transactionHashes = transactionHashes;
            this.confirmationTimestamp = confirmationTimestamp;
        }

        public BigInteger getBlockIndex() {
            return blockIndex;
        }

        public void setBlockIndex(BigInteger blockIndex) {
            this.blockIndex = blockIndex;
        }

        public String getPreviousBlockHash() {
            return previousBlockHash;
        }

        public void setPreviousBlockHash(String previousBlockHash) {
            this.previousBlockHash = previousBlockHash;
        }

        public List<String> getTransactionHashes() {
            return transactionHashes;
        }

        public void setTransactionHashes(List<String> transactionHashes) {
            this.transactionHashes = transactionHashes;
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
            Block block = (Block) o;
            return blockIndex == block.blockIndex
                    && confirmationTimestamp == block.confirmationTimestamp
                    && Objects.equals(previousBlockHash, block.previousBlockHash)
                    && Objects.equals(transactionHashes, block.transactionHashes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    blockIndex, previousBlockHash, transactionHashes, confirmationTimestamp);
        }
    }

    public interface TransactionResult<T> {
        T get();
    }

    public static class TransactionHash implements TransactionResult<String> {
        private String value;

        public TransactionHash() {}

        public TransactionHash(String value) {
            this.value = value;
        }

        @Override
        public String get() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TransactionHash)) {
                return false;
            }

            TransactionHash that = (TransactionHash) o;

            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    public static class TransactionObject extends Transaction
            implements TransactionResult<Transaction> {
        public TransactionObject() {}

        public TransactionObject(
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
            super(
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

        @Override
        public Transaction get() {
            return this;
        }
    }

    public static class ResultTransactionDeserialiser
            extends JsonDeserializer<List<TransactionResult>> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public List<TransactionResult> deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {

            List<TransactionResult> transactionResults = new ArrayList<>();
            JsonToken nextToken = jsonParser.nextToken();

            if (nextToken == JsonToken.START_OBJECT) {
                Iterator<TransactionObject> transactionObjectIterator =
                        objectReader.readValues(jsonParser, TransactionObject.class);
                while (transactionObjectIterator.hasNext()) {
                    transactionResults.add(transactionObjectIterator.next());
                }
            } else if (nextToken == JsonToken.VALUE_STRING) {
                jsonParser.getValueAsString();

                Iterator<TransactionHash> transactionHashIterator =
                        objectReader.readValues(jsonParser, TransactionHash.class);
                while (transactionHashIterator.hasNext()) {
                    transactionResults.add(transactionHashIterator.next());
                }
            }

            return transactionResults;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<Block> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public Block deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, Block.class);
            } else {
                return null; // null is wrapped by Optional in above getter
            }
        }
    }
}
