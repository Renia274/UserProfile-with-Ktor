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





//@Composable
//fun CameraButton(permissionStateViewModel: PermissionStateViewModel) {
//
//    val context = LocalContext.current
//    val launcher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//
//            Log.d("Camera", "Camera permission granted")
//        } else {
//
//            Log.d("Camera", "Camera permission denied")
//
//
//        }
//    }
//
//    Button(
//        onClick = {
//            permissionStateViewModel.setCameraPermissionDialogShown(true)
//            when (PackageManager.PERMISSION_GRANTED) {
//                ContextCompat.checkSelfPermission(
//                    context,
//                    android.Manifest.permission.CAMERA
//                ) -> {
//                    //
//                    Log.d("Camera", "Camera requires permission")
//
//                }
//
//                else -> {
//                    // Asking for permission
//                    launcher.launch(android.Manifest.permission.CAMERA)
//                }
//            }
//        },
//        modifier = Modifier.size(width = 100.dp, height = 40.dp)
//    ) {
//        Text(text = "CAMERA")
//    }
//}
//
//
//
//@Composable
//fun MicButton(permissionStateViewModel: PermissionStateViewModel) {
//
//    val context = LocalContext.current
//
//    val launcher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//
//            Log.d("Mic", "Microphone permission granted")
//
//        } else {
//
//            Log.d("Mic", "Microphone permission denied")
//
//
//        }
//    }
//
//    Button(
//        onClick = {
//            permissionStateViewModel.setMicrophonePermissionDialogShown(true)
//            when (PackageManager.PERMISSION_GRANTED) {
//                ContextCompat.checkSelfPermission(
//                    context,
//                    android.Manifest.permission.RECORD_AUDIO
//                ) -> {
//
//                    Log.d("Mic", "Microphone requires permission")
//                }
//
//                else -> {
//                    // Asking for permission
//                    launcher.launch(android.Manifest.permission.RECORD_AUDIO)
//
//                }
//            }
//        },
//        modifier = Modifier.size(width = 100.dp, height = 40.dp)
//    ) {
//        Text(text = "MIC")
//    }
//}
