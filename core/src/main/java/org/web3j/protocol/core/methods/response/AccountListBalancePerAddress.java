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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.web3j.protocol.core.Response;

public class AccountListBalancePerAddress
        extends Response<List<AccountListBalancePerAddress.AddressBalance>> {

    public List<AddressBalance> getListBalancePerAddress() {
        return getResult();
    }

    public static class AddressBalance {
        private String address;
        private BigInteger balance;

        @JsonProperty("address_name")
        private String addressName;

        public AddressBalance() {}

        public AddressBalance(String address, BigInteger balance, String addressName) {
            this.address = address;
            this.balance = balance;
            this.addressName = addressName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public BigInteger getBalance() {
            return balance;
        }

        public void setBalance(BigInteger balance) {
            this.balance = balance;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }
    }
}
