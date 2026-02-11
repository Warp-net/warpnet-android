package net.warp.android.data

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for data models
 */
class ModelsTest {

    @Test
    fun `NodeConfig equals with same data`() {
        val config1 = NodeConfig(
            peerId = "12D3KooWTest123",
            sessionToken = "token123"
        )
        val config2 = NodeConfig(
            peerId = "12D3KooWTest123",
            sessionToken = "token123"
        )

        assertEquals(config1, config2)
        assertEquals(config1.hashCode(), config2.hashCode())
    }

    @Test
    fun `NodeConfig not equals with different peerId`() {
        val config1 = NodeConfig(
            peerId = "12D3KooWTest123",
            sessionToken = "token123"
        )
        val config2 = NodeConfig(
            peerId = "12D3KooWTest456",
            sessionToken = "token123"
        )

        assertNotEquals(config1, config2)
    }

    @Test
    fun `NodeConfig equals with same PSK`() {
        val psk = byteArrayOf(1, 2, 3, 4, 5)
        val config1 = NodeConfig(
            peerId = "12D3KooWTest123",
            sessionToken = "token123",
            psk = psk
        )
        val config2 = NodeConfig(
            peerId = "12D3KooWTest123",
            sessionToken = "token123",
            psk = psk.copyOf()
        )

        assertEquals(config1, config2)
    }

    @Test
    fun `NodeConfig not equals with different PSK`() {
        val config1 = NodeConfig(
            peerId = "12D3KooWTest123",
            sessionToken = "token123",
            psk = byteArrayOf(1, 2, 3)
        )
        val config2 = NodeConfig(
            peerId = "12D3KooWTest123",
            sessionToken = "token123",
            psk = byteArrayOf(4, 5, 6)
        )

        assertNotEquals(config1, config2)
    }

    @Test
    fun `NodeConfig uses default bootstrap nodes`() {
        val config = NodeConfig(
            peerId = "12D3KooWTest123",
            sessionToken = "token123"
        )

        assertEquals(NodeConfig.WARPNET_BOOTSTRAP_NODES, config.bootstrapNodes)
        assertTrue(config.bootstrapNodes.isNotEmpty())
    }

    @Test
    fun `NodeConfig can use custom bootstrap nodes`() {
        val customNodes = listOf("/ip4/127.0.0.1/tcp/4001/p2p/12D3KooWTest")
        val config = NodeConfig(
            peerId = "12D3KooWTest123",
            sessionToken = "token123",
            bootstrapNodes = customNodes
        )

        assertEquals(customNodes, config.bootstrapNodes)
    }

    @Test
    fun `ConnectionStatus has all required states`() {
        val states = ConnectionStatus.values()
        
        assertTrue(states.contains(ConnectionStatus.DISCONNECTED))
        assertTrue(states.contains(ConnectionStatus.CONNECTING))
        assertTrue(states.contains(ConnectionStatus.CONNECTED))
        assertTrue(states.contains(ConnectionStatus.ERROR))
    }

    @Test
    fun `QRCodeData creates instance correctly`() {
        val qrData = QRCodeData(
            peerId = "12D3KooWTest123",
            addresses = listOf("/ip4/192.168.1.1/tcp/4001"),
            sessionToken = "token123",
            psk = "pskBase64"
        )

        assertEquals("12D3KooWTest123", qrData.peerId)
        assertEquals(1, qrData.addresses.size)
        assertEquals("token123", qrData.sessionToken)
        assertEquals("pskBase64", qrData.psk)
    }

    @Test
    fun `QRCodeData works without PSK`() {
        val qrData = QRCodeData(
            peerId = "12D3KooWTest123",
            addresses = emptyList(),
            sessionToken = "token123"
        )

        assertNull(qrData.psk)
    }

    @Test
    fun `Bootstrap nodes are in correct format`() {
        NodeConfig.WARPNET_BOOTSTRAP_NODES.forEach { node ->
            assertTrue("Bootstrap node should start with /ip4/", node.startsWith("/ip4/"))
            assertTrue("Bootstrap node should contain /tcp/", node.contains("/tcp/"))
            assertTrue("Bootstrap node should contain /p2p/", node.contains("/p2p/"))
        }
    }

    @Test
    fun `Testnet bootstrap nodes are in correct format`() {
        NodeConfig.TESTNET_BOOTSTRAP_NODES.forEach { node ->
            assertTrue("Testnet node should start with /ip4/", node.startsWith("/ip4/"))
            assertTrue("Testnet node should contain /tcp/", node.contains("/tcp/"))
            assertTrue("Testnet node should contain /p2p/", node.contains("/p2p/"))
        }
    }
}
