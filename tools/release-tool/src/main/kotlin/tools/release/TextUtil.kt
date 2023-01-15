/*
 * Copyright (c) 2022~2023 chr_56
 */

package tools.release

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale



val currentTimeString: String
    get() = SimpleDateFormat("yyMMddHHmmss", Locale.ENGLISH).format(Calendar.getInstance().time)

fun String.shiftFirstLetter(): String {
    if (isBlank()) return this
    return this[0].uppercaseChar() + this.substring(1)
}

