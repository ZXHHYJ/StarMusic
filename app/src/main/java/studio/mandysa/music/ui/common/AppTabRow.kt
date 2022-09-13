package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import studio.mandysa.music.ui.theme.anyBarColor
import studio.mandysa.music.ui.theme.onBackground

@Composable
fun AppTabRow(selectedTabIndex: Int, tabs: @Composable () -> Unit) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.Transparent,
        contentColor = onBackground,
        indicator = {
            Card(
                modifier = Modifier
                    .tabIndicatorOffset(it[selectedTabIndex])
                    .height(6.dp)
                    .padding(horizontal = 50.dp),
                shape = RoundedCornerShape(3.dp),
                backgroundColor = anyBarColor
            ) {
            }
        },
    ) {
        tabs.invoke()
    }
}