package com.example.practice.ktor.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.ktor.dto.PostRequest
import com.example.practice.ktor.dto.PostResponse
import com.example.practice.ktor.repository.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: PostsRepository) : ViewModel() {

    private val _posts = MutableLiveData<List<PostResponse>>()
    val posts: LiveData<List<PostResponse>> get() = _posts

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun fetchPosts() {
        viewModelScope.launch {
            try {
                val fetchedPosts = repository.getPosts()

                val filteredPosts = if (_searchQuery.value.isNullOrBlank()) {
                    fetchedPosts
                } else {
                    fetchedPosts.filter { post ->
                        post.title.contains(_searchQuery.value!!, ignoreCase = true) ||
                                post.body.contains(_searchQuery.value!!, ignoreCase = true)
                    }
                }

                _posts.value = filteredPosts.sortedBy { it.title }
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching posts: ${e.message}"
            }
        }
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
                    _posts.value = (_posts.value?.plus(newPost) ?: listOf(newPost))
                        .sortedBy { it.title }

                    val newIndex = _posts.value?.indexOf(newPost) ?: -1
                    listState.scrollToItem(newIndex)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error creating post: ${e.message}"
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
                        _posts.value = _posts.value?.filter { it.id != postId }
                    }
                } else {
                    val updatedResponse = repository.updatePost(postId, updatedPost!!)
                    updatedResponse?.let { updated ->
                        val index = _posts.value?.indexOfFirst { it.id == postId } ?: -1
                        if (index != -1) {
                            val updatedList = _posts.value!!.toMutableList()
                            updatedList[index] = updated
                            _posts.value = updatedList.toList()
                        }
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error updating/deleting post: ${e.message}"
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                val isDeleted = repository.deletePost(postId)
                if (isDeleted) {
                    _posts.value = _posts.value?.filter { it.id != postId }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting post: ${e.message}"
            }
        }
    }
}



