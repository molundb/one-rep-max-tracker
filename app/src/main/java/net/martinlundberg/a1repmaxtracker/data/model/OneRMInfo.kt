package net.martinlundberg.a1repmaxtracker.data.model

import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity
import java.time.OffsetDateTime

data class OneRMInfo(
    val id: Long = 0, // Needs to be 0 to be overwritten by Room https://stackoverflow.com/questions/44109700/how-to-make-primary-key-as-autoincrement-for-room-persistence-lib#comment107992156_44109700
    val movementId: Long,
    val weight: Float,
    val offsetDateTime: OffsetDateTime,
)

fun OneRMInfo.asEntity() =
    OneRMEntity(
        oneRMid = id,
        movementId = movementId,
        weightInKilos = weight,
        date = offsetDateTime,
    )
