package net.martinlundberg.a1repmaxtracker.util

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun OffsetDateTime.formatTo(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))
fun LocalDateTime.formatTo(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))