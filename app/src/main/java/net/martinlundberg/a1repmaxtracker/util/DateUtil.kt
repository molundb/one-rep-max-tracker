package net.martinlundberg.a1repmaxtracker.util

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun OffsetDateTime.formatTo(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))
fun LocalDate.formatTo(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))