package studio.mandysa.music.ui.common

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MediaPermission(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val permissionState =
        rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
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
                Text("授权访问手机媒体以扫描媒体库中的音乐")
                AppButton(onClick = { permissionState.launchPermissionRequest() }) {
                    Text("前往授权")
                }
            }
        }
    }
}