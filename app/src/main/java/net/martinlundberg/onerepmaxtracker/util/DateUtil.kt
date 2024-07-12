package net.martinlundberg.onerepmaxtracker.util

import android.text.format.DateUtils
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun OffsetDateTime.formatTo(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))
fun LocalDateTime.formatTo(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))

fun OffsetDateTime.getRelativeDateString() =
    DateUtils.getRelativeTimeSpanString(
        this.toInstant().toEpochMilli(),
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    ).toString()