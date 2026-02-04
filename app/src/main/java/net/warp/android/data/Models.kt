package net.warp.android.data

/**
 * Node configuration data class
 * Contains all necessary information to connect to a WarpNet desktop node
 */
data class NodeConfig(
    val peerId: String,
    val lanAddress: String? = null,
    val remoteAddress: String? = null,
    val relayAddress: String? = null,
    val sessionToken: String,
    val psk: ByteArray? = null,
    val useRelay: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NodeConfig

        if (peerId != other.peerId) return false
        if (lanAddress != other.lanAddress) return false
        if (remoteAddress != other.remoteAddress) return false
        if (relayAddress != other.relayAddress) return false
        if (sessionToken != other.sessionToken) return false
        if (psk != null) {
            if (other.psk == null) return false
            if (!psk.contentEquals(other.psk)) return false
        } else if (other.psk != null) return false
        if (useRelay != other.useRelay) return false

        return true
    }

    override fun hashCode(): Int {
        var result = peerId.hashCode()
        result = 31 * result + (lanAddress?.hashCode() ?: 0)
        result = 31 * result + (remoteAddress?.hashCode() ?: 0)
        result = 31 * result + (relayAddress?.hashCode() ?: 0)
        result = 31 * result + sessionToken.hashCode()
        result = 31 * result + (psk?.contentHashCode() ?: 0)
        result = 31 * result + useRelay.hashCode()
        return result
    }
}

/**
 * Connection status enum
 */
enum class ConnectionStatus {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

/**
 * QR code data parsed from desktop node
 */
data class QRCodeData(
    val peerId: String,
    val addresses: List<String>,
    val sessionToken: String,
    val psk: String? = null
)
