package com.example.practice.screens.userprofile.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R
import com.example.practice.ui.theme.PracticeTheme

@Composable
fun CustomVerticalGrid(
    items: List<Pair<String, Int>>,
    darkModeState: Boolean
) {
    // Add scrolling capability to handle overflow
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (darkModeState) Color.Gray else Color.White)
            .wrapContentHeight()
            // Add padding to ensure content doesn't touch bottom navigation
            .padding(bottom = 65.dp)
            // Make the grid scrollable to ensure all items are accessible
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        items.chunked(2).forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowItems.forEach { (text, imageResourceId) ->
                    CustomGridItem(
                        text = text,
                        imageResourceId = imageResourceId,
                        color = getColorForIndex(rowIndex)
                    )
                }

                // If there's an odd number of items in the last row, add an empty space
                if (rowItems.size % 2 != 0 && rowIndex == items.chunked(2).size - 1) {
                    Spacer(modifier = Modifier.width(100.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Add extra space at the bottom to ensure the last row is fully visible
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun CustomGridItem(
    text: String,
    imageResourceId: Int,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = painterResource(id = imageResourceId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = text,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

/**
 * Function to get a color based on the index.
 * @param index Index to determine the color.
 * @return Color for the specified index.
 */
fun getColorForIndex(index: Int): Color {
    val itemColors = listOf(
        Color.Blue,
        Color.Green,
        Color.Magenta,
        Color.Yellow,
        Color.Red,
        Color.Cyan,
        Color.Gray,
        Color.DarkGray,
        Color.LightGray,
        Color.Black
    )

    // Using the index to select a color from the list
    return itemColors.getOrElse(index % itemColors.size) { Color.Gray }
}

@Preview
@Composable
fun CustomVerticalGridPreview() {
    val items = listOf(
        "Item 1" to R.drawable.alice_smith,
        "Item 2" to R.drawable.alice_smith,
        "Item 3" to R.drawable.alice_smith,
        "Item 4" to R.drawable.alice_smith,
        "Item 5" to R.drawable.alice_smith,
        "Item 6" to R.drawable.alice_smith
    )

    PracticeTheme {
        CustomVerticalGrid(items = items, darkModeState = true)
    }
}