package studio.mandysa.music.ui.screen.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import dev.olshevski.navigation.reimagined.NavController
import kotlinx.coroutines.launch
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.SettingRepository
import studio.mandysa.music.ui.common.AppTab
import studio.mandysa.music.ui.common.AppTabRow
import studio.mandysa.music.ui.common.HorizontalPager
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.search.singer.SearchSingerScreen
import studio.mandysa.music.ui.screen.search.single.SearchSingleScreen

enum class SearchListScreenDestination {
    Single, Album, Singer
}

val SearchListScreenDestination.tabName: String
    @Composable get() = when (this) {
        SearchListScreenDestination.Single -> stringResource(id = R.string.single)
        SearchListScreenDestination.Album -> stringResource(id = R.string.album)
        SearchListScreenDestination.Singer -> stringResource(id = R.string.singer)
    }

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SearchListScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    keywords: String
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    AppTabRow(
        modifier = Modifier.fillMaxWidth(),
        pagerState = pagerState
    ) {
        SearchListScreenDestination.values().forEachIndexed { index, destination ->
            AppTab(
                selected = pagerState.currentPage == index,
                text = {
                    Text(text = destination.tabName)
                },
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
        }
    }
    HorizontalPager(pages = SearchListScreenDestination.values(), state = pagerState) { t ->
        /*if (enableNeteaseCloud == true) {
            when (t) {
                SearchListScreenDestination.Single -> {
                    SearchSingleScreen(
                        dialogNavController = dialogNavController,
                        paddingValues = paddingValues,
                        keywords = keywords
                    )
                }

                SearchListScreenDestination.Album -> {

                }

                SearchListScreenDestination.Singer -> {
                    SearchSingerScreen(
                        mainNavController = mainNavController,
                        paddingValues = paddingValues,
                        keywords = keywords
                    )
                }
            }
        }*/
    }
}