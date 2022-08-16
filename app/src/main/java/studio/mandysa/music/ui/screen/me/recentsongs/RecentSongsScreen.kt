package studio.mandysa.music.ui.screen.me.recentsongs

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.ui.common.AppScaffold
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentSongsScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarState()
    )
    AppScaffold(
        modifier = Modifier
            .statusBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {

    }
}