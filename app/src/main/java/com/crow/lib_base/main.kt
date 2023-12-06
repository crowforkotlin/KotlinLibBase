package com.crow.lib_base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

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

suspend fun main() {

    val io = CoroutineScope(Dispatchers.IO)
    val scope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
    io.launch {
        while (true) {
            running(this)
        }
    }
    delay(10000000)
}