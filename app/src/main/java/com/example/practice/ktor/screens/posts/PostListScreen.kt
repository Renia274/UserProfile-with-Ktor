package com.example.practice.ktor.screens.posts

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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


    val postState by viewModel.stateFlow.collectAsState()


    val listState = rememberLazyListState()


    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(key1 = true) {
        viewModel.fetchPosts()
    }

    // load next page when reaching the end of the list
    DisposableEffect(listState) {
        onDispose {
            // Check if the last visible item is the last post and not loading
            if (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == postState.posts.size - 1 && !postState.isLoading) {
                viewModel.loadNextPage()
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    onNavigate()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            title = { Text("Posts") },
            actions = {

                IconButton(onClick = {
                    // Toggle search bar visibility
                    isSearchVisible = !isSearchVisible
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Search bar
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
                        IconButton(onClick = {
                            // Clear search query and hide keyboard
                            searchQuery = TextFieldValue("")
                            viewModel.setSearchQuery("")
                            keyboardController?.hide()
                            isSearchVisible = false
                        }) {
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

        // Check for error message and display it if present
        if (postState.errorMessage?.isNotBlank() == true) {
            Text(text = postState.errorMessage ?: "", color = Color.Red)

        } else {
            Spacer(modifier = Modifier.height(8.dp))


            LazyColumn(
                state = listState, modifier = Modifier.fillMaxWidth()
            ) {
                items(postState.posts.filter { post ->
                    // Filter posts based on search query
                    post.title.contains(searchQuery.text, ignoreCase = true) || post.body.contains(
                        searchQuery.text,
                        ignoreCase = true
                    ) || post.title.startsWith(searchQuery.text, ignoreCase = true)
                }.distinctBy { it.id }, key = { post -> post.id }) { post ->
                    // PostItem for displaying each post
                    PostItem(post, onUpdateClick = { updatedPost, updatedTitle, updatedBody ->
                        viewModel.updatePost(
                            updatedPost.id, PostRequest(updatedBody, updatedTitle, 1)
                        )
                    }, onDeleteClick = { postId ->
                        viewModel.deletePost(postId)
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Loading indicator when posts are being fetched
        if (postState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


