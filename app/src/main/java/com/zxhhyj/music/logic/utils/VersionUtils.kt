package com.zxhhyj.music.logic.utils

import android.content.pm.PackageManager
import android.os.Build
import com.zxhhyj.music.MainApplication

object VersionUtils {

    /**
     * 获取应用程序的版本名称
     */
    val VersionName: String by lazy {
        val packageManager = MainApplication.context.packageManager
        val packageName = MainApplication.context.packageName
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName
        } catch (_: PackageManager.NameNotFoundException) {
            String()
        }
    }

    /**
     * 获取应用程序的版本号
     */
    @Suppress("DEPRECATION")
    val VersionCode: Long by lazy {
        val packageManager = MainApplication.context.packageManager
        val packageName = MainApplication.context.packageName

        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
        } catch (_: PackageManager.NameNotFoundException) {
            -1
        }
    }

}