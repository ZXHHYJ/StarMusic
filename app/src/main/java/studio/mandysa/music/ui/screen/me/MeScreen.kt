package studio.mandysa.music.ui.screen.me

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import studio.mandysa.music.R
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.ItemTitle
import studio.mandysa.music.ui.screen.playlist.PlaylistScreen
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Main(navController: NavHostController, me: MeViewModel = viewModel()) {
    me.refresh()
    val userInfo by me.getUserInfo().observeAsState()
    LazyColumn {
        item {
            ItemTitle(stringResource(R.string.me))
        }
        userInfo?.let {
            item {
                Card(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    shape = RoundedCornerShape(round)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                            Card(
                                modifier = Modifier
                                    .size(70.dp),
                                shape = RoundedCornerShape(35.dp)
                            ) {
                                AsyncImage(
                                    model = it.avatarUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                        }
                        Column {
                            Text(text = it.nickname)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = it.signature)
                        }
                    }
                }
            }
        }
        item {
            ItemSubTitle("More")
        }
    }
}

@Composable
fun MeScreen() {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = "main",
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        composable("main") { Main(navController) }
        composable("playlist/{id}") { backStackEntry ->
            PlaylistScreen(
                navController = navController,
                id = backStackEntry.arguments!!.getString("id", "")
            )
        }
    }
}