package com.example.practice.ktor.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    val body: String,
    val title: String,
    val userId: Int
)

@Serializable
data class PostResponse(
    var id: Int,
    var body: String,
    var title: String,
    val userId: Int,
    val isNew: Boolean = false
)

//data class for error handling
data class ApiException(val statusCode: Int, override val message: String?) : Exception(message)
