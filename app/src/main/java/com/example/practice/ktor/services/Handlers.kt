package com.example.practice.ktor.services

import com.example.practice.ktor.dto.ApiException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

fun HttpResponse.ensureSuccess() {
    if (!status.isSuccess()) {
        throw ApiException(status.value, status.description)
    }
}


fun handleApiError(e: Exception): Nothing {
    println("Error: ${e.message}")
    throw e
}
