package studio.mandysa.music.ui.common

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MediaPermission(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val permissionState =
        rememberPermissionState(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_EXTERNAL_STORAGE else Manifest.permission.READ_MEDIA_AUDIO)
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