package com.example.practice.screens.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                rowItems.forEach { item ->



                    CustomGridItem(
                        text = item,
                        isSelected = selectedItem == item,
                        onClick = {
                            selectedItem = if (selectedItem == item) null else item
                        },
                        description = "Description for $item"
                    )
                }
            }
        }


        selectedItem?.let { selectedItem ->
            Text(
                text = "Description: $selectedItem",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}



@Composable
fun CustomGridItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    description: String
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.primary
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )


            Spacer(modifier = Modifier.height(8.dp))

            if (isSelected && description.isNotEmpty()) {
                Text(
                    text = description,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}
