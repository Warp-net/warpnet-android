package net.warp.android.util

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit tests for QRCodeParser
 */
@RunWith(MockitoJUnitRunner::class)
class QRCodeParserTest {

    @Test
    fun `parse valid QR code with lowercase fields`() {
        val qrContent = """
            {
                "peerId": "12D3KooWTestPeerID123",
                "sessionToken": "testToken123"
            }
        """.trimIndent()

        val result = QRCodeParser.parse(qrContent)

        assertTrue(result.isSuccess)
        result.onSuccess { data ->
            assertEquals("12D3KooWTestPeerID123", data.peerId)
            assertEquals("testToken123", data.sessionToken)
            assertNull(data.psk)
        }
    }

    @Test
    fun `parse valid QR code with uppercase fields`() {
        val qrContent = """
            {
                "PeerId": "12D3KooWTestPeerID456",
                "SessionToken": "testToken456"
            }
        """.trimIndent()

        val result = QRCodeParser.parse(qrContent)

        assertTrue(result.isSuccess)
        result.onSuccess { data ->
            assertEquals("12D3KooWTestPeerID456", data.peerId)
            assertEquals("testToken456", data.sessionToken)
        }
    }

    @Test
    fun `parse QR code with PSK`() {
        val qrContent = """
            {
                "peerId": "12D3KooWTestPeerID789",
                "sessionToken": "testToken789",
                "psk": "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXoxMjM0NTY="
            }
        """.trimIndent()

        val result = QRCodeParser.parse(qrContent)

        assertTrue(result.isSuccess)
        result.onSuccess { data ->
            assertEquals("12D3KooWTestPeerID789", data.peerId)
            assertEquals("testToken789", data.sessionToken)
            assertEquals("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXoxMjM0NTY=", data.psk)
        }
    }

    @Test
    fun `parse invalid QR code missing peerId`() {
        val qrContent = """
            {
                "sessionToken": "testToken"
            }
        """.trimIndent()

        val result = QRCodeParser.parse(qrContent)

        assertTrue(result.isFailure)
        result.onFailure { error ->
            assertTrue(error.message?.contains("Missing peerId") == true)
        }
    }

    @Test
    fun `parse invalid QR code missing sessionToken`() {
        val qrContent = """
            {
                "peerId": "12D3KooWTestPeerID"
            }
        """.trimIndent()

        val result = QRCodeParser.parse(qrContent)

        assertTrue(result.isFailure)
        result.onFailure { error ->
            assertTrue(error.message?.contains("Missing sessionToken") == true)
        }
    }

    @Test
    fun `parse invalid JSON`() {
        val qrContent = "not valid json"

        val result = QRCodeParser.parse(qrContent)

        assertTrue(result.isFailure)
        result.onFailure { error ->
            assertTrue(error.message?.contains("Invalid QR code format") == true)
        }
    }

    @Test
    fun `parse empty string`() {
        val qrContent = ""

        val result = QRCodeParser.parse(qrContent)

        assertTrue(result.isFailure)
    }
}
