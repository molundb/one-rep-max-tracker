package net.martinlundberg.a1repmaxtracker.util

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalTime.formatTo(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))