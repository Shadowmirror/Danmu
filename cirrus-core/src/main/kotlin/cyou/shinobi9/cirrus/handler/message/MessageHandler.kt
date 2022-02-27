package miao.kmirror.danmu.handler.message

@MessageHandlerDsl
interface MessageHandler {
    fun handle(message: String)
    fun handleHeartBeat(number: Int)
}
