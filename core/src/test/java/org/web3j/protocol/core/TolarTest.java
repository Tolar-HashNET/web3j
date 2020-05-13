package org.web3j.protocol.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.NetPeerCount;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;

class TolarTest {
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
}