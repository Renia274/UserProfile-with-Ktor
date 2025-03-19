package com.example.practice.screens.userprofile.profile.components

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.practice.logs.app.AppLogger
import com.example.practice.navigation.bottom.navItems.BottomNavItem

@Composable
fun CustomBottomBar(
    bottomNavigationItems: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    backgroundColor: Color = Color.White,
    barHeight: Dp = 64.dp,
    iconSize: Dp = 24.dp,
    modifier: Modifier
) {
    BottomNavigation(
        modifier = Modifier
            .background(backgroundColor)
            .height(barHeight)
            .fillMaxWidth()
    ) {
        bottomNavigationItems.forEachIndexed { index, item ->
            BottomNavigationItem(
                selected = index == selectedIndex,
                onClick = {
                    onItemSelected(index)
                    // Log bottom bar item selected event
                    AppLogger.logEvent("bottom_bar_item_selected", Bundle().apply {
                        putString("item_label", item.label)
                        putInt("selected_index", index)
                    })
                },
                icon = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(iconSize)
                            .fillMaxHeight()
                    ) {
                        Image(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = item.label,
                            modifier = Modifier
                                .fillMaxHeight()
                                .size(iconSize)
                        )
                    }
                },
                label = { Text(item.label) },
                modifier = Modifier
                    .weight(1f)
                    .background(backgroundColor)
            )
        }
    }
}