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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.SignedTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

class TolarTest {
    private static Web3j web3j;

    @BeforeAll
    public static void setup() {
        web3j = Web3j.build(new HttpService("https://tolar.dream-factory.hr/"));
    }

    @Test
    public void testNetPeerCount() throws IOException {
        NetPeerCount response = web3j.netPeerCount().send();
        Assertions.assertEquals(4, response.getQuantity().intValue());
    }

    @Test
    public void testAccountListAddresses() throws IOException {
        TolAddresses response = web3j.accountListAddresses().send();
        Assertions.assertTrue(response.getAddresses().size() > 4);
    }

    @Test
    public void testTolGetBlockCount() throws IOException {
        TolGetBlockCount response = web3j.tolGetBlockCount().send();
        Assertions.assertTrue(response.getBlockCount().intValue() > 200_000);
    }

    @Test
    public void testTolGetBalance() throws IOException {
        TolGetBalance response =
                web3j.tolGetBalance(
                                "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb",
                                BigInteger.valueOf(200_000))
                        .send();
        Assertions.assertTrue(response.getBalance().intValue() > 1_000);
    }

    @Test
    public void testTolGetNonce() throws IOException {
        TolGetNonce response =
                web3j.tolGetNonce("5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb").send();

        Assertions.assertTrue(response.getNonce().intValue() > 0);
    }

    @Test
    public void testNetIsMasterNode() throws IOException {
        IsMasterNode response = web3j.netIsMasterNode().send();
        Assertions.assertFalse(response.isMasterNode());
    }

    @Test
    public void testNetIsMasterNodeStaging() throws IOException {
        Web3j stagingWeb3j =
                Web3j.build(new HttpService("https://tolar-staging.dream-factory.hr/"));
        IsMasterNode response = stagingWeb3j.netIsMasterNode().send();
        Assertions.assertTrue(response.isMasterNode());
    }

    @Test
    public void testNetMaxPeerCount() throws IOException {
        MaxPeerCount response = web3j.netMaxPeerCount().send();
        Assertions.assertTrue(response.getMaxPeerCount() > 4);
    }

    @Test
    public void testNetMasterNodeCount() throws IOException {
        MasterNodeCount response = web3j.netMasterNodeCount().send();
        Assertions.assertTrue(response.getMasterNodeCount() > 1);
    }

    @Test
    @Disabled("manual test")
    public void testGetTransactionList() throws IOException {
        TolGetTransactionList response =
                web3j.tolGetTransactionList(
                                Collections.singletonList(
                                        "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb"),
                                2,
                                0)
                        .send();

        // todo: not working on mainnet atm, enable it when it's working again
        Assertions.assertFalse(response.getTransactionList().isEmpty());
    }

    @Test
    public void testGetTransactionListStaging() throws IOException {
        Web3j stagingWeb3j =
                Web3j.build(new HttpService("https://tolar-staging.dream-factory.hr/"));
        TolGetTransactionList response =
                stagingWeb3j
                        .tolGetTransactionList(
                                Collections.singletonList(
                                        "5493b8597964a2a7f0c93c49f9e4c4a170e0c42a5eb3beda0d"),
                                2,
                                0)
                        .send();

        Assertions.assertFalse(response.getTransactionList().isEmpty());
    }

    @Test
    public void testGetLatestBalance() throws IOException {
        TolGetLatestBalance response =
                web3j.tolGetLatestBalance("5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb")
                        .send();
        Assertions.assertTrue(response.getLatestBalance().intValue() > 1_000);
    }

    @Test
    public void testGetBlockchainInfo() throws IOException {
        TolGetBlockchainInfo response = web3j.tolGetBlockchainInfo().send();
        Assertions.assertTrue(response.getConfirmedBlocksCount().intValue() > 10);
    }

    @Test
    @Disabled("manual test")
    public void testAccountCreate() throws IOException {
        AccountCreate response = web3j.accountCreate("password").send();
        Assertions.assertTrue(response.isCreated());
    }

    @Test
    @Disabled("manual test")
    public void testAccountOpen() throws IOException {
        AccountOpen response = web3j.accountOpen("veryDifferentPassword123").send();
        Assertions.assertTrue(response.isOpened());
    }

    @Test
    public void testAccountVerifyAddress() throws IOException {
        AccountVerifyAddress response =
                web3j.accountVerifyAddress("5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb")
                        .send();
        Assertions.assertTrue(response.isVerified());
    }

