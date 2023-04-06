package studio.mandysa.music.ui.screen.scanmusic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMediaRepository
import studio.mandysa.music.ui.common.*
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.cardBackgroundColor

/**
 * @author 黄浩
 */
@Composable
fun ScanMusicScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues
) {
    val topAppBarState = rememberTopAppBarState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .bindTopAppBarState(topAppBarState)
            .fillMaxSize()
            .padding(padding)
    ) {
        item {
            AppRoundCard(backgroundColor = cardBackgroundColor, modifier = Modifier.size(80.dp)) {
                Text(
                    text = "${LocalMediaRepository.songs.size}",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        item {
            AppButton(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    LocalMediaRepository.scanMedia()
                }
            }) {
                Text(text = stringResource(id = R.string.scan_music))
            }
        }
    }
    TopAppBar(
        state = topAppBarState,
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.scan_music)
    )
}