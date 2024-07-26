package net.martinlundberg.onerepmaxtracker.ui.model

import java.time.OffsetDateTime

class ResultUiModel(
    val id: Long = 0,
    val movementId: Long,
    val weight: Float,
    val offsetDateTime: OffsetDateTime,
    val dateTimeFormatted: String,
    val comment: String,
)