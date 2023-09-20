package com.crow.base.ext

import java.nio.ByteBuffer

fun getInt(byteArray: ByteArray, position: Int) = byteArray[position].toInt()

/**
 * ● 直接在原数组上进行查询处理
 *
 * ● 2023-09-13 14:53:19 周三 下午
 */
fun toIntLittleEndian(byteArray: ByteArray, startIndex: Int = 0): Int {
    return (byteArray[startIndex].toInt() and 0xFF) or
            ((byteArray[startIndex + 1].toInt() and 0xFF) shl 8) or
            ((byteArray[startIndex + 2].toInt() and 0xFF) shl 16) or
            ((byteArray[startIndex + 3].toInt() and 0xFF) shl 24)
}

/**
 * ● 直接在原数组上进行查询处理
 *
 * ● 2023-09-13 14:53:19 周三 下午
 */
fun toIntBigEndian(byteArray: ByteArray, startIndex: Int = 0): Int {
    return ((byteArray[startIndex].toInt() and 0xFF) shl 24) or
            ((byteArray[startIndex + 1].toInt() and 0xFF) shl 16) or
            ((byteArray[startIndex + 2].toInt() and 0xFF) shl 8) or
            (byteArray[startIndex + 3].toInt() and 0xFF)
}

/**
 * ● 直接在原数组上进行查询处理
 *
 * ● 2023-09-13 14:53:19 周三 下午
 */
fun toLongLittleEndian(byteArray: ByteArray, startIndex: Int = 0): Int {
    return (byteArray[startIndex].toInt() and 0xFF) or
            ((byteArray[startIndex + 1].toInt() and 0xFF) shl 8) or
            ((byteArray[startIndex + 2].toInt() and 0xFF) shl 16) or
            ((byteArray[startIndex + 3].toInt() and 0xFF) shl 24)
}

/**
 * ● 直接在原数组上进行查询处理
 *
 * ● 2023-09-13 14:53:19 周三 下午
 */
fun toLongBigEndian(byteArray: ByteArray , startIndex: Int = 0) = ByteBuffer.wrap(byteArray).getLong(startIndex)

/**
 * ● 大端序Int 构建新的ByteArray
 *
 * ● 2023-09-13 16:52:36 周三 下午
 */
fun toByteArrayBigEndian(value: Any): ByteArray {
    return when (value) {
        is Int -> {
            byteArrayOf(
                ((value shr 24) and 0xFF).toByte(),
                ((value shr 16) and 0xFF).toByte(),
                ((value shr 8) and 0xFF).toByte(),
                (value and 0xFF).toByte()
            )
        }
        is UInt -> {
            byteArrayOf(
                ((value shr 24) and 0xFFu).toByte(),
                ((value shr 16) and 0xFFu).toByte(),
                ((value shr 8) and 0xFFu).toByte(),
                (value and 0xFFu).toByte()
            )
        }
        is Long -> {
            byteArrayOf(
                ((value shr 56) and 0xFF).toByte(),
                ((value shr 48) and 0xFF).toByte(),
                ((value shr 40) and 0xFF).toByte(),
                ((value shr 32) and 0xFF).toByte(),
                ((value shr 24) and 0xFF).toByte(),
                ((value shr 16) and 0xFF).toByte(),
                ((value shr 8) and 0xFF).toByte(),
                (value and 0xFF).toByte()
            )
        }
        is ULong -> {
            byteArrayOf(
                ((value shr 56) and 0xFFu).toByte(),
                ((value shr 48) and 0xFFu).toByte(),
                ((value shr 40) and 0xFFu).toByte(),
                ((value shr 32) and 0xFFu).toByte(),
                ((value shr 24) and 0xFFu).toByte(),
                ((value shr 16) and 0xFFu).toByte(),
                ((value shr 8) and 0xFFu).toByte(),
                (value and 0xFFu).toByte()
            )
        }
        is Short -> {
            val valueInt = value.toInt()
            byteArrayOf(
                ((valueInt shr 8) and 0xFF).toByte(),
                (valueInt and 0xFF).toByte()
            )
        }
        is UShort -> {
            val valueInt = value.toUInt()
            byteArrayOf(
                ((valueInt shr 8) and 0xFFu).toByte(),
                (valueInt and 0xFFu).toByte()
            )
        }
        is Byte -> byteArrayOf(value)
        is UByte -> byteArrayOf(value.toByte())
        else -> throw IllegalStateException("type must be Int or UInt!")
    }
}

/**
 * ● 大端序Int 构建新的ByteArray
 *
 * ● 2023-09-13 16:52:36 周三 下午
 */
fun toByteArrayLittleEndian(value: Any): ByteArray {
    return when (value) {
        is Int -> {
            byteArrayOf(
                (value and 0xFF).toByte(),
                ((value shr 8) and 0xFF).toByte(),
                ((value shr 16) and 0xFF).toByte(),
                ((value shr 24) and 0xFF).toByte()
            )
        }
        is UInt -> {
            byteArrayOf(
                (value and 0xFFu).toByte(),
                ((value shr 8) and 0xFFu).toByte(),
                ((value shr 16) and 0xFFu).toByte(),
                ((value shr 24) and 0xFFu).toByte()
            )
        }
        is Long -> {
            byteArrayOf(
                (value and 0xFF).toByte(),
                ((value shr 8) and 0xFF).toByte(),
                ((value shr 16) and 0xFF).toByte(),
                ((value shr 24) and 0xFF).toByte(),
                ((value shr 32) and 0xFF).toByte(),
                ((value shr 40) and 0xFF).toByte(),
                ((value shr 48) and 0xFF).toByte(),
                ((value shr 56) and 0xFF).toByte(),
            )
        }
        is ULong -> {
            byteArrayOf(
                (value and 0xFFu).toByte(),
                ((value shr 8) and 0xFFu).toByte(),
                ((value shr 16) and 0xFFu).toByte(),
                ((value shr 24) and 0xFFu).toByte(),
                ((value shr 32) and 0xFFu).toByte(),
                ((value shr 40) and 0xFFu).toByte(),
                ((value shr 48) and 0xFFu).toByte(),
                ((value shr 56) and 0xFFu).toByte(),
            )
        }
        is Short -> {
            val valueInt = value.toInt()
            byteArrayOf(
                (valueInt and 0xFF).toByte(),
                ((valueInt shr 8) and 0xFF).toByte()
            )
        }
        is UShort -> {
            val valueInt = value.toUInt()
            byteArrayOf(
                (valueInt and 0xFFu).toByte(),
                ((valueInt shr 8) and 0xFFu).toByte()
            )
        }
        is Byte -> byteArrayOf(value)
        is UByte -> byteArrayOf(value.toByte())
        else -> throw IllegalStateException("type must be Int or UInt!")
    }
}