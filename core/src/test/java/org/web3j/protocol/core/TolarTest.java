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

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.ClientConnectionException;
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
    @Ignore
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

    @Test
    public void testImportKeyFile() throws IOException {
        AccountImportKeyFile response =
                web3j.accountImportKeyFile(
                                "Key file: {\n"
                                        + "    \"address\" : \"84c512b1cf3d45e7506a772b7358375acc571b29\",\n"
                                        + "    \"crypto\" : {\n"
                                        + "        \"cipher\" : \"aes-128-ctr\",\n"
                                        + "        \"cipherparams\" : {\n"
                                        + "            \"iv\" : \"26cdcb58f5057c4f3f04468ae9d9b7b1\"\n"
                                        + "        },\n"
                                        + "        \"ciphertext\" : \"9177eba69ff70349d52a4c96b6e98eec2717e5e0218d5f4da143b894111681a9\",\n"
                                        + "        \"kdf\" : \"scrypt\",\n"
                                        + "        \"kdfparams\" : {\n"
                                        + "            \"dklen\" : 32,\n"
                                        + "            \"n\" : 262144,\n"
                                        + "            \"p\" : 1,\n"
                                        + "            \"r\" : 8,\n"
                                        + "            \"salt\" : \"21f0d2c7eb0cf00d96461bacd023a741ebacfd446fba01b9849399ce32d9a416\"\n"
                                        + "        },\n"
                                        + "        \"mac\" : \"963d2541fc26e05b5ff80272632e62060e5394e980b6a31affbaa6f6d09683c4\"\n"
                                        + "    },\n"
                                        + "    \"id\" : \"d90f9e3d-9b1c-cd85-99b7-5161379c97b1\",\n"
                                        + "    \"version\" : 3\n"
                                        + "}",
                                "name",
                                "Password123",
                                "hint")
                        .send();

        Assertions.assertFalse(response.isSuccessful());
    }

    @Test
    public void testListBalancePerAddress() throws IOException {
        AccountListBalancePerAddress response = web3j.accountListBalancePerAddress().send();

        response.getListBalancePerAddress()
                .forEach(
                        t ->
                                System.out.println(
                                        "Address: "
                                                + t.getAddress()
                                                + "\n Balance: "
                                                + t.getBalance()
                                                + "\n Name: "
                                                + t.getAddressName()));
    }

    @Test
    public void testAccountChangePassword() {
        Assertions.assertThrows(
                ClientConnectionException.class,
                () -> web3j.accountChangePassword("oldPassword123", "newPassword123").send());
    }

    @Test
    @Ignore
    public void testAccountChangeAddressPassword() throws IOException {
        AccountChangeAddressPassword response =
                web3j.accountChangeAddressPassword(
                                "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf",
                                "Password123",
                                "Password1234")
                        .send();
        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testTolGetBlockByHash() throws IOException {
        TolBlock response =
                web3j.tolGetBlockByHash(
                                "93f0c1766bf3fe06b7a29b51ceb6626906b0d8d654f33b5c1b50768fab06bb9f")
                        .send();
        System.out.println("Transaction hashes: ");
        response.getBlock().getTransactionHashes().forEach(System.out::println);
        System.out.println("Block index: " + response.getBlock().getBlockIndex());
        System.out.println(
                "Confirmation timestamp: " + response.getBlock().getConfirmationTimestamp());
        System.out.println("Previous block hash: " + response.getBlock().getPreviousBlockHash());
    }

    @Test
    public void testTolGetBlockByIndex() throws IOException {
        TolBlock blockByHash =
                web3j.tolGetBlockByHash(
                                "93f0c1766bf3fe06b7a29b51ceb6626906b0d8d654f33b5c1b50768fab06bb9f")
                        .send();
        TolBlock blockByIndex =
                web3j.tolGetBlockByIndex(
                                DefaultBlockParameter.valueOf(
                                        blockByHash.getBlock().getBlockIndex()))
                        .send();

        Assertions.assertEquals(
                blockByHash.getBlock().getBlockIndex(), blockByIndex.getBlock().getBlockIndex());
        Assertions.assertEquals(
                blockByHash.getBlock().getConfirmationTimestamp(),
                blockByIndex.getBlock().getConfirmationTimestamp());
        Assertions.assertEquals(
                blockByHash.getBlock().getPreviousBlockHash(),
                blockByIndex.getBlock().getPreviousBlockHash());

        System.out.println("Transaction hashes: ");
        blockByIndex.getBlock().getTransactionHashes().forEach(System.out::println);
    }

    @Test
    public void testTolGetTransaction() throws IOException {
        TolTransaction response =
                web3j.tolGetTransaction(
                                "808cf4ff160048ce58de4869ea11fe43c62a72f63a7df415d18f33ac0f6c769b")
                        .send();

        if (response.getTransaction().isPresent()) {
            Transaction transaction = response.getTransaction().get();
            Assertions.assertEquals(
                    "93f0c1766bf3fe06b7a29b51ceb6626906b0d8d654f33b5c1b50768fab06bb9f",
                    transaction.getBlockHash());
            Assertions.assertEquals(BigInteger.valueOf(0L), transaction.getTransactionIndex());
            Assertions.assertEquals(
                    "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb",
                    transaction.getSenderAddress());
            Assertions.assertEquals(
                    "540dc971237be2361e04c1643d57b572709db15e449a870fef",
                    transaction.getReceiverAddress());
            Assertions.assertEquals(BigInteger.valueOf(1L), transaction.getValue());
            Assertions.assertEquals(BigInteger.valueOf(210000L), transaction.getGas());
            Assertions.assertEquals(BigInteger.valueOf(1L), transaction.getGasPrice());
            Assertions.assertEquals("kitula", transaction.getData());
            Assertions.assertEquals(BigInteger.valueOf(30L), transaction.getNonce());
            Assertions.assertEquals(BigInteger.valueOf(21000L), transaction.getGasUsed());
            Assertions.assertEquals(BigInteger.valueOf(0L), transaction.getGasRefunded());
            Assertions.assertEquals(
                    "54000000000000000000000000000000000000000023199e2b",
                    transaction.getNewAddress());
            Assertions.assertEquals("", transaction.getOutput());
            Assertions.assertFalse(transaction.isExcepted());
            Assertions.assertEquals(
                    BigInteger.valueOf(1588850261994L), transaction.getConfirmationTimestamp());
        }
    }

    @Test
    public void testTolTryCallTransaction() throws IOException {
        org.web3j.protocol.core.methods.request.Transaction transaction =
                new org.web3j.protocol.core.methods.request.Transaction(
                        "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb",
                        BigInteger.valueOf(28L),
                        BigInteger.valueOf(1L),
                        BigInteger.valueOf(6000000L),
                        "5457c2d11f05725f4fa5c0cd119b75415b95cd40d059dfc2d5",
                        BigInteger.valueOf(0L),
                        "0xcfae3217");
        TolTryCallTransaction response = web3j.tolTryCallTransaction(transaction).send();
        Assertions.assertFalse(response.isExcepted());
        Assertions.assertEquals(
                response.getOutput(),
                "00000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000007506f7a6472617600000000000000000000000000000000000000000000000000");
    }

    @Test
    public void testTolGetTransactionReceipt() throws IOException {
        TolGetTransactionReceipt response =
                web3j.tolGetTransactionReceipt(
                                "808cf4ff160048ce58de4869ea11fe43c62a72f63a7df415d18f33ac0f6c769b")
                        .send();

        if (response.getTransactionReceipt().isPresent()) {
            TransactionReceipt receipt = response.getTransactionReceipt().get();
            System.out.println(receipt.getTransactionIndex());
        }
    }

    @Test
    public void testTolGetGasEstimate() {
        org.web3j.protocol.core.methods.request.Transaction transaction =
                new org.web3j.protocol.core.methods.request.Transaction(
                        "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb",
                        BigInteger.valueOf(7L),
                        BigInteger.valueOf(1L),
                        BigInteger.valueOf(6000000L),
                        "5457c2d11f05725f4fa5c0cd119b75415b95cd40d059dfc2d5",
                        BigInteger.valueOf(0L),
                        "0xcfae3217");
        Assertions.assertThrows(
                ClientConnectionException.class, () -> web3j.tolGetGasEstimate(transaction).send());
    }

    @Test
    @Ignore
    public void testAccountSendRawTransaction() throws IOException {
        org.web3j.protocol.core.methods.request.Transaction transaction =
                new org.web3j.protocol.core.methods.request.Transaction(
                        "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb",
                        BigInteger.valueOf(33L),
                        BigInteger.valueOf(1L),
                        BigInteger.valueOf(6000000L),
                        "5457c2d11f05725f4fa5c0cd119b75415b95cd40d059dfc2d5",
                        BigInteger.valueOf(0L),
                        "0xcfae3217",
                        "Password123");

        EthSendTransaction response = web3j.accountSendRawTransaction(transaction).send();
        System.out.println("Transaction hash: " + response.getTransactionHash());
    }
}
