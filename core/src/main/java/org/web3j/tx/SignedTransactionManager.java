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
package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.bouncycastle.util.encoders.Base64;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.SignedTransaction;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.AccountSendRawTransaction;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.TolGetNonce;
import org.web3j.protocol.core.methods.response.TolTryCallTransaction;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Numeric;
import org.web3j.utils.SignatureData;
import org.web3j.utils.TxHashVerifier;

/**
 * TransactionManager implementation using Tolar wallet file to create and sign transactions
 * locally.
 *
 * <p>This transaction manager provides support for specifying the chain id for transactions as per
 * <a href="https://github.com/ethereum/EIPs/issues/155">EIP155</a>, as well as for locally signing
 * RawTransaction instances without broadcasting them.
 */
public class SignedTransactionManager extends TransactionManager {

    private final Web3j web3j;
    final Credentials credentials;

    private final long chainId;
    private String transactionProtobuf;

    protected TxHashVerifier txHashVerifier = new TxHashVerifier();

    public SignedTransactionManager(Web3j web3j, Credentials credentials, long chainId) {
        super(web3j, credentials.getAddress());

        this.web3j = web3j;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public SignedTransactionManager(
            Web3j web3j,
            Credentials credentials,
            long chainId,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, credentials.getAddress());

        this.web3j = web3j;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public SignedTransactionManager(
            Web3j web3j, Credentials credentials, long chainId, int attempts, long sleepDuration) {
        super(web3j, attempts, sleepDuration, credentials.getAddress());

        this.web3j = web3j;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public SignedTransactionManager(Web3j web3j, Credentials credentials) {
        this(web3j, credentials, ChainId.NONE);
    }

    public SignedTransactionManager(
            Web3j web3j, Credentials credentials, int attempts, int sleepDuration) {
        this(web3j, credentials, ChainId.NONE, attempts, sleepDuration);
    }

    protected BigInteger getNonce() throws IOException {
        TolGetNonce tolGetNonce = web3j.tolGetNonce(credentials.getAddress()).send();

        return tolGetNonce.getNonce();
    }

    public TxHashVerifier getTxHashVerifier() {
        return txHashVerifier;
    }

    public void setTxHashVerifier(TxHashVerifier txHashVerifier) {
        this.txHashVerifier = txHashVerifier;
    }

    @Override
    public AccountSendRawTransaction sendTransaction(
            String receiverAddress,
            BigInteger amount,
            BigInteger gas,
            BigInteger gasPrice,
            String data)
            throws IOException {

        BigInteger nonce = getNonce();
        RawTransaction transaction =
                RawTransaction.createTransaction(
                        getSenderAddress(), receiverAddress, amount, gas, gasPrice, data, nonce);
        return signAndSend(transaction);
    }

    @Override
    public String sendCall(String receiverAddress, String data, BigInteger gas, BigInteger gasPrice)
            throws IOException {
        TolTryCallTransaction tolTryCallTransaction =
                web3j.tolTryCallTransaction(
                                Transaction.createTryCallTransaction(
                                        getSenderAddress(), receiverAddress, gas, gasPrice, data))
                        .send();

        assertCallNotReverted(tolTryCallTransaction);
        return tolTryCallTransaction.getOutput();
    }

    @Override
    public EthGetCode getCode(
            final String contractAddress, final DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        throw new UnsupportedOperationException();
    }

    public AccountSendRawTransaction signAndSend(RawTransaction transaction) throws IOException {
        SignatureData signatureData = sign(transaction);
        return web3j.txSendSignedTransaction(new SignedTransaction(transaction, signatureData))
                .send();
    }

    public SignatureData sign(RawTransaction transaction) {
        return createSignatureData(transaction);
    }

    private String getTransactionProtobuf(RawTransaction transaction) {
        if (transactionProtobuf == null) {
            try {
                transactionProtobuf =
                        web3j.tolGetTransactionProtobuf(transaction)
                                .send()
                                .getTransactionProtobuf();
            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to create signature. Can't get transaction protobuf.", e);
            }
        }
        return transactionProtobuf;
    }

    private SignatureData createSignatureData(RawTransaction transaction) {
        String hash = createHash(transaction);
        String signature = createSignature(transaction);
        String signerId = createSignerId();

        return new SignatureData(hash, signature, signerId);
    }

    private String createHash(RawTransaction transaction) {
        return Numeric.toHexStringNoPrefix(
                Hash.sha3(Base64.decode(getTransactionProtobuf(transaction))));
    }

    private String createSignature(RawTransaction transaction) {
        byte[] hashed = Hash.sha3(Base64.decode(getTransactionProtobuf(transaction)));
        Sign.SignatureData signatureData =
                Sign.signMessage(hashed, credentials.getEcKeyPair(), false);

        byte[] concatSignatureLikeWeb3js =
                new byte
                        [signatureData.getR().length
                                + signatureData.getS().length
                                + signatureData.getV().length];

        signatureData.getV()[0] = (byte) ((int) signatureData.getV()[0] - 27);

        System.arraycopy(
                signatureData.getR(), 0, concatSignatureLikeWeb3js, 0, signatureData.getR().length);
        System.arraycopy(
                signatureData.getS(),
                0,
                concatSignatureLikeWeb3js,
                signatureData.getR().length,
                signatureData.getS().length);
        System.arraycopy(
                signatureData.getV(),
                0,
                concatSignatureLikeWeb3js,
                signatureData.getR().length + signatureData.getS().length,
                signatureData.getV().length);

        return Numeric.toHexStringNoPrefix(concatSignatureLikeWeb3js);
    }

    private String createSignerId() {
        return credentials.getEcKeyPair().getPublicKey().toString(16);
    }
}
