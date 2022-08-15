package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import studio.mandysa.music.ui.theme.roundedCornerShape
import studio.mandysa.music.ui.theme.dialogBackground

@Composable
fun AppDialog(scope: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = roundedCornerShape,
        colors = CardDefaults.cardColors(containerColor = dialogBackground)
    ) {
        scope.invoke()
    }
}