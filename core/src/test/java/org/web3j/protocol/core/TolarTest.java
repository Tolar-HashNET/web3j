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
package org.web3j.protocol.core;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;

class TolarTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TolarTest.class);
    private static Web3j web3j;

    @BeforeAll
    public static void setup() {
        web3j = Web3j.build(new HttpService("https://tolar.dream-factory.hr/"));
    }

    @Test
    public void testNetPeerCount() throws IOException {
        NetPeerCount response = web3j.netPeerCount().send();
        Assertions.assertEquals(BigInteger.valueOf(4L), response.getQuantity());
    }

    @Test
    public void testAccountListAddresses() throws IOException {
        TolAddresses response = web3j.accountListAddresses().send();

        for (String address : response.getAddresses()) {
            System.out.println("Address: " + address);
        }
    }

    @Test
    public void testTolGetBlockCount() throws IOException {
        TolGetBlockCount response = web3j.tolGetBlockCount().send();

        System.out.println("Block count: " + response.getBlockCount());
    }

    @Test
    public void testTolGetBalance() throws IOException {
        TolGetBalance response =
                web3j.tolGetBalance(
                                "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb",
                                BigInteger.valueOf(2104397L))
                        .send();

        System.out.println("Balance: " + response.getBalance());
        System.out.println("Block index: " + response.getBlockIndex());
    }

    @Test
    public void testTolGetNonce() throws IOException {
        TolGetNonce response =
                web3j.tolGetNonce("5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb").send();

        System.out.println("Nonce: " + response.getNonce());
    }

    @Test
    public void testNetIsMasterNode() throws IOException {
        IsMasterNode response = web3j.netIsMasterNode().send();

        System.out.println("Is master node: " + response.isMasterNode());
    }
}
