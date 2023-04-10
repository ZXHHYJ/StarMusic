package studio.mandysa.music.ui.composable

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import studio.mandysa.music.R



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MediaPermission(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val permissionState =
        rememberPermissionState(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO else Manifest.permission.READ_EXTERNAL_STORAGE)
    when (permissionState.status) {
        PermissionStatus.Granted -> {
            Box(modifier = modifier) {
                content.invoke(this)
            }
        }

        is PermissionStatus.Denied -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.permission_media_lib))
                AppButton(onClick = { permissionState.launchPermissionRequest() }) {
                    Text(text = stringResource(id = R.string.go_permission))
                }
            }
        }
    }
}