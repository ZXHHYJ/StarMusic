@file:Suppress("DEPRECATION")

package com.zxhhyj.music.logic.helper

import android.content.pm.PackageManager
import android.os.Build
import com.zxhhyj.music.MainApplication

object VersionHelper {
    val VersionName: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        MainApplication.context.packageManager.getPackageInfo(
            MainApplication.context.packageName,
            PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        MainApplication.context.packageManager.getPackageInfo(
            MainApplication.context.packageName,
            0
        )
    }.versionName
}