package net.martinlundberg.a1repmaxtracker.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import java.time.OffsetDateTime

@Entity
data class OneRMEntity(
    @PrimaryKey(autoGenerate = true) val oneRMid: Long,
    val movementId: Long,
    val weightInKilos: Float,
    val date: OffsetDateTime,
)

fun OneRMEntity.asExternalModel() = OneRMInfo(
    id = oneRMid,
    movementId = movementId,
    weight = weightInKilos,
    offsetDateTime = date,
)