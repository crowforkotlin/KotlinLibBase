package com.crow.base.ext

typealias Bytes = ByteArray

/**
 * ● 大端序Bytes 转 Int32
 *
 * ● 2023-09-13 14:53:19 周三 下午
 */
fun toInt32(bytes: Bytes, startIndex: Int = 0, isLittleEndian: Boolean = false): Int {
    return((bytes[startIndex].toInt() and 0xFF) shl 24) or
        ((bytes[startIndex + 1].toInt() and 0xFF) shl 16) or
        ((bytes[startIndex + 2].toInt() and 0xFF) shl 8) or
        (bytes[startIndex + 3].toInt() and 0xFF)
}

/**
 * ● 小端序Bytes 转 Int32
 *
 * ● 2023-09-23 15:07:14 周六 下午
 */
fun toInt32LittleEndian(bytes: Bytes, startIndex: Int = 0): Int {
    return (bytes[startIndex].toInt() and 0xFF) or
            ((bytes[startIndex + 1].toInt() and 0xFF) shl 8) or
            ((bytes[startIndex + 2].toInt() and 0xFF) shl 16) or
            ((bytes[startIndex + 3].toInt() and 0xFF) shl 24)
}

/**
 * ● Int16转 Bytes -- 大端序
 *
 * ● 2023-09-23 15:06:59 周六 下午
 */
fun fromInt16(int32: Int) = byteArrayOf(((int32 shr 8) and 0xFF).toByte(), (int32 and 0xFF).toByte())

/**
 * ● Int16 转 Bytes -- 小端序
 *
 * ● 2023-09-23 15:06:36 周六 下午
 */
fun fromInt16LittleEndian(int32: Int) = byteArrayOf((int32 and 0xFF).toByte(), ((int32 shr 8) and 0xFF).toByte())

/**
 * ● Int32 转 Bytes -- 大端序
 *
 * ● 2023-09-23 15:06:17 周六 下午
 */
fun fromInt32(int32: Int) = byteArrayOf(
    ((int32 shr 24) and 0xFF).toByte(),
    ((int32 shr 16) and 0xFF).toByte(),
    ((int32 shr 8) and 0xFF).toByte(),
    (int32 and 0xFF).toByte()
)

/**
 * ● Int32 转 Bytes -- 小端序
 *
 * ● 2023-09-23 15:05:49 周六 下午
 */
fun fromInt32LitterEndian(int32: Int) = byteArrayOf(
    (int32 and 0xFF).toByte(),
    ((int32 shr 8) and 0xFF).toByte(),
    ((int32 shr 16) and 0xFF).toByte(),
    ((int32 shr 24) and 0xFF).toByte()
)

/**
 * ● 大端序Int 构建新的ByteArray
 *
 * ● 2023-09-13 16:52:36 周三 下午
 */
@Deprecated("It may be removed in the future.")
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
@Deprecated("It may be removed in the future.")
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