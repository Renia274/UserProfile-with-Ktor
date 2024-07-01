package com.example.practice.screens.pin



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.R
import com.example.practice.profiles.viewmodel.pin.PinViewModel
import com.example.practice.ui.theme.PracticeTheme

@Composable
fun PinSetupScreen(

    onNavigate: () -> Unit,
    onBack: () -> Unit,
    pinViewModel: PinViewModel = hiltViewModel()
) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPinVisible by remember { mutableStateOf(false) }
    var isConfirmPinVisible by remember { mutableStateOf(false) }

    PinSetupContent(
        pin = pin,
        onPinChange = { newPin ->
            pin = newPin
            errorMessage = null
        },
        confirmPin = confirmPin,
        onConfirmPinChange = { newConfirmPin ->
            confirmPin = newConfirmPin
            errorMessage = null
        },
        isPinVisible = isPinVisible,
        onTogglePinVisibility = { isPinVisible = !isPinVisible },
        isConfirmPinVisible = isConfirmPinVisible,
        onToggleConfirmPinVisibility = { isConfirmPinVisible = !isConfirmPinVisible },
        onSavePin = {
            if (pin == confirmPin && pin.length == 6) {
                pinViewModel.savePinForProfile("Bob", pin)
                pinViewModel.savePinForProfile("Alice", pin)
                pinViewModel.savePinForProfile("Eve", pin)
                onNavigate()
            } else {
                errorMessage = "Pins do not match or are not 6 digits long"
            }
        },
        errorMessage = errorMessage,
        onBack = onBack
    )
}

@Composable
fun PinSetupContent(
    pin: String,
    onPinChange: (String) -> Unit,
    confirmPin: String,
    onConfirmPinChange: (String) -> Unit,
    isPinVisible: Boolean,
    onTogglePinVisibility: () -> Unit,
    isConfirmPinVisible: Boolean,
    onToggleConfirmPinVisibility: () -> Unit,
    onSavePin: () -> Unit,
    errorMessage: String?,
    onBack: () -> Unit
) {
    val maxPinLength = 6

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("") },
            navigationIcon = {
                IconButton(
                    onClick = { onBack.invoke() }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            backgroundColor = Color.White,
            contentColor = Color.Black,
            elevation = 0.dp
        )


        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "Set Your Pin",
            style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = pin,
            onValueChange = {
                if (it.length <= maxPinLength) {
                    onPinChange(it)
                }
            },
            label = { Text("Enter PIN") },
            visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(
                    onClick = { onTogglePinVisibility() },
                ) {
                    Icon(
                        painter = painterResource(id = if (isPinVisible) R.drawable.ic_show else R.drawable.ic_hide),
                        contentDescription = if (isPinVisible) "Hide pin" else "Show pin"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPin,
            onValueChange = {
                if (it.length <= maxPinLength) {
                    onConfirmPinChange(it)
                }
            },
            label = { Text("Confirm PIN") },
            visualTransformation = if (isConfirmPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(
                    onClick = { onToggleConfirmPinVisibility() },
                ) {
                    Icon(
                        painter = painterResource(id = if (isConfirmPinVisible) R.drawable.ic_show else R.drawable.ic_hide),
                        contentDescription = if (isConfirmPinVisible) "Hide pin" else "Show pin"
                    )
                }
            },
            isError = confirmPin.length > maxPinLength,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = onSavePin,
                enabled = pin == confirmPin && pin.length == maxPinLength,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Save PIN")
            }
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun PinSetupContentPreview() {
    PracticeTheme {
        PinSetupContent(
            pin = "123456",
            onPinChange = {},
            confirmPin = "123456",
            onConfirmPinChange = {},
            isPinVisible = true,
            onTogglePinVisibility = {},
            isConfirmPinVisible = true,
            onToggleConfirmPinVisibility = {},
            onSavePin = {},
            errorMessage = "Pins do not match or are not 6 digits long",
            onBack = {}
        )
    }
}
