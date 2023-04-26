package com.zxhhyj.music.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zxhhyj.music.ui.common.card.AppRoundCard
import com.zxhhyj.music.ui.theme.appAccentColor
import com.zxhhyj.music.ui.theme.appBackgroundColor


@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = appBackgroundColor,
        contentColor = appAccentColor,
        elevation = 0.dp,
        content = content
    )
}

@Composable
fun RowScope.AppBottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    imageVector: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    /*enabled: Boolean = true,*/
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium)
) {
    val iconCardBackground = selectedContentColor.copy(alpha = 0.15f)
    CompositionLocalProvider(
        LocalContentColor provides if (selected) selectedContentColor else unselectedContentColor,
        LocalContentAlpha provides if (selected) ContentAlpha.high else ContentAlpha.medium
    ) {
        Column(
            modifier = modifier
                .weight(1.0f)
                .fillMaxHeight()
                .clickable { onClick.invoke() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppRoundCard(
                backgroundColor = if (selected) iconCardBackground else Color.Transparent,
                modifier = Modifier.width(50.dp)
            ) {
                Icon(imageVector = imageVector, contentDescription = null)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = title, fontSize = 11.sp)
        }
    }

}
