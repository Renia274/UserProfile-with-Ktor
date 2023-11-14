package com.example.practice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice.ui.theme.PracticeTheme
import kotlinx.coroutines.launch


@Composable
fun ImageLoading(selectedImageResId: Int) {
    // State to track whether the image is currently loading
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    // Array of image resource IDs for selection
    val imageResourceIds = arrayOf(
        R.drawable.bob_johnson, R.drawable.eve_brown, R.drawable.alice_smith
    )
    // Currently selected image resource ID
    var selectedIndex by remember { mutableIntStateOf(selectedImageResId) }
    var isZoomed by remember { mutableStateOf(false) }
    // State to manage the scale factor for zooming
    var scale by remember { mutableFloatStateOf(1f) }
    // State to manage the offset for panning when zoomed
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    val professions = listOf("Engineer", "Teacher", "Doctor", "Artist")
    var generatedProfession by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Row with left arrow, image, and right arrow
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left arrow
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .weight(1f)
                        .clickable {
                            // Navigate to the previous image in the array
                            val prevIndex =
                                (imageResourceIds.indexOf(selectedIndex) - 1 + imageResourceIds.size) % imageResourceIds.size
                            coroutineScope.launch {
                                isLoading = true
                                selectedIndex = imageResourceIds[prevIndex]
                                isLoading = false
                            }
                        }
                )

                // Image with zoom and pan functionality
                Image(
                    painter = painterResource(id = selectedIndex),
                    contentDescription = null,
                    contentScale = if (isZoomed) androidx.compose.ui.layout.ContentScale.Crop else androidx.compose.ui.layout.ContentScale.FillBounds,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .pointerInput(Unit) {
                            // Gesture detection for zoom and pan
                            detectTransformGestures { _, pan, zoom, _ ->
                                // Update scale and offset based on zoom and pan gestures
                                scale *= zoom
                                offset = if (isZoomed) {
                                    Offset(offset.x + pan.x * scale, offset.y + pan.y * scale)
                                } else {
                                    Offset(0f, 0f)
                                }
                            }
                        }
                        .graphicsLayer(
                            // Apply transformations based on scale and offset
                            scaleX = maxOf(1f, minOf(scale, 3f)),
                            scaleY = maxOf(1f, minOf(scale, 3f)),
                            translationX = with(LocalDensity.current) { offset.x.toDp().value },
                            translationY = with(LocalDensity.current) { offset.y.toDp().value }
                        )
                        .clickable {
                            // Toggle zoom state and reset scale and offset when zoomed out
                            isZoomed = !isZoomed
                            if (!isZoomed) {
                                scale = 1f
                                offset = Offset(0f, 0f)
                            }
                        }
                )

                // Right arrow
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .weight(1f)
                        .clickable {
                            // Navigate to the next image in the array
                            val nextIndex =
                                (imageResourceIds.indexOf(selectedIndex) + 1) % imageResourceIds.size
                            coroutineScope.launch {
                                isLoading = true
                                selectedIndex = imageResourceIds[nextIndex]
                                isLoading = false
                            }
                        }
                )
            }

            // Loading indicator
            if (isLoading) {
                BoxWithConstraints(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }

            // Button to generate a random profession
            ProfessionButton(professions = professions) {
                generatedProfession = it
            }

            // Display the generated profession
            if (generatedProfession.isNotEmpty()) {
                Text(
                    text = "Profession: $generatedProfession",
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ProfessionButton(professions: List<String>, onClick: (String) -> Unit) {
    // State to manage focus state of the button
    var isFocused by remember { mutableStateOf(false) }

    Button(
        onClick = {
            // Randomly select a profession and invoke the onClick callback
            val generatedProfession = professions.random()
            onClick(generatedProfession)
            // Set focus to true after the button is clicked
            isFocused = true
        },
        modifier = Modifier
            .padding(16.dp)
            .focusTarget()
            .focusable(interactionSource = remember { MutableInteractionSource() })
            .onFocusChanged {
                // Update the focus state when the button is clicked
                isFocused = it.isFocused
            }
    ) {
        Text(
            text = "Generate Profession",
            // Change the color based on focus
            color = if (isFocused) Color.White else Color.Red
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ImageLoadingPreview() {
    PracticeTheme {
        ImageLoading(selectedImageResId = R.drawable.bob_johnson)
    }
}