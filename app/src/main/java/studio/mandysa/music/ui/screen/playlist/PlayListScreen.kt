package studio.mandysa.music.ui.screen.playlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.TopAppBar
import studio.mandysa.music.ui.common.rememberTopAppBarState
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination

/**
 * @author 黄浩
 */

@Composable
fun PlayListScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues
) {
    val topAppBarState = rememberTopAppBarState()
    TopAppBar(
        state = topAppBarState, modifier = Modifier, title = stringResource(id = R.string.play_list)
    )
}