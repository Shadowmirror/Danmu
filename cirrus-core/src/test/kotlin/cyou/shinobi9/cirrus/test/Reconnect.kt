package miao.kmirror.danmu.test

import miao.kmirror.danmu.Cirrus
import miao.kmirror.danmu.handler.event.simpleEventHandler
import miao.kmirror.danmu.handler.message.simpleMessageHandler
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val room = readLine()!!.toInt()
        val cirrus = Cirrus(
            messageHandler = simpleMessageHandler {
                onReceiveDanmaku { user, said ->
                    TEST_LOG.info { "$user : $said" }
                }
                onUserEnterInLiveRoom {
                    TEST_LOG.info { "$it enter in" }
                }
            },
            eventHandler = simpleEventHandler {
                onDisconnect {
                    TEST_LOG.info { "ready to reconnect" }
                    runBlocking {
                        it.connectToBLive(room)
                    }
                }
            }
        )
        cirrus.connectToBLive(room)
    }
    Thread.currentThread().join()
}
