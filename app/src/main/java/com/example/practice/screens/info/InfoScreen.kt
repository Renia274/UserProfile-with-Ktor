package com.example.practice.screens.info

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.logs.navigation.CrashlyticsNavigationLogger
import com.example.practice.logs.navigation.NavigationAnalyticsLogger
import com.example.practice.ui.theme.PracticeTheme




@Composable
fun InfoScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current

    InfoScreenContent(
        onNavigateBack = onNavigateBack,
        onOpenLink = {
            navigateToDeepLink("https://pl-coding.com", context)
            NavigationAnalyticsLogger.logAnalyticsDeepLinkEvent("https://pl-coding.com")
            CrashlyticsNavigationLogger.logCrashlyticsDeepLinkEvent("https://pl-coding.com")
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreenContent(onNavigateBack: () -> Unit, onOpenLink: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopAppBar(
            title = { Text(text = "Info/Test Screen", fontFamily = FontFamily.SansSerif) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Yellow)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Welcome to the Info Screen!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))



        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.width(32.dp))
            Text(
                "Open the link to explore and interact with the content:",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onOpenLink) {
                Text("Open Link")
            }
        }
    }
}

private fun navigateToDeepLink(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun InfoScreenContentPreview() {
    PracticeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            InfoScreenContent(
                onNavigateBack = {},
                onOpenLink = {}
            )
        }
    }
}