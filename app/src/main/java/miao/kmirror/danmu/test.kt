package miao.kmirror.danmu

import miao.kmirror.danmu.handler.message.rawMessageHandler
import miao.kmirror.danmu.handler.message.simpleMessageHandler
import mu.KotlinLogging

fun main() {
    println("raw ?(y/n)")
    if (readLine()!! == "y") rawMessage() else simpleMessage()
}

val TEST_LOG = KotlinLogging.logger {}

fun simpleMessage() {
    val cirrus = Cirrus(
        messageHandler = simpleMessageHandler {
            onReceiveDanmaku { user, said ->
                TEST_LOG.info { "$user : $said" }
            }
            onUserEnterInLiveRoom {
                TEST_LOG.info { "$it enter in" }
            }
        }
    )
    cirrus.connectToBLive(readLine()!!.toInt())
    Thread.currentThread().join()
}

fun rawMessage() {
    val cirrus = Cirrus(
        messageHandler = rawMessageHandler {
            onMessage {
                println(it)
            }
        }
    )
    cirrus.connectToBLive(readLine()!!.toInt())
    Thread.currentThread().join()
}