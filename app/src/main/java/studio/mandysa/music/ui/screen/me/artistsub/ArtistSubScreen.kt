package studio.mandysa.music.ui.screen.me.artistsub

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.AppMediumTopAppBar
import studio.mandysa.music.ui.common.AppScaffold
import studio.mandysa.music.ui.common.Preview
import studio.mandysa.music.ui.item.ItemSinger
import studio.mandysa.music.ui.screen.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistSubScreen(
    mainNavController: NavController<ScreenDestination>,
    paddingValues: PaddingValues,
    viewModel: ArtistSubViewModel = viewModel()
) {
    val follows = viewModel.artistSublist
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarState()
    )
    AppScaffold(modifier = Modifier
        .statusBarsPadding()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppMediumTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.album))
                },
                navigationIcon = {
                    IconButton(onClick = { mainNavController.pop() }) {
                        Icon(Icons.Rounded.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }) {
        Preview(modifier = Modifier.padding(it), refresh = { viewModel.refresh() }) {
            LazyColumn {
                follows?.let { list ->
                    items(list) {
                        ItemSinger(model = it) {
                            mainNavController.navigate(ScreenDestination.Singer(it.id))
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}