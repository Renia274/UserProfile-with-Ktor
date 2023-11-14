package com.example.practice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.practice.ui.theme.PracticeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageLoadingWithDrawer() {

    var selectedIndex by remember { mutableIntStateOf(0) }
    var isDrawerOpen by remember { mutableStateOf(false) }


    val imageResourceIds = arrayOf(
        R.drawable.bob_johnson, R.drawable.eve_brown, R.drawable.alice_smith
    )

    val navController = rememberNavController()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Profiles") },
            navigationIcon = {
                // IconButton to toggle the drawer state
                IconButton(onClick = {
                    isDrawerOpen = !isDrawerOpen
                }) {
                    Icon(Icons.Default.Menu, contentDescription = null)
                }
            }
        )

        // Main content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isDrawerOpen) {
                DrawerContent(
                    imageResourceIds = imageResourceIds,
                    selectedIndex = selectedIndex,
                    onItemSelected = { index ->
                        selectedIndex = index
                        isDrawerOpen = false
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            ImageLoading(
                selectedImageResId = imageResourceIds[selectedIndex],
                navController = navController
            )
        }
    }
}

@Composable
fun ImageLoading(selectedImageResId: Int, navController: NavHostController) {

    val OverrideFontPadding = PlatformTextStyle(includeFontPadding = false)
    val h6 = TextStyle(
        fontSize = 14.sp,
        platformStyle = OverrideFontPadding
    )
    val subtitle1 = TextStyle(
        fontSize = 15.sp,
        platformStyle = OverrideFontPadding
    )

    // First and last names based on the selected image
    val firstName = when (selectedImageResId) {
        R.drawable.bob_johnson -> "Bob"
        R.drawable.eve_brown -> "Eve"
        R.drawable.alice_smith -> "Alice"
        else -> "Unknown"
    }

    val lastName = when (selectedImageResId) {
        R.drawable.bob_johnson -> "Johnson"
        R.drawable.eve_brown -> "Brown"
        R.drawable.alice_smith -> "Smith"
        else -> "Unknown"
    }

    // Column to display the image and names
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = selectedImageResId),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                    // Navigate to a different screen when the image is clicked
                    navController.navigate("details/$selectedImageResId")
                }
        )

        // First Name
        Text(
            text = "First Name: $firstName",
            style = h6,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Last Name
        Text(
            text = "Last Name: $lastName",
            style = subtitle1,
            color = Color.Red,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun DrawerContent(
    imageResourceIds: Array<Int>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {

    val overrideFontPadding = PlatformTextStyle(includeFontPadding = false)
    val h6 = TextStyle(
        fontSize = 14.sp,
        platformStyle = overrideFontPadding
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Drawer header
        Text(
            text = "Person Profile",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = h6
        )

        // Drawer items
        imageResourceIds.forEachIndexed { index, imageResource ->
            DrawerItem(
                isSelected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                imageResource = imageResource
            )
        }
    }
}

@Composable
fun DrawerItem(isSelected: Boolean, onClick: () -> Unit, imageResource: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Text for the drawer item-person profile
        val name = when (imageResource) {
            R.drawable.bob_johnson -> "Bob Johnson"
            R.drawable.eve_brown -> "Eve Brown"
            R.drawable.alice_smith -> "Alice Smith"
            else -> "Unknown"
        }

        Text(text = name, color = if (isSelected) Color.Red else Color.Gray)
    }
}

@Preview
@Composable
fun ImageLoadingWithDrawerPreview() {
    PracticeTheme {
        ImageLoadingWithDrawer()
    }
}
