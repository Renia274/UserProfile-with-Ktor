package com.example.practice.screens.userprofile.profile.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.practice.data.UserData

@Composable
fun UserProfileImage(userProfile: UserData, onEditClick: () -> Unit, isEditingProfession: Boolean) {
    val imageSize by animateDpAsState(
        targetValue = if (isEditingProfession) 150.dp else 200.dp,
        animationSpec = tween(durationMillis = 500),
        label = "imageSizeAnimation"
    )

    Image(
        painter = painterResource(id = userProfile.imageResId),
        contentDescription = null,
        modifier = Modifier
            .size(imageSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSecondary)
            .clickable {
                if (!isEditingProfession) {
                    onEditClick()
                }
            }
    )
}