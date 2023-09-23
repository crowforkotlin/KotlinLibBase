package com.crow.base

import com.crow.base.ext.fromLong64
import java.nio.ByteBuffer
import kotlin.system.measureNanoTime

fun main() {

    val byteArray = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8)
    // 预热
    /*
          for (i in 1..10000) {
              val value1 = ByteBuffer.wrap(byteArray).long
              val value2 = toLongBigEndian(byteArray)
          }
    */
}