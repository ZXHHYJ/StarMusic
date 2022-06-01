package studio.mandysa.music.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contactless
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.rememberNavController
import studio.mandysa.music.ui.screen.browse.BrowseScreen
import studio.mandysa.music.ui.screen.me.MeScreen
import studio.mandysa.music.ui.theme.navHeight

/**
 * Happy 22nd Birthday Shuangshengzi
 */

enum class MainScreenDestination {
    Browse, Me
}

val MainScreenDestination.tabIcon
    get() = when (this) {
        MainScreenDestination.Browse -> Icons.Rounded.Contactless
        MainScreenDestination.Me -> Icons.Rounded.Person
    }

@Composable
fun MainScreen() {
    val navController = rememberNavController(
        startDestination = MainScreenDestination.values()[0],
    )
    rememberSystemUiController().apply {
        setSystemBarsColor(Color.Transparent, true, isNavigationBarContrastEnforced = false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .statusBarsPadding()
        ) {
            NavHost(navController) { screen ->
                when (screen) {
                    MainScreenDestination.Browse -> BrowseScreen()
                    MainScreenDestination.Me -> MeScreen()
                }
            }
        }
        ControllerScreen()
        val lastDestination = navController.backstack.entries.last().destination
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(navHeight)
        ) {
            MainScreenDestination.values().forEach { destination ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            destination.tabIcon,
                            contentDescription = null
                        )
                    },
                    selected = destination == lastDestination,
                    onClick = {
                        if (!navController.moveToTop { it == destination }) {
                            navController.navigate(destination)
                        }
                    }
                )
            }
        }
    }
}