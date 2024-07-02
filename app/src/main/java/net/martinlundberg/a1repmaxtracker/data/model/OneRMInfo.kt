package net.martinlundberg.a1repmaxtracker.data.model

import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class OneRMInfo(
    val id: Long = 0, // Will be overwritten by Room
    val weight: Int,
    val date: OffsetDateTime,
) {
    fun formattedDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        return date.format(formatter)
    }
}

fun OneRMInfo.asEntity(movementId: Long) = OneRMEntity(
    movementId = movementId,
    weight = weight,
    date = date,
)