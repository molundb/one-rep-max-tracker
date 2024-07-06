package net.martinlundberg.a1repmaxtracker.util

import kotlin.math.roundToInt

fun Double.roundToNearestQuarter() = (this / 0.25).roundToInt() * 0.25
