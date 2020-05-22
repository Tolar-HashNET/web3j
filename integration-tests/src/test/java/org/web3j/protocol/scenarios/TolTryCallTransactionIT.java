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
package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.generated.Revert;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TolTryCallTransaction;
import org.web3j.tx.gas.DefaultGasProvider;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TolTryCallTransactionIT extends Scenario {

    private Revert contract;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.contract = Revert.deploy(web3j, ALICE, new DefaultGasProvider()).send();
    }

    @Test
    public void testWithoutRevert() throws Exception {
        TolTryCallTransaction tolTryCallTransaction = ethCall(BigInteger.valueOf(0L));

        assertFalse(tolTryCallTransaction.isReverted());
    }

    @Test
    public void testRevertWithoutMessage() throws Exception {
        TolTryCallTransaction tolTryCallTransaction = ethCall(BigInteger.valueOf(1L));

        assertTrue(tolTryCallTransaction.isReverted());
        assertTrue(tolTryCallTransaction.getRevertReason().endsWith("revert"));
    }

    @Test
    public void testRevertWithMessage() throws Exception {
        TolTryCallTransaction tolTryCallTransaction = ethCall(BigInteger.valueOf(2L));

        assertTrue(tolTryCallTransaction.isReverted());
        assertTrue(tolTryCallTransaction.getRevertReason().endsWith("revert The reason for revert"));
    }

    private TolTryCallTransaction ethCall(BigInteger value) throws java.io.IOException {
        final Function function =
                new Function(
                        Revert.FUNC_SET,
                        Collections.singletonList(new Uint256(value)),
                        Collections.emptyList());
        String encodedFunction = FunctionEncoder.encode(function);

        return web3j.tolTryCallTransaction(
                        Transaction.createEthCallTransaction(
                                ALICE.getAddress(), contract.getContractAddress(), encodedFunction),
                        DefaultBlockParameterName.LATEST)
                .send();
    }
}
