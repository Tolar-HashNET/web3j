package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

import java.math.BigInteger;

public class TolGetLatestBalance extends Response<TolGetBalance.BlockBalance> {

    public BigInteger getLatestBalance() {
        return getResult().getBalance();
    }

    public BigInteger getBlockIndex() {
        return getResult().getBlockIndex();
    }
}