    @Test
    @Disabled("manual test")
    public void testAccountCreateNewAddress() throws IOException {
        AccountCreateNewAddress response =
                web3j.accountCreateNewAddress("nameFromTest", "Password123", "hint").send();
        System.out.println("Created address: " + response.getAddress());
        Assertions.assertNotNull(response.getAddress());
    }

    @Test
    public void testExportKeyFile() throws IOException {
        AccountExportKeyFile response =
                web3j.accountExportKeyFile("54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf")
                        .send();
        Assertions.assertNotNull(response.getKeyFile());
    }

    @Test
    public void testImportKeyFile() throws IOException {
        AccountImportKeyFile response =
                web3j.accountImportKeyFile(
                                "{\n"
                                        + "    \"address\" : \"b24647dc5a34b858eae00a1977b7263c66f1af12\",\n"
                                        + "    \"crypto\" : {\n"
                                        + "        \"cipher\" : \"aes-128-ctr\",\n"
                                        + "        \"cipherparams\" : {\n"
                                        + "            \"iv\" : \"4efedccb4438efa973a018cb4733e45d\"\n"
                                        + "        },\n"
                                        + "        \"ciphertext\" : \"8884322f8e490178b6e457141ae364fa9facc807aa63cce82cbb8ed3cb053cf5\",\n"
                                        + "        \"kdf\" : \"scrypt\",\n"
                                        + "        \"kdfparams\" : {\n"
                                        + "            \"dklen\" : 32,\n"
                                        + "            \"n\" : 262144,\n"
                                        + "            \"p\" : 1,\n"
                                        + "            \"r\" : 8,\n"
                                        + "            \"salt\" : \"a04f5d8337713171d11a20dda597e50028e34a09ce366adeff26a508dc1f3410\"\n"
                                        + "        },\n"
                                        + "        \"mac\" : \"1541ea4c25e689d4f4fe0018787fc8e2fdf517740e675386ccc3b4a42a272385\"\n"
                                        + "    },\n"
                                        + "    \"id\" : \"b16917de-a380-47bd-5d4c-0f5df0cd0302\",\n"
                                        + "    \"version\" : 3\n"
                                        + "}",
                                "importTestAddress",
                                "Password1234",
                                "hint")
                        .send();

        Assertions.assertTrue(response.isSuccessful());
    }

    @Test
    public void testListBalancePerAddress() throws IOException {
        AccountListBalancePerAddress response = web3j.accountListBalancePerAddress().send();
        Assertions.assertFalse(response.getListBalancePerAddress().isEmpty());
    }

    @Test
    @Disabled("manual test")
    public void testAccountChangePassword() throws Exception {
        AccountChangePassword send =
                web3j.accountChangePassword("oldPassword123", "newPassword123").send();
        Assertions.assertTrue(send.getResult());
    }

