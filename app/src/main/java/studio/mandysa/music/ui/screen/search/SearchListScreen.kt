package studio.mandysa.music.ui.screen.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.R
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination

enum class SearchListScreenDestination {
    Single, Album, Singer
}

val SearchListScreenDestination.tabName: String
    @Composable get() = when (this) {
        SearchListScreenDestination.Single -> stringResource(id = R.string.single)
        SearchListScreenDestination.Album -> stringResource(id = R.string.album)
        SearchListScreenDestination.Singer -> stringResource(id = R.string.singer)
    }

@Composable
fun SearchListScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    paddingValues: PaddingValues,
    keywords: String
) {

}