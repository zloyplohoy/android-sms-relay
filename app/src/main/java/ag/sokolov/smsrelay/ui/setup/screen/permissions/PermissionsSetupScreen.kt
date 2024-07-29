package ag.sokolov.smsrelay.ui.setup.screen.permissions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.serialization.Serializable

@Serializable
object PermissionsSetupScreen

fun NavController.navigateToPermissionsSetup() =
    navigate(route = PermissionsSetupScreen)

fun NavGraphBuilder.permissionsSetupScreen(
    onContinue: () -> Unit
) =
    composable<PermissionsSetupScreen> {
        PermissionsSetupScreen(
            onContinue = onContinue
        )
    }

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsSetupScreen(onContinue: () -> Unit) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.READ_SMS)
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}

    Column {
        Text(text = "Permission granted: ${permissionState.status}")
        Button(onClick = {
            requestPermissionLauncher.launch(Manifest.permission.READ_SMS)
        }) {
            Text(text = "Request permission")
        }
        Button(onClick = onContinue) {
            Text(text = "Finish")
        }
    }
}
