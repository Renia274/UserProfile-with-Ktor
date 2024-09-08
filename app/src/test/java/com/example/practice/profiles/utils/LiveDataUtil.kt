package com.example.practice.profiles.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> LiveData<T>.getOrAwaitValue(): T {
    var data: T? = null
    val latch = CountDownLatch(1)

    // Create an observer that will be used to capture LiveData changes
    val observer = Observer<T> { value ->
        data = value
        latch.countDown()
    }

    // Observe LiveData forever to ensure we get the value
    this.observeForever(observer)

    // Wait for the LiveData value to be set or timeout after 2 seconds
    latch.await(2, TimeUnit.SECONDS)

    // Return the value or throw an exception if no value was set
    return data ?: throw IllegalStateException("LiveData value was null")
}
