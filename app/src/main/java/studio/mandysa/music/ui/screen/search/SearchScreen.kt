package studio.mandysa.music.ui.screen.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.R
import studio.mandysa.music.ui.item.ItemTitle
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun SearchScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
) {
    LazyColumn {
        item {
            ItemTitle(stringResource(R.string.search))
        }
    }
}