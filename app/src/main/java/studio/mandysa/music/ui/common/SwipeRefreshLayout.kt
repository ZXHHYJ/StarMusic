package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.drake.net.exception.HttpFailureException
import com.drake.net.exception.NoCacheException
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import studio.mandysa.music.R
import studio.mandysa.music.logic.config.mainApplication

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
            } catch (e: HttpFailureException) {
                POPWindows.postValue(mainApplication.getString(R.string.network_error))
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

            }
        }
    }

    val isRefreshing by viewModel.isRefreshing.observeAsState(false)
    var previewed by rememberSaveable {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        DisposableEffect(key1 = this, effect = {
            if (!previewed) {
                preview()
                previewed = true
            }
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