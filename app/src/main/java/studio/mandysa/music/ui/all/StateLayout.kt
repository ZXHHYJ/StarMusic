package studio.mandysa.music.ui.all

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zj.statelayout.ComposeStateLayout
import com.zj.statelayout.OnRetry
import com.zj.statelayout.PageStateData
import com.zj.statelayout.StateLayoutData

@Composable
fun DefaultStateLayout(
    modifier: Modifier = Modifier,
    pageStateData: PageStateData,
    onRetry: OnRetry = { },
    loading: @Composable (StateLayoutData) -> Unit = { Text("loading") },
    empty: @Composable (StateLayoutData) -> Unit = { },
    error: @Composable (StateLayoutData) -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    ComposeStateLayout(
        modifier = modifier,
        pageStateData = pageStateData,
        onRetry = onRetry,
        loading = { loading(it) },
        empty = { empty(it) },
        error = { error(it) },
        content = content
    )
}