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
package org.web3j.protocol.core.methods.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bouncycastle.util.encoders.Base64;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;
import org.web3j.utils.SignatureData;

public class SignedTransaction {

    @JsonProperty("body")
    private Transaction transaction;

    @JsonProperty("sig_data")
    private SignatureData signatureData;

    @JsonIgnore private String transactionProtobuf;

    @JsonIgnore private Credentials credentials;

    public SignedTransaction(Transaction transaction, Credentials credentials) {
        this.transaction = transaction;
        this.credentials = credentials;
        this.signatureData = createSignatureData();
    }

    private String getTransactionProtobuf() {
        if (transactionProtobuf == null) {
            try {
                transactionProtobuf =
                        Web3j.build(new HttpService("https://tolar.dream-factory.hr/"))
                                .tolGetTransactionProtobuf(transaction)
                                .send()
                                .getTransactionProtobuf();
            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to create signature. Can't get transaction protobuf.", e);
            }
        }
        return transactionProtobuf;
    }

    private String createHash() {
        return Numeric.toHexStringNoPrefix(Hash.sha3(Base64.decode(getTransactionProtobuf())));
    }

    private String createSignature() {
        byte[] hashed = Hash.sha3(Base64.decode(getTransactionProtobuf()));
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

    private SignatureData createSignatureData() {
        String hash = createHash();
        String signature = createSignature();
        String signerId = createSignerId();

        return new SignatureData(hash, signature, signerId);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public SignatureData getSignatureData() {
        return signatureData;
    }
}
