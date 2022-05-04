package studio.mandysa.music

import android.view.Gravity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.sothree.slidinguppanel.ktx.SlidingPanel
import studio.mandysa.music.ui.screen.BrowseScreen
import studio.mandysa.music.ui.screen.ControllerScreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen() {
    val items = LinkedHashMap<String, ImageVector>().apply {
        put(
            stringResource(R.string.browse),
            ImageVector.vectorResource(R.drawable.ic_round_contactless_24)
        )
        put(
            stringResource(id = R.string.me),
            ImageVector.vectorResource(R.drawable.ic_round_person_24)
        )
    }
    var selectedItem by remember {
        mutableStateOf(0)
    }
    val pagerState = rememberPagerState()
    LaunchedEffect(key1 = selectedItem, block = {
        pagerState.scrollToPage(selectedItem)
    })
    BoxWithConstraints {
        if (maxWidth >= 600.dp) {
            Row {

            }
        } else {
            Box {
                SlidingPanel(
                    gravity = Gravity.BOTTOM,
                    panelHeight = LocalDensity.current.run { 56.dp.roundToPx() },
                    shadowHeight = LocalDensity.current.run { 5.dp.roundToPx() },
                    content = {
                        HorizontalPager(
                            modifier = Modifier
                                .fillMaxWidth(),
                            count = 2,
                            state = pagerState,
                        ) { page ->
                            when (page) {
                                0 -> {
                                    BrowseScreen()
                                }
                                1 -> {}
                            }
                        }
                    },
                    panel = {
                        ControllerScreen()
                    })
                BottomNavigation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .align(Alignment.BottomCenter), backgroundColor = Color.White
                ) {
                    items.toList().forEachIndexed { index, pair ->
                        BottomNavigationItem(
                            selected = selectedItem == index,
                            onClick = { selectedItem = index },
                            icon = {
                                Icon(
                                    imageVector = pair.second,
                                    contentDescription = null
                                )
                            },
                            label = { Text(pair.first) },
                            selectedContentColor = MaterialTheme.colors.primary,
                            unselectedContentColor = Color.Gray
                        )
                    }
                }
            }
        }
    }
}