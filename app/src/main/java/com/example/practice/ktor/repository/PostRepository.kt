package com.example.practice.ktor.repository

import com.example.practice.ktor.dto.PostRequest
import com.example.practice.ktor.dto.PostResponse
import com.example.practice.ktor.services.PostsService
import javax.inject.Inject


class PostsRepository @Inject constructor (private val postsService: PostsService) {

    suspend fun getPosts(page: Int, pageSize: Int): List<PostResponse> {
        return postsService.getPosts(page, pageSize)
    }


    suspend fun createPost(postRequest: PostRequest): PostResponse? {
        return postsService.createPost(postRequest)
    }

    suspend fun updatePost(postId: Int, updatedPost: PostRequest): PostResponse? {
        return postsService.updatePost(postId, updatedPost)
    }

    suspend fun deletePost(postId: Int): Boolean {
        return try {
            postsService.deletePost(postId)
            true
        } catch (e: Exception) {
            false
        }
    }

}
