import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practice.profiles.viewmodel.permissions.PermissionStateViewModel

@Composable
fun CameraButton(permissionStateViewModel: PermissionStateViewModel) {
    Button(
        onClick = {
            permissionStateViewModel.setCameraPermissionDialogShown(true)
        },
        modifier = Modifier.size(width = 100.dp, height = 40.dp)
    ) {
        Text(text = "CAMERA")
    }
}

@Composable
fun MicButton(permissionStateViewModel: PermissionStateViewModel) {
    Button(
        onClick = {
            permissionStateViewModel.setMicrophonePermissionDialogShown(true)
        },
        modifier = Modifier.size(width = 100 .dp, height = 40.dp)
    ) {
        Text(text = "MIC")
    }
}
