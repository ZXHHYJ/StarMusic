package com.zxhhyj.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.theme.StarDimens

@Composable
fun RoundColumn(modifier: Modifier, content: @Composable () -> Unit) {
    AppCard(
        backgroundColor = LocalColorScheme.current.highBackground,
        modifier = modifier.padding(horizontal = StarDimens.horizontal)
    ) {
        Column(modifier = modifier) {
            content.invoke()
        }
    }
}

fun LazyListScope.roundColumn(content: RoundColumnLazyListScope.() -> Unit) {
    content.invoke(RoundColumnLazyListScope(this))
}

class RoundColumnLazyListScope(private val lazyListScope: LazyListScope) : LazyListScope {

    @ExperimentalFoundationApi
    override fun stickyHeader(
        key: Any?,
        contentType: Any?,
        content: @Composable (LazyItemScope.() -> Unit)
    ) {
        lazyListScope.stickyHeader(key, contentType, content)
    }

    override fun item(
        key: Any?,
        contentType: Any?,
        content: @Composable (LazyItemScope.() -> Unit)
    ) {
        lazyListScope.item(key, contentType, content)
    }

    override fun items(
        count: Int,
        key: ((index: Int) -> Any)?,
        contentType: (index: Int) -> Any?,
        itemContent: @Composable (LazyItemScope.(index: Int) -> Unit)
    ) {
        lazyListScope.items(count, key, contentType) {
            when {
                it == 0 -> {
                    AppCard(
                        backgroundColor = LocalColorScheme.current.highBackground,
                        shape = RoundedCornerShape(
                            topStart = StarDimens.round,
                            topEnd = StarDimens.round
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = StarDimens.horizontal)
                    ) {
                        itemContent(this, it)
                    }
                }

                count == 1 -> {
                    RoundColumn(modifier = Modifier.fillMaxWidth()) {
                        itemContent(this, it)
                    }
                }

                it == (count - 1) -> {
                    AppCard(
                        backgroundColor = LocalColorScheme.current.highBackground,
                        shape = RoundedCornerShape(
                            bottomStart = StarDimens.round,
                            bottomEnd = StarDimens.round
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = StarDimens.horizontal)
                    ) {
                        itemContent(this, it)
                    }
                }

                else -> {
                    AppCard(
                        backgroundColor = LocalColorScheme.current.highBackground,
                        shape = RoundedCornerShape(0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = StarDimens.horizontal)
                    ) {
                        itemContent(this, it)
                    }
                }

            }
        }
    }

}