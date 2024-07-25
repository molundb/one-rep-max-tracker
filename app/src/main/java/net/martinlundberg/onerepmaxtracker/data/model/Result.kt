package net.martinlundberg.onerepmaxtracker.data.model

import net.martinlundberg.onerepmaxtracker.data.database.model.ResultEntity
import java.time.OffsetDateTime

data class Result(
    val id: Long = 0, // Needs to be 0 to be overwritten by Room https://developer.android.com/reference/kotlin/androidx/room/PrimaryKey#autoGenerate()
    val movementId: Long,
    val weight: Float,
//    val offsetDateTimeAndFormatted: OffsetDateTimeAndFormatted,
    val offsetDateTime: OffsetDateTime,
    val dateTimeFormatted: String? = null, // This should be in UI model
    val comment: String,
)

fun Result.asEntity() =
    ResultEntity(
        resultId = id,
        movementId = movementId,
        weightInKilos = weight,
        date = offsetDateTime,
        comment = comment,
    )
