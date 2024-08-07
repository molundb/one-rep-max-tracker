package net.martinlundberg.onerepmaxtracker.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.martinlundberg.onerepmaxtracker.data.model.Result
import java.time.OffsetDateTime

@Entity
data class ResultEntity(
    @PrimaryKey(autoGenerate = true) val resultId: Long,
    val movementId: Long,
    val weightInKilos: Float,
    val date: OffsetDateTime,
    val comment: String,
)

fun ResultEntity.asExternalModel() = Result(
    id = resultId,
    movementId = movementId,
    weight = weightInKilos,
    offsetDateTime = date,
    comment = comment,
)