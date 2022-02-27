package miao.kmirror.danmu.test

import miao.kmirror.danmu.defaultClient
import miao.kmirror.danmu.network.loadBalanceWebsocketServer
import kotlinx.coroutines.runBlocking

fun main() {

    test()
}

fun test() {
    runBlocking {
        println(defaultClient.loadBalanceWebsocketServer(readLine()!!.toInt()))
        println("sad")
    }
}
