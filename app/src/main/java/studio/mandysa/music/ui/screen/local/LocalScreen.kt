package studio.mandysa.music.ui.screen.local

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.common.AppButton
import studio.mandysa.music.ui.common.SearchBar
import studio.mandysa.music.ui.item.SongItem
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
            val localMusicBeans = LocalMusicRepository.getAudioFiles()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                item {
                    SearchBar(click = { mainNavController.navigate(ScreenDestination.Search) }) {
                        Text(text = stringResource(id = R.string.search_hint))
                    }
                }
                itemsIndexed(localMusicBeans) { index, item->
                    SongItem(dialogNavController = dialogNavController, song = item) {
                        PlayManager.play(localMusicBeans, index)
                    }
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