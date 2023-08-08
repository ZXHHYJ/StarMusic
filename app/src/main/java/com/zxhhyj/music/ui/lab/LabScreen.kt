package com.zxhhyj.music.ui.lab

import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.ui.view.Scaffold
import com.zxhhyj.ui.view.TopBar
import com.zxhhyj.ui.view.item.ItemSwitcher

@Composable
fun LabScreen(
    padding: PaddingValues
) {
    var isExternalStorageManager by rememberSaveable {
        mutableStateOf(false)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        isExternalStorageManager = Environment.isExternalStorageManager()
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                isExternalStorageManager = Environment.isExternalStorageManager()
                if (!isExternalStorageManager) {
                    SettingRepository.EnableCueSupport = false
                }
            }
        }
    )
    LaunchedEffect(SettingRepository.EnableCueSupport) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SettingRepository.EnableCueSupport && !isExternalStorageManager) {
            launcher.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.lab)
            )
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (SettingRepository.EnableAndroidMediaLibs) {
                item {
                    ItemSwitcher(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.LibraryMusic,
                                contentDescription = null
                            )
                        },
                        text = {
                            Text(text = stringResource(id = R.string.cue_support))
                        },
                        subText = {
                            Text(text = stringResource(id = R.string.cue_support_info))
                        },
                        checked = SettingRepository.EnableCueSupport,
                        onCheckedChange = {
                            SettingRepository.EnableCueSupport = it
                        }
                    )
                }
            }
        }
    }
}