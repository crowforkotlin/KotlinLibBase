package com.crow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis


suspend fun main() {
    val flow = MutableStateFlow(0)
    val IO = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
    var startTime = 0L
    val job = IO.launch {
        delay(10000)
    }
    job.invokeOnCompletion {
        println((System.currentTimeMillis() - startTime))
    }

    println(measureTimeMillis {
        IO.launch {
                flow.collect {
                    println((System.currentTimeMillis() - startTime))
                }
        }
    })
    IO.launch {
        startTime = System.currentTimeMillis()
        flow.emit(1)
    }
    delay(10000000)
}
