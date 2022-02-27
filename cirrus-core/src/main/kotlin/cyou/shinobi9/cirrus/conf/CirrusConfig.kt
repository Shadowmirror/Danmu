package miao.kmirror.danmu.conf

data class CirrusConfig(
    var threadsCount: Int = 10,
    var useDispatchersIO: Boolean = false,
    var reconnect: Boolean = true,
)
