package com.example.practice.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun SecurityCodeStrengthIndicator(securityCode: String) {
    val annotatedString = calculateSecurityCodeStrength(securityCode)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = annotatedString,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

fun calculateSecurityCodeStrength(securityCode: String): AnnotatedString {
    val strength = when {
        securityCode.length >= 8 -> "Strong"
        securityCode.length >= 6 -> "Good"
        securityCode.length >= 4 -> "Weak"
        else -> "VeryWeak"
    }

    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = getStrengthColor(strength),
                fontWeight = FontWeight.Bold
            )
        ) {
            append(strength)
        }
    }
}

fun getStrengthColor(strength: String): Color {
    return when (strength) {
        "Strong" -> Color.Blue
        "Good" -> Color.Green
        "Weak" -> Color.Gray
        "VeryWeak" -> Color.LightGray
        else -> Color.Black
    }
}