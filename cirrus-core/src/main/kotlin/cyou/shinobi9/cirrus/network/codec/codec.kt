package miao.kmirror.danmu.network.codec

import miao.kmirror.danmu.LOG
import miao.kmirror.danmu.handler.event.EventHandler
import miao.kmirror.danmu.handler.message.MessageHandler
import miao.kmirror.danmu.network.packet.Operation.*
import miao.kmirror.danmu.network.packet.Packet
import miao.kmirror.danmu.network.packet.Version.WS_BODY_PROTOCOL_VERSION_DEFLATE
import miao.kmirror.danmu.network.packet.Version.WS_BODY_PROTOCOL_VERSION_NORMAL
import miao.kmirror.danmu.network.packet.searchOperation
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.*
import java.util.zip.InflaterOutputStream
import kotlin.text.Charsets.UTF_8

fun uncompressZlib(input: ByteArray): ByteArray =
    ByteArrayOutputStream().use { InflaterOutputStream(it).use { output -> output.write(input) }; return@use it.toByteArray() }

fun decode(
    buffer: ByteBuffer,
    messageHandler: MessageHandler?,
    eventHandler: EventHandler?
) {
    val (mask, payload) = Packet.resolve(buffer)
    when (mask.code) {
        HEARTBEAT_REPLY -> {
            val message = payload.int
            messageHandler?.handleHeartBeat(message)
            LOG.debug { "receive heart beat packet : $message" }
        }
        AUTH_REPLY -> {
            val message = payload.array().toString(UTF_8)
            LOG.info { "auth info response => $message" }
        }
        SEND_MSG_REPLY -> {
            if (mask.version == WS_BODY_PROTOCOL_VERSION_DEFLATE) {
                decode(ByteBuffer.wrap(uncompressZlib(payload.array())), messageHandler, eventHandler)
                return
            }
            require(mask.version == WS_BODY_PROTOCOL_VERSION_NORMAL)
            val byteArray = ByteArray(mask.packLength - mask.maskLength)
            payload.get(byteArray)
            byteArray.toString(UTF_8).let { message ->
                LOG.debug { message }
                messageHandler?.handle(message)
            }
            if (payload.hasRemaining())
                decode(payload, messageHandler, eventHandler)
        }
        else -> {
            val operation = searchOperation(mask.code.code)
            if (operation == UNKNOWN)
                LOG.warn { "code unknown! => ${mask.code.code}" }
            else
                LOG.warn { "code exist in dictionary , but now haven't been handle => name : ${operation.name} , code : ${operation.code} " }
        }
    }
}
