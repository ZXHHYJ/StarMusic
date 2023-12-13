package com.zxhhyj.music.ui.screen.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.rounded.ModeNight
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.ui.theme.PaletteStyle
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.ItemCheckbox
import com.zxhhyj.ui.view.item.ItemDivider
import com.zxhhyj.ui.view.item.ItemSlider
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSubTitle
import java.math.BigDecimal
import java.math.RoundingMode

private val SettingRepository.DarkModeEnum.itemName: String
    @Composable get() = when (this) {
        SettingRepository.DarkModeEnum.AUTO -> stringResource(id = R.string.follow_system)
        SettingRepository.DarkModeEnum.LIGHT -> stringResource(id = R.string.light_mode)
        SettingRepository.DarkModeEnum.DARK -> stringResource(id = R.string.dark_mode)
    }

private val SettingRepository.DarkModeEnum.itemIcon: ImageVector
    @Composable get() = when (this) {
        SettingRepository.DarkModeEnum.AUTO -> Icons.Rounded.Smartphone
        SettingRepository.DarkModeEnum.LIGHT -> Icons.Rounded.WbSunny
        SettingRepository.DarkModeEnum.DARK -> Icons.Rounded.ModeNight
    }

@Composable
fun ThemeScreen(paddingValues: PaddingValues) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.theme)) })
        }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemCheckbox(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Nightlight,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.white_moonlight_and_cinnabar)) },
                        subText = { },
                        checked = SettingRepository.ThemeMode == SettingRepository.ThemeModeEnum.DEFAULT,
                        onCheckedChange = {
                            SettingRepository.ThemeMode = SettingRepository.ThemeModeEnum.DEFAULT
                        }
                    )
                    ItemDivider()
                    ItemCheckbox(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Palette,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.dynamic_colors_theme)) },
                        subText = { },
                        checked = SettingRepository.ThemeMode == SettingRepository.ThemeModeEnum.MONET,
                        onCheckedChange = {
                            SettingRepository.ThemeMode =
                                SettingRepository.ThemeModeEnum.MONET
                        }
                    )
                }
            }
            if (SettingRepository.ThemeMode == SettingRepository.ThemeModeEnum.MONET) {
                item {
                    ItemSubTitle {
                        Text(text = stringResource(id = R.string.palette))
                    }
                }
                item {
                    RoundColumn(modifier = Modifier.fillMaxWidth()) {
                        PaletteStyle.entries.forEach { style ->
                            ItemCheckbox(
                                text = { Text(text = style.name) },
                                subText = { },
                                checked = SettingRepository.MonetPaletteStyle == style,
                                onCheckedChange = {
                                    SettingRepository.MonetPaletteStyle = style
                                }
                            )
                        }
                    }
                }
                item { ItemSpacer() }
                item {
                    RoundColumn(modifier = Modifier.fillMaxWidth()) {
                        ItemSlider(
                            text = { Text(text = stringResource(id = R.string.contrast)) },
                            subText = {
                                Text(
                                    text = SettingRepository.MonetContrastLevel.toFloat().toString()
                                )
                            },
                            value = SettingRepository.MonetContrastLevel.toFloat(),
                            valueRange = -1f..1f,
                            onValueChange = {
                                SettingRepository.MonetContrastLevel =
                                    BigDecimal(it.toDouble()).setScale(1, RoundingMode.HALF_UP)
                                        .toDouble()
                            }
                        )
                    }
                }
            }
            item {
                ItemSubTitle {
                    Text(text = stringResource(id = R.string.dark_mode))
                }
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    SettingRepository.DarkModeEnum.entries.forEachIndexed { index, type ->
                        ItemCheckbox(
                            icon = {
                                Icon(
                                    imageVector = type.itemIcon,
                                    contentDescription = type.itemName
                                )
                            },
                            text = { Text(text = type.itemName) },
                            subText = { },
                            checked = SettingRepository.DarkMode == type,
                            onCheckedChange = {
                                SettingRepository.DarkMode = type
                            }
                        )
                        if (index != SettingRepository.DarkModeEnum.entries.size - 1) {
                            ItemDivider()
                        }
                    }
                }
            }
        }
    }
}