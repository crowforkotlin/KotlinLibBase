package com.crow.lib_base

import com.crow.base.tools.extensions.toInt32
import com.crow.base.tools.extensions.toUInt32
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.Date

var job: Job? = null
private suspend fun running(scope: CoroutineScope) {
    val a: Job? = null
        repeat(2) {
            repeat(1) {
                println("RUNNING $it")
                scope.launch {
                    delay(3000)
                    println("CANCEL")
                    job!!.cancel()
                }
                job = scope.launch {
                    println("WAIT")
                    delay(Long.MAX_VALUE)
                }
                println("JOIN")
                job!!.join()
                println("UNIT")
                println("--------------------")
            }
        }
}

@OptIn(ExperimentalStdlibApi::class)
suspend fun main() {

    println((0x02 - 0xE7).toByte())
    return
    NTPClient.connect("120.25.115.20", 123)
    val value = toUInt32(ubyteArrayOf(0xE9.toUByte(),0x1B.toUByte(),0xB6.toUByte(),0x8C.toUByte()))
    val value2 = value - 2208988800L
    println(value)
    println(value2)
    println(value2 * 1000)
    println(Date(value2))
    println(Date(value2 * 1000))
    repeat(10) {
        val time = NTPClient.getTime()
        println(Date(time))
    }
    delay(10000000)
}

object NTPClient {

    private var mSocket: DatagramSocket? = null
    private var mIP: InetAddress? = null
    private var mPort: Int? = null
    private val mIO = CoroutineScope(Dispatchers.IO)

    fun connect(ip: String, port: Int) {
        mSocket?.close()
        mSocket = DatagramSocket()
        mIP = InetAddress.getByName(ip)
        mPort = port
    }

    suspend fun sendData(data: ByteArray) {
        if (mIP == null || mPort == null || mSocket == null) return
        mIO.launch { mSocket!!.send(DatagramPacket(data, data.size, mIP, mPort!!)) }.join()
    }

    suspend fun readData(data: ByteArray) {
        if (mIP == null && mPort == null && mSocket == null) return
        mIO.launch { mSocket!!.receive(DatagramPacket(data, data.size)) }.join()
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun getTime(): Long {
        if (mIP == null && mPort == null && mSocket == null) return 0L
        val bytes = ByteArray(48)
        sendData(byteArrayOf(0x1b,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0))
        readData(bytes)
        val ntpTimestamp = toInt32(bytes, 40)
        println(ntpTimestamp)
        println(bytes.map { it.toHexString() })
        val unixTimestamp = ntpTimestamp - 2208988800L
        return unixTimestamp * 1000
    }
}
