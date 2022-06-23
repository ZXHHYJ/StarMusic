package studio.mandysa.music.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.*
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.AppDivider
import studio.mandysa.music.ui.common.AppTabRow
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination

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
    Column {
        AppDivider()
        var selectedIndex by remember {
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

                }
                SearchListScreenDestination.Singer -> {

                }
            }
        }
    }
}