package studio.mandysa.music.ui.common.lyric

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import studio.mandysa.music.ui.theme.cornerShape
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.translucentWhite

@Composable
fun Lyric(
    modifier: Modifier = Modifier,
    lyric: String,
    liveTime: Int,
    onClick: (Int) -> Unit
) {
    val viewModel = viewModel<LyricViewModel>()
    val state = rememberLazyListState()
    val position by viewModel.positionLiveData.observeAsState(0)
    val lyrics by viewModel.lyricLiveData.observeAsState()
    LaunchedEffect(key1 = lyric) {
        viewModel.lyric = lyric
    }
    LaunchedEffect(key1 = liveTime) {
        viewModel.liveTime = liveTime
    }
    LaunchedEffect(key1 = position) {
        if (position > 2 && !state.isScrollInProgress) {
            state.animateScrollToItem(position - 2)
        }
    }
    LazyColumn(modifier = modifier
        .graphicsLayer { alpha = 0.99F }
        .drawWithContent {
            val colors = listOf(
                Color.Transparent, Color.Black, Color.Black, Color.Black, Color.Black,
                Color.Black, Color.Black, Color.Black, Color.Transparent
            )
            drawContent()
            drawRect(
                brush = Brush.verticalGradient(colors),
                blendMode = BlendMode.DstIn
            )
        }, state = state) {
        lyrics?.let {
            itemsIndexed(it) { index, model ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 20.dp,
                            horizontal = horizontalMargin
                        )
                        .clip(cornerShape)
                        .clickable {
                            onClick.invoke(model.second)
                        },
                    text = model.first,
                    color = if (position == index) Color.White else translucentWhite,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
