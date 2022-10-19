package studio.mandysa.music.ui.screen.me.artistsub

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.ui.item.ItemSinger
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.onBackground

@Composable
fun ArtistSubScreen(
    mainNavController: NavController<ScreenDestination>,
    paddingValues: PaddingValues,
    artistSubViewModel: ArtistSubViewModel = viewModel()
) {
    val follows by artistSubViewModel.artistSubs.observeAsState(listOf())
    Column {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            contentColor = onBackground,
            elevation = 0.dp
        ) {
            IconButton(onClick = { mainNavController.pop() }) {
                Icon(Icons.Rounded.ArrowBack, null)
            }
        }
        LazyColumn {
            items(follows) {
                ItemSinger(model = it) {
                    mainNavController.navigate(ScreenDestination.Singer(it.id))
                }
            }
            item {
                Spacer(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}