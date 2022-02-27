package miao.kmirror.danmu.network.packet

import miao.kmirror.danmu.network.packet.CMD.Companion.byCommand
import miao.kmirror.danmu.network.packet.Operation.Companion.byCode
import miao.kmirror.danmu.network.packet.Version.Companion.byVersion

fun searchCMD(cmd: String, throwIfNotFound: Boolean = false) = byCommand[cmd]
    ?: if (throwIfNotFound) throw UnknownProtocolTypeException("unknown cmd : $cmd") else CMD.UNKNOWN

fun searchOperation(operation: Int, throwIfNotFound: Boolean = false) = byCode[operation]
    ?: if (throwIfNotFound) throw UnknownProtocolTypeException("unknown operation : $operation") else Operation.UNKNOWN

fun searchVersion(version: Short, throwIfNotFound: Boolean = false) = byVersion[version]
    ?: if (throwIfNotFound) throw UnknownProtocolTypeException("unknown version : $version") else Version.UNKNOWN

class UnknownProtocolTypeException(message: String) : RuntimeException(message)
