package miao.kmirror.danmu.test

import miao.kmirror.danmu.defaultCookiesClient
import miao.kmirror.danmu.network.accountInfo
import miao.kmirror.danmu.network.applyForLoginQrcode
import miao.kmirror.danmu.network.loginInfo
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    defaultCookiesClient.login()
    println(defaultCookiesClient.accountInfo())
}

suspend fun HttpClient.login() {
    val (url, oauthKey) = applyForLoginQrcode()
    println(url)
    while (true) {
        delay(2000)
        val (success, data) = loginInfo(oauthKey)
        println(success)
        println(data)
        if (success) {
            val decode = (data as String).decodeURLQueryComponent()
            println(decode)
            get<String>(decode).also { println(it) }
            break
        }
    }
}
