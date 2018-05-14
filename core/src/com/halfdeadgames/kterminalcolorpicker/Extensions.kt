package com.halfdeadgames.kterminalcolorpicker

fun Float.format(digits: Int) = java.lang.String.format("%.${digits}f", this)