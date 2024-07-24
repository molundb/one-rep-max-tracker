package net.martinlundberg.onerepmaxtracker.util

import kotlin.math.roundToInt

fun Float.roundToNearestQuarter() = (this / 0.25).roundToInt() * 0.25f

fun Float.removeTrailingZeros() = if (this == this.toInt().toFloat()) {
    this.toInt()
} else {
    this
}
