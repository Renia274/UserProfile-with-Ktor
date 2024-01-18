package com.example.practice.screens.userprofile.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.practice.screens.items.DropDownList

@Composable
fun EditProfessionSection(
    selectedProfession: String,
    onProfessionValueChange: (String) -> Unit,
    isDropDownListVisible: Boolean,
    onDropDownClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            value = selectedProfession,
            onValueChange = { newProfession ->
                onProfessionValueChange(newProfession)
            },
            label = { Text("Enter Profession") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .weight(1f),
            trailingIcon = {
                IconButton(onClick = {
                    onDropDownClick()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        )

        // Show the DropDownList when isDropDownListVisible is true
        if (isDropDownListVisible) {
            DropDownList(
                onOptionSelected = { selectedOption ->
                    // Handle the selected option
                    onProfessionValueChange(selectedOption)
                },
                expanded = isDropDownListVisible,
                onDismissRequest = {
                    // Close the DropDownList when dismissed
                    onDropDownClick()
                }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}