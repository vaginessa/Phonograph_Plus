/*
 * Copyright (c) 2022 chr_56
 */

package lib.phonograph.preferencedsl


import androidx.annotation.ColorInt
import android.content.res.ColorStateList
import android.graphics.Color

fun generateColorStateList(@ColorInt primaryColor: Int, @ColorInt defaultColor: Int) =
    ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(),
        ),
        intArrayOf(primaryColor, Color.GRAY, defaultColor, defaultColor)
    )