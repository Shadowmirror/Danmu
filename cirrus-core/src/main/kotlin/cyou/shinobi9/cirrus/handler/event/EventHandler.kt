package miao.kmirror.danmu.handler.event

import miao.kmirror.danmu.Cirrus


interface EventHandler {
    fun handle(eventType: EventType, cirrus: Cirrus)
}
