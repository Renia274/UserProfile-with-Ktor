package com.example.practice.screens.items

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomVerticalGrid(
    items: List<String>
) {
    var selectedItem by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
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
                        description = "Description for $item",
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
            .height(80.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondary
                else color
            )
            .clickable(onClick = onClick)
    ) {
        Column(
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isSelected && description.isNotEmpty()) {
                Text(
                    text = description,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

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
