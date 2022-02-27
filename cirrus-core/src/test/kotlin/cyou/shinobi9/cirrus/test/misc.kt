package miao.kmirror.danmu.test

import miao.kmirror.danmu.Cirrus
import miao.kmirror.danmu.handler.message.simpleMessageHandler
import mu.KotlinLogging

val TEST_LOG = KotlinLogging.logger {}

val TEST_MESSAGE_HANDLER = simpleMessageHandler {
    onReceiveDanmaku { user, said ->
        TEST_LOG.info { "$user : $said" }
    }
    onUserEnterInLiveRoom {
        TEST_LOG.info { "$it enter in" }
    }
}

val TEST_CIRRUS = Cirrus().apply { messageHandler = TEST_MESSAGE_HANDLER }
