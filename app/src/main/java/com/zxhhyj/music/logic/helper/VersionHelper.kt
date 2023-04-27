package com.zxhhyj.music.logic.helper

import android.content.pm.PackageManager
import android.os.Build
import com.zxhhyj.music.logic.config.application

object VersionHelper {
    val VersionName: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        application.packageManager.getPackageInfo(
            application.packageName,
            PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        @Suppress("DEPRECATION")
        application.packageManager.getPackageInfo(application.packageName, 0)
    }.versionName
}