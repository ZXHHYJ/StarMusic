package studio.mandysa.music.ui.common

import androidx.compose.runtime.Composable

@Composable
fun AppDialog(scope: @Composable () -> Unit) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = roundedCornerShape,
//        colors = CardDefaults.cardColors(containerColor = dialogBackground)
//    ) {
//        scope.invoke()
//    }
    AppCard {
        scope.invoke()
    }
}