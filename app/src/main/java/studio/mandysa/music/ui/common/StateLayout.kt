package studio.mandysa.music.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
@Preview
fun PreviewStateLayout() {

}

@Composable
fun StateLayout(
    modifier: Modifier = Modifier,
    viewModel: SwipeRefreshViewModel,
    content: @Composable () -> Unit
) {
    val isRefreshing by viewModel.isRefreshing.observeAsState(false)
    SwipeRefresh(
        modifier = modifier,
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refresh() },
    ) {
        content.invoke()
    }
}