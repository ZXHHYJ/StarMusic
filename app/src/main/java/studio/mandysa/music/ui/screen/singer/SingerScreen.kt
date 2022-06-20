package studio.mandysa.music.ui.screen.singer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import studio.mandysa.music.ui.theme.background

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun SingerScreen(id: String = "test") {
    LazyColumn(modifier = Modifier.background(background)) {
        item {

        }
        stickyHeader {
            var selectedIndex by remember {
                mutableStateOf(0)
            }
            Column {
                ScrollableTabRow(
                    selectedTabIndex = selectedIndex,
                    containerColor = Color.Gray,
                    indicator = {}
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