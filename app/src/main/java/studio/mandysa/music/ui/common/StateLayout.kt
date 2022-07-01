package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import studio.mandysa.music.R
import studio.mandysa.music.ui.base.BaseViewModel
import studio.mandysa.music.ui.theme.neutralColor

enum class State {
    LOADING, CONTENT, ERROR
}

@Composable
@Preview
fun PreviewStateLayout() {

}

@Composable
fun DefaultStateLayout(
    modifier: Modifier = Modifier,
    viewModel: BaseViewModel,
    content: @Composable () -> Unit
) {
    val state by viewModel.state.observeAsState()
    StateLayout(modifier = modifier, state = state, loading = {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = neutralColor
            )
        }
    }, error = {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = stringResource(id = R.string.network_error))
            MenuItem(
                title = stringResource(id = R.string.retry),
                imageVector = Icons.Rounded.Refresh
            ) {
                viewModel.refresh()
            }
        }
    }) {
        content.invoke()
    }
}

@Composable
fun StateLayout(
    modifier: Modifier,
    state: State?,
    loading: @Composable () -> Unit,
    error: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier) {
        when (state) {
            State.LOADING -> loading.invoke()
            State.CONTENT -> content.invoke()
            State.ERROR -> error.invoke()
            else -> {}
        }
    }
}