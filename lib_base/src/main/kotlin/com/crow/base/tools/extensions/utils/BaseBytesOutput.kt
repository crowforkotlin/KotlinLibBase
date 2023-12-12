package com.crow.base.tools.extensions.utils

import com.crow.base.tools.extensions.Bytes
import com.crow.base.tools.extensions.fromInt16
import com.crow.base.tools.extensions.fromInt16LittleEndian
import com.crow.base.tools.extensions.fromInt32
import com.crow.base.tools.extensions.fromInt8
import java.io.ByteArrayOutputStream

class BaseBytesOutput : ByteArrayOutputStream() {

    fun writeInt8(byte: Byte) {
        this.write(byte.toInt())
    }

    fun writeInt8(int32: Int) {
        this.write(fromInt8(int32))
    }

    fun writeInt16(int32: Int) {
        val bytes: Bytes = fromInt16(int32)
        this.write(bytes, 0, bytes.size)
    }

    fun writeInt16Reversal(int32: Int) {
        val bytes: Bytes = fromInt16LittleEndian(int32)
        this.write(bytes, 0, bytes.size)
    }

    fun writeInt32(int32: Int) {
        val bytes: ByteArray = fromInt32(int32)
        this.write(bytes, 0, bytes.size)
    }

    fun writeBytes(bytes: Bytes, len: Int) {
        this.write(bytes, 0, len)
    }
}
