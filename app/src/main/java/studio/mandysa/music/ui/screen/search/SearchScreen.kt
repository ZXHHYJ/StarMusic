package studio.mandysa.music.ui.screen.search

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import studio.mandysa.music.R
import studio.mandysa.music.ui.item.ItemTitle

@Composable
fun SearchScreen() {
    LazyColumn {
        item {
            ItemTitle(stringResource(R.string.search))
        }
    }
}