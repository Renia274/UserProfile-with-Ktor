package com.example.practice.screens.userprofile.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.ui.theme.PracticeTheme

@Composable
fun CustomVerticalGrid(
    items: List<String>,
    darkModeState: Boolean
) {
    // State to track the selected item
    var selectedItem by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (darkModeState) Color.Gray else Color.White) // Use darkModeState for background
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

                rowItems.forEach { item ->
                    CustomGridItem(
                        text = item,
                        isSelected = selectedItem == item,
                        onClick = {
                            selectedItem = if (selectedItem == item) null else item
                        },
                        description = "Info: $item",
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
    isSelected: Boolean,
    onClick: () -> Unit,
    description: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(60.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondary
                else color
            )
            .clickable(onClick = onClick)
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description for the selected item
            if (isSelected && description.isNotEmpty()) {
                DescriptionText(description)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DescriptionText(description: String) {
    Text(
        text = description,
        color = Color.Black,
        fontSize = 14.sp
    )
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
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6")

    PracticeTheme {
        CustomVerticalGrid(items = items,darkModeState = true)
    }
}