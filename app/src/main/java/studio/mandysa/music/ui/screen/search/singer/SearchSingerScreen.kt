package studio.mandysa.music.ui.screen.search.singer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.ui.item.ItemSinger
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun SearchSingerScreen(
    mainNavController: NavController<ScreenDestination>,
    paddingValues: PaddingValues,
    keywords: String,
    singerViewModel: SearchSingerViewModel = viewModel(factory = viewModelFactory {
        addInitializer(SearchSingerViewModel::class) { SearchSingerViewModel(keywords) }
    })
) {
    val singers by singerViewModel.singers.collectAsState(listOf())
    LazyColumn {
        items(singers) {
            ItemSinger(model = it) {
                mainNavController.navigate(ScreenDestination.Singer(it.id))
            }
        }
        item {
            Spacer(modifier = Modifier.padding(paddingValues))
        }
    }
}