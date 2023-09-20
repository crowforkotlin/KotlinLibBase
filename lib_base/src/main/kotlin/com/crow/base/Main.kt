package com.crow.base

import com.crow.base.ext.toLongBigEndian
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

    // 多次测量并取平均值
    val repeatTimes = 100000
    val time1 = (1..repeatTimes).map { measureNanoTime { ByteBuffer.wrap(byteArray).long } }.average()
    val time2 = (1..repeatTimes).map { measureNanoTime { toLongBigEndian(byteArray) } }.average()

    println("Average time 1 : $time1")
    println("Average time 2 : $time2")
}