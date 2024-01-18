package com.example.practice.screens.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R
import com.example.practice.ui.theme.PracticeTheme

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
    ) {
        Text(
            text = label,
            style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
        )
        if (isEditable) {
            val editableState = remember { mutableStateOf(displayedValue) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ){

                Spacer(modifier=Modifier.width(8.dp))

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
                    .weight(1f),
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

            Spacer(modifier=Modifier.width(8.dp))
            }


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



@Preview
@Composable
fun SettingsFieldPreview() {
    PracticeTheme {
        SettingsField(
            label = "Password:",
            value = "1234",
            onValueChange = { /* */ },
            isEditable = true,
            onClearClick = { /* */ }
        )
    }
}
