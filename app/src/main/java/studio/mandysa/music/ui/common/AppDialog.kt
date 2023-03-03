package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import studio.mandysa.music.ui.theme.appBackgroundColor

@Composable
fun AppDialog(scope: @Composable () -> Unit) {
    AppCard(modifier = Modifier.fillMaxWidth(), backgroundColor = appBackgroundColor) {
        scope.invoke()
    }
}