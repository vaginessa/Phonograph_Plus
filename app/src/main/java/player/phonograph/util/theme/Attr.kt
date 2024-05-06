/*
 *  Copyright (c) 2022~2024 chr_56
 */

package player.phonograph.util.theme

import player.phonograph.R
import util.theme.internal.resolveColor
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import android.content.Context


@CheckResult
@ColorInt
fun themeFooterColor(context: Context) =
    context.resolveColor(
        R.attr.defaultFooterColor,
        context.getColor(R.color.footer_background_lightdark)
    )

@CheckResult
@ColorInt
fun themeIconColor(context: Context) =
    context.resolveColor(
        R.attr.iconColor,
        context.getColor(R.color.icon_lightdark)
    )

@CheckResult
@ColorInt
fun themeDividerColor(context: Context) =
    context.resolveColor(
        R.attr.dividerColor,
        context.getColor(R.color.divider_lightdark)
    )


@CheckResult
@ColorInt
fun themeCardBackgroundColor(context: Context) =
    context.resolveColor(
        com.google.android.material.R.attr.cardBackgroundColor,
        context.getColor(R.color.card_background_lightblack)
    )

@CheckResult
@ColorInt
fun themeFloatingBackgroundColor(context: Context) =
    context.resolveColor(
        com.google.android.material.R.attr.colorBackgroundFloating,
        context.getColor(R.color.card_background_lightblack)
    )