package net.martinlundberg.a1repmaxtracker.data.model

import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity
import java.time.OffsetDateTime

data class OneRMInfo(
    val id: Long = 0, // Will be overwritten by Room
    val movementId: Long,
    val weight: Int,
    val date: OffsetDateTime,
)

fun OneRMInfo.asEntity() = OneRMEntity(
    movementId = movementId,
    weight = weight,
    date = date,
)