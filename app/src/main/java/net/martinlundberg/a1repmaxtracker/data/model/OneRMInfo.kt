package net.martinlundberg.a1repmaxtracker.data.model

import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity
import java.time.OffsetDateTime

data class OneRMInfo(
    val id: Long = -1, // Will be overwritten by Room
    val movementId: Long,
    val weight: Int,
    val date: OffsetDateTime,
)

fun OneRMInfo.asEntity() =
    if (id == -1L) {
        OneRMEntity(
            movementId = movementId,
            weight = weight,
            date = date,
        )
    } else {
        OneRMEntity(
            oneRMid = id,
            movementId = movementId,
            weight = weight,
            date = date,
        )
    }