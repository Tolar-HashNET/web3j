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
import java.util.Collections;

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

    @Test
    public void testNetMaxPeerCount() throws IOException {
        MaxPeerCount response = web3j.netMaxPeerCount().send();
        System.out.println("Max peer count: " + response.getMaxPeerCount());
    }

    @Test
    public void testNetMasterNodeCount() throws IOException {
        MasterNodeCount response = web3j.netMasterNodeCount().send();
        System.out.println("Master node count: " + response.getMasterNodeCount());
    }

    @Test
    public void testGetTransactionList() throws IOException {
        TolGetTransactionList response =
                web3j.tolGetTransactionList(
                                Collections.singletonList(
                                        "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb"),
                                2,
                                0)
                        .send();

        System.out.println("Transaction list:");
        response.getTransactionList().forEach(t -> System.out.println(t.getBlockHash()));
    }

    @Test
    public void testGetLatestBalance() throws IOException {
        TolGetLatestBalance response =
                web3j.tolGetLatestBalance("5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb")
                        .send();

        System.out.println("Latest balance: " + response.getLatestBalance());
        System.out.println("Block index: " + response.getBlockIndex());
    }

    @Test
    public void testGetBlockchainInfo() throws IOException {
        TolGetBlockchainInfo response = web3j.tolGetBlockchainInfo().send();

        System.out.println("Last confirmed block hash: " + response.getLastConfirmedBlockHash());
        System.out.println("Confirmed blocks count: " + response.getConfirmedBlocksCount());
        System.out.println("Total block count: " + response.getTotalBlockCount());
    }

    @Test
    public void testAccountCreate() throws IOException {
        AccountCreate response = web3j.accountCreate("password").send();
        System.out.println("Is account created: " + response.isCreated());
    }

    @Test
    public void testAccountOpen() throws IOException {
        AccountOpen response = web3j.accountOpen("password").send();
        System.out.println("Is account opened: " + response.isOpened());
    }

    @Test
    public void testAccountVerifyAddress() throws IOException {
        AccountVerifyAddress response =
                web3j.accountVerifyAddress("5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb")
                        .send();
        Assertions.assertTrue(response.isVerified());
    }

    @Test
    public void testAccountCreateNewAddress() throws IOException {
        AccountCreateNewAddress response =
                web3j.accountCreateNewAddress("name", "Password123", "hint").send();
        System.out.println("Created address: " + response.getAddress());
    }

    @Test
    public void testExportKeyFile() throws IOException {
        AccountExportKeyFile response =
                web3j.accountExportKeyFile("5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb")
                        .send();

        System.out.println("Key file: " + response.getKeyFile());
    }
}
