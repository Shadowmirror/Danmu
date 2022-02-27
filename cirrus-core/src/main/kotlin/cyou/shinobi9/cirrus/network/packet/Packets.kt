package miao.kmirror.danmu.network.packet

import miao.kmirror.danmu.json
import miao.kmirror.danmu.network.packet.Operation.AUTH
import miao.kmirror.danmu.network.packet.Operation.HEARTBEAT
import miao.kmirror.danmu.network.packet.Version.WS_BODY_PROTOCOL_VERSION_INT
import kotlinx.serialization.encodeToString
import java.nio.ByteBuffer

object Packets {
    private val heartBeatContent = "[object Object]".toByteArray()
    val heartBeat = Packet.createPacket(
        PacketMask(WS_BODY_PROTOCOL_VERSION_INT, HEARTBEAT),
        ByteBuffer.wrap(heartBeatContent)
    )
    val auth = fun(roomId: Int, token: String) = Packet.createPacket(
        PacketMask(WS_BODY_PROTOCOL_VERSION_INT, AUTH),
        ByteBuffer.wrap(json.encodeToString(AuthInfo(roomId = roomId, key = token)).toByteArray())
    )
}
