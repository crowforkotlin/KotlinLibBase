package com.crow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import okhttp3.internal.wait
import java.io.File
import java.io.OutputStreamWriter
import java.util.concurrent.Executors

suspend fun main() {
    println(toBinary(10).map { it })
}

private fun toBinary(decimal: Int, size: Int = 32): IntArray {
    if (decimal < 0) {
        throw IllegalArgumentException("输入的十进制数必须是非负整数！")
    }

    val binaryArray = IntArray(size) // 假设你想要32位的二进制表示

    var number = decimal
    var index = size - 1 // 从最高位开始
    while (number > 0 && index >= 0) {
        binaryArray[index] = number and 1 // 获取最低位的二进制位
        number = number ushr 1 // 右移一位
        index--
    }

    return binaryArray
}
val thread = Thread {
    println("Runnable 1")
    Thread.sleep(1000)
    println("Runnable 2")
}
private fun thre() {

    thread.run()
}
fun test() {
    val data = '蜯'.code.toByte()
    val osw = OutputStreamWriter(File("1.txt").outputStream(), Charsets.UTF_16BE)
    println('一'.code)
    osw.use {
        it.write( 19968 + 16384)
    }
}
