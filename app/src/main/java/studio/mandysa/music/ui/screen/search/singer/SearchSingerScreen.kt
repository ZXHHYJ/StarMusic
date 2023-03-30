package studio.mandysa.music.ui.screen.search.singer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.ui.screen.ScreenDestination

@Composable
fun SearchSingerScreen(
    mainNavController: NavController<ScreenDestination>,
    paddingValues: PaddingValues,
    keywords: String
) {
    /*val singers by singerViewModel.singers.observeAsState(listOf())
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(singers) {
            SingerItem(model = it) {
                // TODO:
                //mainNavController.navigate(ScreenDestination.Singer(it.id))
            }
        }
    }*/
}