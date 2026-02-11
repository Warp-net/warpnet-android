package net.warp.android.network

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import net.warp.android.data.ConnectionStatus
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

/**
 * Unit tests for NodeApiClient
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NodeApiClientTest {

    @Mock
    private lateinit var mockLibP2PClient: LibP2PClient

    private lateinit var nodeApiClient: NodeApiClient

    @Before
    fun setup() {
        nodeApiClient = NodeApiClient(mockLibP2PClient)
    }

    @Test
    fun `getFeed returns success with valid response`() = runTest {
        val mockResponse = """
            {
                "Tweets": [
                    {
                        "Id": "tweet1",
                        "Author": "user1",
                        "Content": "Test tweet 1",
                        "Timestamp": 1234567890
                    },
                    {
                        "Id": "tweet2",
                        "Author": "user2",
                        "Content": "Test tweet 2",
                        "Timestamp": 1234567891
                    }
                ]
            }
        """.trimIndent()

        `when`(mockLibP2PClient.getStatus()).thenReturn(ConnectionStatus.CONNECTED)
        `when`(mockLibP2PClient.sendMessage(any(), any())).thenReturn(Result.success(mockResponse))

        val result = nodeApiClient.getFeed(20)

        assertTrue(result.isSuccess)
        result.onSuccess { items ->
            assertEquals(2, items.size)
            assertEquals("tweet1", items[0].id)
            assertEquals("user1", items[0].author)
            assertEquals("Test tweet 1", items[0].content)
            assertEquals(1234567890L, items[0].timestamp)
        }
    }

    @Test
    fun `getFeed returns empty list with empty response`() = runTest {
        val mockResponse = """{"Tweets": []}"""

        `when`(mockLibP2PClient.sendMessage(any(), any())).thenReturn(Result.success(mockResponse))

        val result = nodeApiClient.getFeed()

        assertTrue(result.isSuccess)
        result.onSuccess { items ->
            assertTrue(items.isEmpty())
        }
    }

    @Test
    fun `getFeed returns failure when sendMessage fails`() = runTest {
        val error = Exception("Network error")
        `when`(mockLibP2PClient.sendMessage(any(), any())).thenReturn(Result.failure(error))

        val result = nodeApiClient.getFeed()

        assertTrue(result.isFailure)
        result.onFailure { e ->
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun `createPost returns success`() = runTest {
        `when`(mockLibP2PClient.sendMessage(any(), any())).thenReturn(Result.success("{}"))

        val result = nodeApiClient.createPost("Test content")

        assertTrue(result.isSuccess)
        verify(mockLibP2PClient).sendMessage(any(), any())
    }

    @Test
    fun `createPost returns failure when sendMessage fails`() = runTest {
        val error = Exception("Post failed")
        `when`(mockLibP2PClient.sendMessage(any(), any())).thenReturn(Result.failure(error))

        val result = nodeApiClient.createPost("Test content")

        assertTrue(result.isFailure)
    }

    @Test
    fun `getNotifications returns success with valid response`() = runTest {
        val mockResponse = """
            {
                "Notifications": [
                    {
                        "Id": "notif1",
                        "Type": "mention",
                        "Content": "You were mentioned",
                        "Timestamp": 1234567890
                    }
                ]
            }
        """.trimIndent()

        `when`(mockLibP2PClient.sendMessage(any(), any())).thenReturn(Result.success(mockResponse))

        val result = nodeApiClient.getNotifications()

        assertTrue(result.isSuccess)
        result.onSuccess { notifications ->
            assertEquals(1, notifications.size)
            assertEquals("notif1", notifications[0].id)
            assertEquals("mention", notifications[0].type)
        }
    }

    @Test
    fun `getMessages returns success with valid response`() = runTest {
        val mockResponse = """
            {
                "Messages": [
                    {
                        "Id": "msg1",
                        "From": "user1",
                        "Content": "Hello",
                        "Timestamp": 1234567890
                    }
                ]
            }
        """.trimIndent()

        `when`(mockLibP2PClient.sendMessage(any(), any())).thenReturn(Result.success(mockResponse))

        val result = nodeApiClient.getMessages()

        assertTrue(result.isSuccess)
        result.onSuccess { messages ->
            assertEquals(1, messages.size)
            assertEquals("msg1", messages[0].id)
            assertEquals("user1", messages[0].from)
            assertEquals("Hello", messages[0].content)
        }
    }

    @Test
    fun `parse handles malformed JSON gracefully`() = runTest {
        val mockResponse = "not valid json"

        `when`(mockLibP2PClient.sendMessage(any(), any())).thenReturn(Result.success(mockResponse))

        val result = nodeApiClient.getFeed()

        assertTrue(result.isSuccess)
        result.onSuccess { items ->
            assertTrue(items.isEmpty())
        }
    }
}
