package studio.mandysa.music.ui.screen.singer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.neutralColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingerScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
) {
    LazyColumn {
        item {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                elevation = 0.dp
            ) {
                IconButton(onClick = { mainNavController.pop() }) {
                    Icon(Icons.Rounded.ArrowBack, null)
                }
                Spacer(modifier = Modifier.weight(1.0f))
                IconButton(onClick = { }) {
                    Icon(Icons.Rounded.MoreVert, null)
                }
            }
        }
        stickyHeader {
            var selectedIndex by remember {
                mutableStateOf(0)
            }
            Column {
                TabRow(
                    selectedTabIndex = selectedIndex,
                    containerColor = Color.Transparent,
                    contentColor = Color.Black,
                    indicator = {
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(it[selectedIndex]),
                            color = neutralColor
                        )
                    }
                ) {
                    Tab(
                        selected = selectedIndex == 0,
                        text = {
                            Text(text = "Tab0")
                        },
                        onClick = { selectedIndex = 0 })
                    Tab(selected = selectedIndex == 1, text = {
                        Text(text = "Tab1")
                    }, onClick = { selectedIndex = 1 })
                    Tab(selected = selectedIndex == 2, text = {
                        Text(text = "Tab2")
                    }, onClick = { selectedIndex = 2 })
                }
                Text("current index : $selectedIndex")
            }
        }
    }
}