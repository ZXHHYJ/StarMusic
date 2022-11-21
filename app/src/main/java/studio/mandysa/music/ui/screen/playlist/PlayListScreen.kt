package studio.mandysa.music.ui.screen.playlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayListScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    drawerState: DrawerState,
    padding: PaddingValues
) {
}