    @Test
    @Disabled("manual test")
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
                                "e11bf6dde22a2c7b14d8931363d6737104ece8df6524500fe918520ff494be6b")
                        .send();
        Assertions.assertNotNull(response.getResult());
    }

    @Test
    public void testTolGetBlockByIndex() throws IOException {
        TolBlock blockByIndex =
                web3j.tolGetBlockByIndex(DefaultBlockParameter.valueOf(BigInteger.TEN)).send();

        Assertions.assertEquals(BigInteger.TEN, blockByIndex.getBlock().getBlockIndex());
    }

    @Test
    public void testTolGetTransaction() throws IOException {
        TolTransaction response =
                web3j.tolGetTransaction(
                                "0abfdd5b4b2d2e55b3cda4b0959ea7cf4de0cd0e8fb4804105ad2eec5c2ae9ae")
                        .send();

        if (response.getTransaction().isPresent()) {
            Transaction transaction = response.getTransaction().get();
            Assertions.assertEquals(
                    "2358d7fa3d13649a8dac2292f5a3b3b9efc4632cee40ed0b0f08fa612de51385",
                    transaction.getBlockHash());
            Assertions.assertEquals(BigInteger.valueOf(0L), transaction.getTransactionIndex());
            Assertions.assertEquals(
                    "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb",
                    transaction.getSenderAddress());
            Assertions.assertEquals(
                    "54000000000000000000000000000000000000000023199e2b",
                    transaction.getReceiverAddress());
            Assertions.assertEquals(BigInteger.ZERO, transaction.getValue());
            Assertions.assertEquals(BigInteger.valueOf(6_000_000L), transaction.getGas());
            Assertions.assertEquals(BigInteger.valueOf(1L), transaction.getGasPrice());
            Assertions.assertTrue(transaction.getData().length() > 500);
            Assertions.assertEquals(BigInteger.ONE, transaction.getNonce());
            Assertions.assertEquals(BigInteger.valueOf(249053L), transaction.getGasUsed());
            Assertions.assertEquals(BigInteger.valueOf(0L), transaction.getGasRefunded());
            Assertions.assertEquals(
                    "5422de393d7a0d716cdf1f9ea12d85f6c880b4570b17e1a7f9",
                    transaction.getNewAddress());
            Assertions.assertTrue(transaction.getOutput().length() > 500);
            Assertions.assertFalse(transaction.isExcepted());
            Assertions.assertEquals(
                    BigInteger.valueOf(1591177492201L), transaction.getConfirmationTimestamp());
        } else {
            Assertions.fail("missing transaction");
        }
    }

    @Test
    public void testTolTryCallTransaction() throws IOException {
        org.web3j.protocol.core.methods.request.Transaction transaction =
                new org.web3j.protocol.core.methods.request.Transaction(
                        "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb",
                        BigInteger.ZERO,
                        BigInteger.valueOf(1L),
                        BigInteger.valueOf(6000000L),
                        "5422de393d7a0d716cdf1f9ea12d85f6c880b4570b17e1a7f9",
                        BigInteger.valueOf(0L),
                        "0xcfae3217");
        TolTryCallTransaction response = web3j.tolTryCallTransaction(transaction).send();
        Assertions.assertFalse(response.isExcepted());
        Assertions.assertEquals(
                "000000000000000000000000000000000000000000000000000000000000002000000000000"
                        + "00000000000000000000000000000000000000000000000000007426f6b626f6b21000000000"
                        + "00000000000000000000000000000000000000000",
                response.getOutput());
    }

    @Test
    public void testTolGetTransactionReceipt() throws IOException {
        TolGetTransactionReceipt response =
                web3j.tolGetTransactionReceipt(
                                "0abfdd5b4b2d2e55b3cda4b0959ea7cf4de0cd0e8fb4804105ad2eec5c2ae9ae")
                        .send();

        if (response.getTransactionReceipt().isPresent()) {
            TransactionReceipt receipt = response.getTransactionReceipt().get();
            Assertions.assertEquals(
                    "2358d7fa3d13649a8dac2292f5a3b3b9efc4632cee40ed0b0f08fa612de51385",
                    receipt.getBlockHash());
        } else {
            Assertions.fail();
        }
    }

    @Test
    public void testTolGetGasEstimate() throws Exception {
        org.web3j.protocol.core.methods.request.Transaction transaction =
                new org.web3j.protocol.core.methods.request.Transaction(
                        "5484c512b1cf3d45e7506a772b7358375acc571b2930d27deb",
                        BigInteger.valueOf(7L),
                        BigInteger.valueOf(1L),
                        BigInteger.valueOf(6000000L),
                        "5457c2d11f05725f4fa5c0cd119b75415b95cd40d059dfc2d5",
                        BigInteger.valueOf(0L),
                        "0xcfae3217");

        TolGetGasEstimate send = web3j.tolGetGasEstimate(transaction).send();
        Assertions.assertEquals(BigInteger.valueOf(21272), send.getGasEstimate());
    }

    @Test
    @Disabled("manual test")
    public void testAccountSendRawTransaction() throws IOException {
        org.web3j.protocol.core.methods.request.Transaction transaction =
                new org.web3j.protocol.core.methods.request.Transaction(
                        "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf",
                        BigInteger.valueOf(33L),
                        BigInteger.valueOf(1L),
                        BigInteger.valueOf(6000000L),
                        "54000000000000000000000000000000000000000023199e2b",
                        BigInteger.valueOf(0L),
                        "0xcfae3217",
                        "Password1234");

        AccountSendRawTransaction response = web3j.accountSendRawTransaction(transaction).send();
        System.out.println("Transaction hash: " + response.getTransactionHash());
    }

    @Test
    @Disabled("manual test")
    public void testAccountSendDeployContractTransaction() throws IOException {
        String sender = "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf";

        TolGetNonce getNonce = web3j.tolGetNonce(sender).send();

        org.web3j.protocol.core.methods.request.Transaction transaction =
                new org.web3j.protocol.core.methods.request.Transaction(
                        sender,
                        getNonce.getNonce(),
                        BigInteger.valueOf(1L),
                        BigInteger.valueOf(6000000L),
                        "54000000000000000000000000000000000000000023199e2b",
                        BigInteger.valueOf(0L),
                        "60806040526040805190810160405280600c81526020017f48656c6c6f20576f726c642100000000000000000000000000000000000000008152506000908051906020019061004f929190610062565b5034801561005c57600080fd5b50610107565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100a357805160ff19168380011785556100d1565b828001600101855582156100d1579182015b828111156100d05782518255916020019190600101906100b5565b5b5090506100de91906100e2565b5090565b61010491905b808211156101005760008160009055506001016100e8565b5090565b90565b6102d7806101166000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063a413686214610051578063cfae3217146100ba575b600080fd5b34801561005d57600080fd5b506100b8600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061014a565b005b3480156100c657600080fd5b506100cf610164565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561010f5780820151818401526020810190506100f4565b50505050905090810190601f16801561013c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b8060009080519060200190610160929190610206565b5050565b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101fc5780601f106101d1576101008083540402835291602001916101fc565b820191906000526020600020905b8154815290600101906020018083116101df57829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061024757805160ff1916838001178555610275565b82800160010185558215610275579182015b82811115610274578251825591602001919060010190610259565b5b5090506102829190610286565b5090565b6102a891905b808211156102a457600081600090555060010161028c565b5090565b905600a165627a7a7230582005676cc36787b4b0fdaecadf2525768dca9c2503fb3892aec9082426d9aecdb70029",
                        "Password1234");

        AccountSendRawTransaction response =
                web3j.accountSendDeployContractTransaction(transaction).send();
        System.out.println("Transaction hash: " + response.getTransactionHash());
    }

    @Test
    @Disabled("manual test")
    public void testAccountSendDeployContractWithoutAmount() throws IOException {
        String sender = "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf";

        TolGetNonce getNonce = web3j.tolGetNonce(sender).send();

        org.web3j.protocol.core.methods.request.Transaction transaction =
                org.web3j.protocol.core.methods.request.Transaction.createDeployContractTransaction(
                        sender,
                        BigInteger.valueOf(6000000L),
                        BigInteger.valueOf(1L),
                        "60806040526040805190810160405280600c81526020017f48656c6c6f20576f726c642100000000000000000000000000000000000000008152506000908051906020019061004f929190610062565b5034801561005c57600080fd5b50610107565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100a357805160ff19168380011785556100d1565b828001600101855582156100d1579182015b828111156100d05782518255916020019190600101906100b5565b5b5090506100de91906100e2565b5090565b61010491905b808211156101005760008160009055506001016100e8565b5090565b90565b6102d7806101166000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063a413686214610051578063cfae3217146100ba575b600080fd5b34801561005d57600080fd5b506100b8600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061014a565b005b3480156100c657600080fd5b506100cf610164565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561010f5780820151818401526020810190506100f4565b50505050905090810190601f16801561013c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b8060009080519060200190610160929190610206565b5050565b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101fc5780601f106101d1576101008083540402835291602001916101fc565b820191906000526020600020905b8154815290600101906020018083116101df57829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061024757805160ff1916838001178555610275565b82800160010185558215610275579182015b82811115610274578251825591602001919060010190610259565b5b5090506102829190610286565b5090565b6102a891905b808211156102a457600081600090555060010161028c565b5090565b905600a165627a7a7230582005676cc36787b4b0fdaecadf2525768dca9c2503fb3892aec9082426d9aecdb70029",
                        getNonce.getNonce());

        AccountSendRawTransaction response =
                web3j.accountSendDeployContractTransaction(transaction).send();
        System.out.println("Transaction hash: " + response.getTransactionHash());
    }

    @Test
    @Disabled("manual test")
    public void testAccountSendExecuteFunction() throws IOException {
        String sender = "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf";

        TolGetNonce getNonce = web3j.tolGetNonce(sender).send();

        org.web3j.protocol.core.methods.request.Transaction transaction =
                org.web3j.protocol.core.methods.request.Transaction
                        .createExecuteFunctionTransaction(
                                sender,
                                "5422de393d7a0d716cdf1f9ea12d85f6c880b4570b17e1a7f9",
                                BigInteger.ZERO,
                                "Password123",
                                BigInteger.valueOf(6000000L),
                                BigInteger.valueOf(1L),
                                "0xa413686200000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000007426f6b626f6b2100000000000000000000000000000000000000000000000000",
                                getNonce.getNonce());

        AccountSendRawTransaction response =
                web3j.accountSendExecuteFunctionTransaction(transaction).send();
        System.out.println("Transaction hash: " + response.getTransactionHash());
    }

    @Test
    @Disabled("manual test")
    public void testAccountSendExecuteFunctionWithoutAmount() throws IOException {
        String sender = "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf";

        TolGetNonce getNonce = web3j.tolGetNonce(sender).send();

        org.web3j.protocol.core.methods.request.Transaction transaction =
                org.web3j.protocol.core.methods.request.Transaction
                        .createExecuteFunctionTransaction(
                                sender,
                                "5422de393d7a0d716cdf1f9ea12d85f6c880b4570b17e1a7f9",
                                "Password123",
                                BigInteger.valueOf(6000000L),
                                BigInteger.valueOf(1L),
                                "0xa413686200000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000007426f6b626f6b2100000000000000000000000000000000000000000000000000",
                                getNonce.getNonce());

        AccountSendRawTransaction response =
                web3j.accountSendExecuteFunctionTransaction(transaction).send();
        System.out.println("Transaction hash: " + response.getTransactionHash());
    }

    @Test
    @Disabled("manual test")
    public void testAccountSendExecuteFunctionWithoutAmountAndWithoutPassword() throws IOException {
        String sender = "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf";

        TolGetNonce getNonce = web3j.tolGetNonce(sender).send();

        org.web3j.protocol.core.methods.request.Transaction transaction =
                org.web3j.protocol.core.methods.request.Transaction
                        .createExecuteFunctionTransaction(
                                sender,
                                "5422de393d7a0d716cdf1f9ea12d85f6c880b4570b17e1a7f9",
                                BigInteger.valueOf(6000000L),
                                BigInteger.valueOf(1L),
                                "0xa413686200000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000007426f6b626f6b2100000000000000000000000000000000000000000000000000",
                                getNonce.getNonce());

        AccountSendRawTransaction response =
                web3j.accountSendExecuteFunctionTransaction(transaction).send();
        System.out.println("Transaction hash: " + response.getTransactionHash());
    }

    @Test
    @Disabled("manual test")
    public void testAccountSendFundTransferTransaction() throws IOException {
        org.web3j.protocol.core.methods.request.Transaction transaction =
                org.web3j.protocol.core.methods.request.Transaction.createFundTransferTransaction(
                        "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf",
                        "5457c2d11f05725f4fa5c0cd119b75415b95cd40d059dfc2d5",
                        BigInteger.valueOf(500000L),
                        "Password1234",
                        BigInteger.valueOf(6000000L),
                        BigInteger.valueOf(1L),
                        BigInteger.valueOf(38L));

        AccountSendRawTransaction response =
                web3j.accountSendFundTransferTransaction(transaction).send();
        System.out.println("Transaction hash: " + response.getTransactionHash());
    }

    @Test
    @Disabled("manual test")
    public void testAccountSendFundTransferTransactionWithoutPassword() throws IOException {
        org.web3j.protocol.core.methods.request.Transaction transaction =
                org.web3j.protocol.core.methods.request.Transaction.createFundTransferTransaction(
                        "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf",
                        "5457c2d11f05725f4fa5c0cd119b75415b95cd40d059dfc2d5",
                        BigInteger.valueOf(500000L),
                        BigInteger.valueOf(6000000L),
                        BigInteger.valueOf(1L),
                        BigInteger.valueOf(38L));

        AccountSendRawTransaction response =
                web3j.accountSendFundTransferTransaction(transaction).send();
        System.out.println("Transaction hash: " + response.getTransactionHash());
    }

    @Test
    @Disabled("manual test")
    public void testClientTransactionManager() throws IOException {
        ClientTransactionManager manager =
                new ClientTransactionManager(
                        web3j, "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf");

        AccountSendRawTransaction response =
                manager.sendTransaction(
                        "540dc971237be2361e04c1643d57b572709db15e449a870fef",
                        BigInteger.ZERO,
                        "Password1234",
                        BigInteger.valueOf(21000),
                        BigInteger.ONE,
                        "");

        System.out.println("Transaction hash: " + response.getTransactionHash());
    }

    @Test
    @Disabled("manual test")
    public void testRawTransactionManager() throws Exception {
        // fake private key, swap with a real one (who has some tolars)
        Credentials credentials =
                Credentials.create(
                        "34b2655334e81dbda04632aedfcc100cc45270496432d03bb9c564f66509db3d");

        SignedTransactionManager manager = new SignedTransactionManager(web3j, credentials);
        TolGetNonce nonce = web3j.tolGetNonce(credentials.getAddress()).send();

        RawTransaction transaction = RawTransaction.createTransaction(
                credentials.getAddress(),
                "54b24647dc5a34b858eae00a1977b7263c66f1af121f461ddf",
                BigInteger.valueOf(240000L),
                BigInteger.ONE,
                "test",
                nonce.getNonce()
        );

        AccountSendRawTransaction accountSendRawTransaction = manager.signAndSend(transaction);
        System.out.println(accountSendRawTransaction.getTransactionHash());

        Assertions.assertNotNull(manager);
    }

    @Test
    public void testCredentialsAddress() throws Exception {
        Credentials newPrivateKey =
                Credentials.create(
                        "34b2655334e81dbda04632aedfcc100cc45270496432d03bb9c564f66509db3d");
        Assertions.assertEquals(
                "54bf2d11fc974940f03ab8f29241877f95602633a689d13f86", newPrivateKey.getAddress());
    }

    @Test
    @Disabled("manual test")
    public void deployContractToStagingSolidity4() throws IOException {
        Web3j web3j = Web3j.build(new HttpService("https://tolar-staging.dream-factory.hr/"));
        // fake private key, swap with a real one (who has some tolars)
        Credentials credentials =
                Credentials.create(
                        "34b2655334e81dbda04632aedfcc100cc45270496432d03bb9c564f66509db3d");

        SignedTransactionManager manager = new SignedTransactionManager(web3j, credentials);
        String transactionHash =
                manager.sendTransaction(
                                "54000000000000000000000000000000000000000023199e2b",
                                BigInteger.valueOf(6000000L),
                                BigInteger.valueOf(1),
                                "60806040526040805190810160405280600c81526020017f48656c6c6f20576f726c642100000000000000000000000000000000000000008152506000908051906020019061004f929190610062565b5034801561005c57600080fd5b50610107565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100a357805160ff19168380011785556100d1565b828001600101855582156100d1579182015b828111156100d05782518255916020019190600101906100b5565b5b5090506100de91906100e2565b5090565b61010491905b808211156101005760008160009055506001016100e8565b5090565b90565b6102d7806101166000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063a413686214610051578063cfae3217146100ba575b600080fd5b34801561005d57600080fd5b506100b8600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061014a565b005b3480156100c657600080fd5b506100cf610164565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561010f5780820151818401526020810190506100f4565b50505050905090810190601f16801561013c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b8060009080519060200190610160929190610206565b5050565b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101fc5780601f106101d1576101008083540402835291602001916101fc565b820191906000526020600020905b8154815290600101906020018083116101df57829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061024757805160ff1916838001178555610275565b82800160010185558215610275579182015b82811115610274578251825591602001919060010190610259565b5b5090506102829190610286565b5090565b6102a891905b808211156102a457600081600090555060010161028c565b5090565b905600a165627a7a7230582005676cc36787b4b0fdaecadf2525768dca9c2503fb3892aec9082426d9aecdb70029")
                        .getTransactionHash();
        System.out.println(transactionHash);
    }

    @Test
    @Disabled("manual test")
    public void executeFunctionStaging() throws IOException {
        Web3j web3j = Web3j.build(new HttpService("https://tolar-staging.dream-factory.hr/"));
        // fake private key, swap with a real one (who has some tolars)
        Credentials credentials = Credentials.create("private-key");

        SignedTransactionManager manager = new SignedTransactionManager(web3j, credentials);
        String transactionHash =
                manager.sendTransaction(
                                "54f51fb1836ad0dcaee07f2c750376d11fb21474f5587ea83c",
                                BigInteger.valueOf(6000000L),
                                BigInteger.valueOf(1),
                                "0xcfae3217")
                        .getTransactionHash();
        System.out.println(transactionHash);
    }
}
