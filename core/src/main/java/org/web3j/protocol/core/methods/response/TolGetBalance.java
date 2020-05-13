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

import com.fasterxml.jackson.annotation.JsonProperty;

import org.web3j.protocol.core.Response;

/** eth_getBalance. */
public class TolGetBalance extends Response<TolGetBalance.BlockBalance> {

    public BigInteger getBalance() {
        return getResult().getBalance();
    }

    public BigInteger getBlockIndex() {
        return getResult().getBlockIndex();
    }

    public static class BlockBalance {
        private BigInteger balance;

        @JsonProperty("block_index")
        private BigInteger blockIndex;

        public BlockBalance() {}

        public BlockBalance(BigInteger balance, BigInteger blockIndex) {
            this.balance = balance;
            this.blockIndex = blockIndex;
        }

        public BigInteger getBalance() {
            return balance;
        }

        public BigInteger getBlockIndex() {
            return blockIndex;
        }

        public void setBalance(BigInteger balance) {
            this.balance = balance;
        }

        public void setBlockIndex(BigInteger blockIndex) {
            this.blockIndex = blockIndex;
        }
    }
}
