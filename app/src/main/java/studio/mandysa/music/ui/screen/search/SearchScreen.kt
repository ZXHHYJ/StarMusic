package studio.mandysa.music.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.ui.common.SearchBar
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.onBackground
import studio.mandysa.music.ui.theme.textColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
) {
    // TODO: 搜索模块
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember {
        FocusRequester()
    }
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
        var search by rememberSaveable {
            mutableStateOf("")
        }
        SearchBar {
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                value = search,
                onValueChange = {
                    search = it
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = textColor,
                    fontSize = 16.sp,
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboard?.hide()
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )
        }
        DisposableEffect(key1 = this, effect = {
            focusRequester.requestFocus()
            keyboard?.show()
            onDispose {  }
        })
    }
}