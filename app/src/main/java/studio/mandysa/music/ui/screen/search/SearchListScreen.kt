package studio.mandysa.music.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.*
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.AppTabRow
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.search.singer.SearchSingerScreen
import studio.mandysa.music.ui.screen.search.single.SearchSingleScreen

enum class SearchListScreenDestination {
    Single, Singer
}

val SearchListScreenDestination.tabName: String
    @Composable get() = when (this) {
        SearchListScreenDestination.Single -> stringResource(id = R.string.single)
        SearchListScreenDestination.Singer -> stringResource(id = R.string.singer)
    }

@Composable
fun SearchListScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    keywords: String
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        var selectedIndex by rememberSaveable {
            mutableStateOf(0)
        }
        val navController =
            rememberNavController(startDestination = SearchListScreenDestination.Single)
        AppTabRow(
            selectedTabIndex = selectedIndex,
        ) {
            SearchListScreenDestination.values().forEachIndexed { index, destination ->
                Tab(
                    selected = selectedIndex == index,
                    text = {
                        Text(text = destination.tabName)
                    },
                    onClick = {
                        selectedIndex = index
                        if (!navController.moveToTop { it == destination }) {
                            navController.navigate(destination)
                        }
                    })
            }
        }
        NavHost(navController) {
            when (it) {
                SearchListScreenDestination.Single -> {
                    SearchSingleScreen(
                        dialogNavController = dialogNavController,
                        paddingValues = paddingValues,
                        keywords = keywords
                    )
                }
                SearchListScreenDestination.Singer -> {
                    SearchSingerScreen(
                        mainNavController = mainNavController,
                        paddingValues = paddingValues,
                        keywords = keywords
                    )
                }
            }
        }
    }
}