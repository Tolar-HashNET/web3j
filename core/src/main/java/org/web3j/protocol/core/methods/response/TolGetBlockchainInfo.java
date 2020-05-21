/*
 * Copyright 2020 Web3 Labs Ltd.
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

import com.fasterxml.jackson.annotation.JsonProperty;

import org.web3j.protocol.core.Response;

public class TolGetBlockchainInfo extends Response<TolGetBlockchainInfo.BlockchainInfo> {

    public BigInteger getConfirmedBlocksCount() {
        return getResult().getConfirmedBlocksCount();
    }

    public BigInteger getTotalBlockCount() {
        return getResult().getTotalBlockCount();
    }

    public String getLastConfirmedBlockHash() {
        return getResult().getLastConfirmedBlockHash();
    }

    public static class BlockchainInfo {
        @JsonProperty("confirmed_blocks_count")
        private BigInteger confirmedBlocksCount;

        @JsonProperty("total_block_count")
        private BigInteger totalBlockCount;

        @JsonProperty("last_confirmed_block_hash")
        private String lastConfirmedBlockHash;

        public BlockchainInfo() {}

        public BlockchainInfo(
                BigInteger confirmedBlocksCount,
                BigInteger totalBlockCount,
                String lastConfirmedBlockHash) {
            this.confirmedBlocksCount = confirmedBlocksCount;
            this.totalBlockCount = totalBlockCount;
            this.lastConfirmedBlockHash = lastConfirmedBlockHash;
        }

        public BigInteger getConfirmedBlocksCount() {
            return confirmedBlocksCount;
        }

        public void setConfirmedBlocksCount(BigInteger confirmedBlocksCount) {
            this.confirmedBlocksCount = confirmedBlocksCount;
        }

        public BigInteger getTotalBlockCount() {
            return totalBlockCount;
        }

        public void setTotalBlockCount(BigInteger totalBlockCount) {
            this.totalBlockCount = totalBlockCount;
        }

        public String getLastConfirmedBlockHash() {
            return lastConfirmedBlockHash;
        }

        public void setLastConfirmedBlockHash(String lastConfirmedBlockHash) {
            this.lastConfirmedBlockHash = lastConfirmedBlockHash;
        }
    }
}
