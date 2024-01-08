package com.example.practice.screens.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R

@Composable
fun SettingsField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditable: Boolean,
    onClearClick: () -> Unit,
    initiallyVisible: Boolean = false
) {
    var isVisible by remember { mutableStateOf(initiallyVisible) }
    val displayedValue by remember { mutableStateOf(value) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = label,
            style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
        )
        if (isEditable) {
            val editableState = remember { mutableStateOf(displayedValue) }
            OutlinedTextField(
                value = editableState.value,
                onValueChange = {
                    editableState.value = it
                    onValueChange(it)
                },
                label = { Text(label) },
                visualTransformation =
                if (label.equals("Password:", ignoreCase = true) || label.equals(
                        "Security Code:",
                        ignoreCase = true
                    )
                )
                    if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
                else VisualTransformation.None,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = if (label.equals("Password:", ignoreCase = true))
                        KeyboardType.Password else KeyboardType.Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                trailingIcon = {
                    if (label.equals(
                            "Password:",
                            ignoreCase = true
                        ) || label.equals("Security Code:", ignoreCase = true)
                    ) {
                        IconButton(
                            onClick = {
                                isVisible = !isVisible
                                onClearClick.invoke()
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = if (isVisible) R.drawable.ic_show else R.drawable.ic_hide),
                                contentDescription = if (isVisible) "Hide" else "Show"
                            )
                        }
                    }
                }
            )

            if (label.equals(
                    "Security Code:",
                    ignoreCase = true
                ) && isEditable && editableState.value.isNotEmpty()
            ) {
                SecurityCodeStrengthIndicator(securityCode = editableState.value)
            }
        } else {
            Text(
                text = displayedValue,
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}
