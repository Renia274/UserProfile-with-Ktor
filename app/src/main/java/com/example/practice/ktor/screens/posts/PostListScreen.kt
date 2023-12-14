package com.example.practice.ktor.screens.posts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.ktor.dto.PostRequest
import com.example.practice.ktor.screens.items.PostItem
import com.example.practice.ktor.viewmodel.PostsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PostsScreen(
    onNavigate: () -> Unit,
    viewModel: PostsViewModel = hiltViewModel()
) {
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
    val posts by viewModel.posts.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState("")

    val listState = rememberLazyListState()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        viewModel.fetchPosts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(
                    onClick = {
                        onNavigate()
                    }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            title = { Text("Posts") },
            actions = {
                // Search icon to toggle search bar visibility
                IconButton(
                    onClick = {
                        // Toggle search bar visibility
                        isSearchVisible = !isSearchVisible
                    }
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (isSearchVisible) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.setSearchQuery(it.text)
                },
                label = { Text("Search") },
                trailingIcon = {
                    if (searchQuery.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                // Clear search query and hide keyboard
                                searchQuery = TextFieldValue("")
                                viewModel.setSearchQuery("")
                                keyboardController?.hide()
                                isSearchVisible = false
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { viewModel.createPosts(listState) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Add New Posts")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotBlank()) {
            Text(text = errorMessage, color = Color.Red)
        } else {
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(posts.filter { post ->
                    post.title.contains(searchQuery.text, ignoreCase = true) ||
                            post.body.contains(searchQuery.text, ignoreCase = true) ||
                            post.title.startsWith(searchQuery.text, ignoreCase = true)
                }.distinctBy { it.id }, key = { post -> post.id }) { post ->
                    PostItem(
                        post,
                        onUpdateClick = { updatedPost, updatedTitle, updatedBody ->
                            viewModel.updatePost(
                                updatedPost.id,
                                PostRequest(updatedBody, updatedTitle, 1)
                            )
                        },
                        onDeleteClick = { postId ->
                            viewModel.deletePost(postId)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }
    }
}