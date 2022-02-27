package miao.kmirror.danmu.network

import kotlinx.serialization.Serializable

@Serializable
data class SimpleWrap<T>(
    val data: T,
)
