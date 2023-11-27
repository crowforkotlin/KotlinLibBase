package com.crow.lib_base

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.system.measureTimeMillis

var i = 0
@OptIn(InternalCoroutinesApi::class)
fun main() {

    var deferred = CompletableDeferred<Int>()

    fun running() {

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            deferred.complete(5)
            delay(4000)
            deferred.complete(6)
        }
    }

    running()


    runBlocking(CoroutineExceptionHandler { _, catch -> println(catch.stackTraceToString()) }) {
        val value = deferred.await()
        println(value)
        deferred.cancel()
        deferred = CompletableDeferred<Int>()
        val newValue = deferred.await()
        println(newValue)
        delay(10000000)
    }
}

suspend fun running(io: CoroutineScope) {
    repeat(Int.MAX_VALUE) {
        println(i)
        suspendCancellableCoroutine<Unit> {
            io.launch {
                delay(5000)
                i++
                it.resume(Unit)
            }
        }
    }
}