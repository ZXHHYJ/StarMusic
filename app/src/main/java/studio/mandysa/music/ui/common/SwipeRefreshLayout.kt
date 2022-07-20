package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.drake.net.exception.NoCacheException
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun SwipeRefreshLayout(
    modifier: Modifier = Modifier,
    viewModel: SwipeRefreshViewModel,
    content: @Composable () -> Unit
) {

    fun refresh() {
        viewModel.viewModelScope.launch {
            try {
                viewModel.isRefreshing.value = true
                viewModel.refresh()
                viewModel.isRefreshing.value = false
            } catch (e: CancellationException) {
            } catch (e: Exception) {
                POPWindows.postValue(e.message.toString())
            }
        }
    }

    fun preview() {
        viewModel.viewModelScope.launch {
            try {
                viewModel.preview()
            } catch (e: CancellationException) {
            } catch (e: NoCacheException) {
                refresh()
            } catch (e: Exception) {
                POPWindows.postValue(e.message.toString())
            }
        }
    }

    val isRefreshing by viewModel.isRefreshing.observeAsState(false)
    Box(modifier = modifier) {
        DisposableEffect(key1 = this, effect = {
            preview()
            onDispose { }
        })
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                refresh()
            },
        ) {
            content.invoke()
        }
    }
}