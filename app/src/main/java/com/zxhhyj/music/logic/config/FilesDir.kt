package com.zxhhyj.music.logic.config

import com.zxhhyj.music.MainApplication

/**
 * WebDav音乐下载文件夹
 */
val musicFilesDir by lazy { MainApplication.context.getExternalFilesDir("music") }

/**
 * 歌曲封面文件夹
 */
val coverFilesDir by lazy { MainApplication.context.getExternalFilesDir("cover") }