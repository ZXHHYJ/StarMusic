package com.zxhhyj.music

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zxhhyj.music.ui.theme.StarMusicTheme
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemTint

class CrashActivity : ComponentActivity() {
    companion object {
        const val LOG_KEY = "LOG"

        fun startActivity(context: Context, log: String) {
            context.startActivity(Intent(context, CrashActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putExtra(LOG_KEY, log)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            rememberSystemUiController().setSystemBarsColor(
                Color.Transparent,
                !isSystemInDarkTheme(),
                isNavigationBarContrastEnforced = false
            )
            StarMusicTheme {
                AppScaffold(
                    topBar = {
                        AppCenterTopBar(
                            modifier = Modifier.fillMaxWidth(),
                            title = { Text(text = stringResource(id = R.string.crash_info)) }
                        ) {

                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                                ItemTint {
                                    Text(text = intent.extras?.getString(LOG_KEY) ?: "")
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}