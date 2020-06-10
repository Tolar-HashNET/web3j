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
package org.web3j.ens;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSyncing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.web3j.ens.EnsResolver.DEFAULT_SYNC_THRESHOLD;
import static org.web3j.ens.EnsResolver.isValidEnsName;

@Disabled("Not needed for HashNet")
public class EnsResolverTest {

    private Web3j web3j;
    private Web3jService web3jService;
    private EnsResolver ensResolver;

    @BeforeEach
    public void setUp() {
        web3jService = mock(Web3jService.class);
        web3j = Web3j.build(web3jService);
        ensResolver = new EnsResolver(web3j);
    }

    @Test
    public void testResolve() throws Exception {}

    @Test
    public void testReverseResolve() throws Exception {}

    @Test
    public void testIsSyncedSyncing() throws Exception {
        configureSyncing(true);

        assertFalse(ensResolver.isSynced());
    }

    @Test
    public void testIsSyncedFullySynced() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000); // block timestamp is in seconds

        assertTrue(ensResolver.isSynced());
    }

    @Test
    public void testIsSyncedBelowThreshold() throws Exception {
        configureSyncing(false);
        configureLatestBlock((System.currentTimeMillis() / 1000) - DEFAULT_SYNC_THRESHOLD);

        assertFalse(ensResolver.isSynced());
    }

    private void configureSyncing(boolean isSyncing) throws IOException {
        EthSyncing ethSyncing = new EthSyncing();
        EthSyncing.Result result = new EthSyncing.Result();
        result.setSyncing(isSyncing);
        ethSyncing.setResult(result);

        when(web3jService.send(any(Request.class), eq(EthSyncing.class))).thenReturn(ethSyncing);
    }

    private void configureLatestBlock(long timestamp) throws IOException {}

    @Test
    public void testIsEnsName() {
        assertTrue(isValidEnsName("eth"));
        assertTrue(isValidEnsName("web3.eth"));
        assertTrue(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635.eth"));

        assertFalse(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
        assertFalse(isValidEnsName("19e03255f667bdfd50a32722df860b1eeaf4d635"));

        assertTrue(isValidEnsName(""));
    }
}
