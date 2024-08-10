package com.example.practice.screens.userprofile.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R
import com.example.practice.ui.theme.PracticeTheme

@Composable
fun CustomVerticalGrid(
    items: List<Pair<String, Int>>,  // List of pairs (text, image resource ID)
    darkModeState: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (darkModeState) Color.Gray else Color.White)
            .wrapContentHeight()
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        items.chunked(2).forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.width(16.dp))

                rowItems.forEach { (text, imageResourceId) ->
                    CustomGridItem(
                        text = text,
                        imageResourceId = imageResourceId,  // Pass the image resource ID
                        color = getColorForIndex(rowIndex)
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CustomGridItem(
    text: String,
    imageResourceId: Int,  // Add this parameter for image resource ID
    color: Color
) {
    Box(
        modifier = Modifier
            .width(100.dp)  // Adjust width as needed
            .height(100.dp) // Adjust height to accommodate image
            .background(color)
    ) {
        Column {
            Image(
                painter = painterResource(id = imageResourceId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()  // Fill the entire space
                    .background(color)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = text,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(color)
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
        "Item 1" to R.drawable.bob_johnson,
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
