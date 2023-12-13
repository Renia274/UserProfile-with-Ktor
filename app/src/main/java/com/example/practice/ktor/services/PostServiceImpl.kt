package com.example.practice.ktor.services

import com.example.practice.ktor.dto.PostRequest
import com.example.practice.ktor.dto.PostResponse
import com.example.practice.ktor.routes.HttpRoutes
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

@ViewModelScoped
class PostsServiceImpl @Inject constructor(
    private val client: HttpClient
) : PostsService {

    override suspend fun getPosts(): List<PostResponse> {
        return try {
            val response = client.get<HttpResponse> {
                url(HttpRoutes.POSTS)
            }
            response.ensureSuccess()
            response.receive()
        } catch (e: Exception) {
            handleApiError(e)
        }
    }


    override suspend fun createPost(postRequest: PostRequest): PostResponse? {
        return try {
            val response = client.post<HttpResponse> {
                url(HttpRoutes.POSTS)
                contentType(ContentType.Application.Json)
                body = postRequest
            }
            response.ensureSuccess()
            response.receive<PostResponse>()
        } catch (e: Exception) {
            handleApiError(e)
        }
    }


    override suspend fun updatePost(postId: Int, updatedPost: PostRequest): PostResponse? {
        return try {
            val response = client.put<HttpResponse> {
                url("${HttpRoutes.POSTS}/$postId")
                contentType(ContentType.Application.Json)
                body = updatedPost
            }
            response.ensureSuccess()
            response.receive<PostResponse>()
        } catch (e: Exception) {
            handleApiError(e)
        }
    }


    override suspend fun deletePost(postId: Int) {
        try {
            client.delete<HttpResponse> {
                url("${HttpRoutes.POSTS}/$postId")
            }.ensureSuccess()
        } catch (e: Exception) {
            handleApiError(e)
        }
    }
}