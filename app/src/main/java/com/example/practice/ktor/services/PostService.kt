package com.example.practice.ktor.services

import com.example.practice.ktor.dto.PostRequest
import com.example.practice.ktor.dto.PostResponse


interface PostsService {

    suspend fun getPosts(): List<PostResponse>

    suspend fun createPost(postRequest: PostRequest): PostResponse?

    suspend fun updatePost(postId: Int, updatedPost: PostRequest): PostResponse?

    suspend fun deletePost(postId: Int)

}