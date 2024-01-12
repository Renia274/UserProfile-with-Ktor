package com.example.practice.ktor.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.ktor.dto.PostRequest
import com.example.practice.ktor.dto.PostResponse
import com.example.practice.ktor.repository.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostsViewState(
    val posts: List<PostResponse>,
    val errorMessage: String?,
    val isLoading: Boolean,
    val searchQuery: String?
)

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: PostsRepository) : ViewModel() {

    private val state = MutableStateFlow(PostsViewState(
        posts = emptyList(),
        errorMessage = null,
        isLoading = false,
        searchQuery = null
    ))
    val stateFlow: StateFlow<PostsViewState> get() = state

    private var currentPage = 1
    private val pageSize = 10

    fun setSearchQuery(query: String) {
        state.value = state.value.copy(searchQuery = query)
    }

    fun fetchPosts() {
        viewModelScope.launch {
            try {
                state.value = state.value.copy(isLoading = true)

                delay(2000)

                val fetchedPosts = repository.getPosts(currentPage, pageSize)

                val filteredPosts = if (state.value.searchQuery.isNullOrBlank()) {
                    fetchedPosts
                } else {
                    fetchedPosts.filter { post ->
                        post.title.contains(state.value.searchQuery!!, ignoreCase = true) ||
                                post.body.contains(state.value.searchQuery!!, ignoreCase = true)
                    }
                }

                state.value = state.value.copy(
                    posts = (state.value.posts.plus(filteredPosts))
                        .distinctBy { it.id }
                        .sortedBy { it.title },
                    isLoading = false
                )

            } catch (e: Exception) {
                state.value = state.value.copy(isLoading = false, errorMessage = "Error fetching posts: ${e.message}")
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        fetchPosts()
    }

    fun createPosts(listState: LazyListState) {
        viewModelScope.launch {
            try {
                val initialTitle = "Initial Title"
                val initialBody = "Initial Body"
                val timestamp = System.currentTimeMillis()
                val newPostRequest = createSamplePostRequest(initialBody, initialTitle, userId = 1)
                val newPost = repository.createPost(newPostRequest)

                if (newPost != null) {
                    newPost.id = timestamp.toInt()
                    println("New post key: ${newPost.id}")
                    state.value = state.value.copy(
                        posts = (state.value.posts.plus(newPost))
                            .sortedBy { it.title }
                    )

                    val newIndex = state.value.posts.indexOf(newPost)
                    listState.scrollToItem(newIndex)
                }
            } catch (e: Exception) {
                state.value = state.value.copy(errorMessage = "Error creating post: ${e.message}")
            }
        }
    }

    private fun createSamplePostRequest(body: String, title: String, userId: Int): PostRequest {
        return PostRequest(body = body, title = title, userId = userId)
    }

    fun updatePost(postId: Int, updatedPost: PostRequest? = null, isDelete: Boolean = false) {
        viewModelScope.launch {
            try {
                if (isDelete) {
                    val isDeleted = repository.deletePost(postId)
                    if (isDeleted) {
                        state.value = state.value.copy(
                            posts = state.value.posts.filter { it.id != postId }
                        )
                    }
                } else {
                    val updatedResponse = repository.updatePost(postId, updatedPost!!)
                    updatedResponse?.let { updated ->
                        val index = state.value.posts.indexOfFirst { it.id == postId }
                        if (index != -1) {
                            val updatedList = state.value.posts.toMutableList()
                            updatedList[index] = updated
                            state.value = state.value.copy(posts = updatedList.toList())
                        }
                    }
                }
            } catch (e: Exception) {
                state.value = state.value.copy(errorMessage = "Error updating/deleting post: ${e.message}")
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                val isDeleted = repository.deletePost(postId)
                if (isDeleted) {
                    state.value = state.value.copy(
                        posts = state.value.posts.filter { it.id != postId }
                    )
                }
            } catch (e: Exception) {
                state.value = state.value.copy(errorMessage = "Error deleting post: ${e.message}")
            }
        }
    }
}
