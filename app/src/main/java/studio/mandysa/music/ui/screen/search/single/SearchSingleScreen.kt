package studio.mandysa.music.ui.screen.search.single

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.ui.screen.BottomSheetDestination

@Composable
fun SearchSingleScreen(
    dialogNavController: NavController<BottomSheetDestination>,
    paddingValues: PaddingValues,
    keywords: String
) {

    LazyColumn {
        /*itemsIndexed(songs) { index, item ->
            SongItem(dialogNavController = dialogNavController, song = item!!) {
                PlayManager.play(songs.itemSnapshotList, index)
            }
        }*/

    }
}