package studio.mandysa.music.ui.screen.local

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMusicRepository
import studio.mandysa.music.ui.common.AppButton
import studio.mandysa.music.ui.common.SearchBar
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocalScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
) {
    val permissionState =
        rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    when (permissionState.status) {
        PermissionStatus.Granted -> {
            for (s in LocalMusicRepository.getAudioFiles()) {
                println(s)
            }
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                columns = GridCells.Fixed(2)
            ) {
                item(span = { GridItemSpan(2) }) {
                    SearchBar(click = { mainNavController.navigate(ScreenDestination.Search) }) {
                        Text(text = stringResource(id = R.string.search_hint))
                    }
                }
                item {

                }
            }
        }

        is PermissionStatus.Denied -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
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