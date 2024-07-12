package net.martinlundberg.a1repmaxtracker.data.model

import net.martinlundberg.a1repmaxtracker.data.database.model.ResultEntity
import java.time.OffsetDateTime

data class Result(
    val id: Long = 0, // Needs to be 0 to be overwritten by Room https://developer.android.com/reference/kotlin/androidx/room/PrimaryKey#autoGenerate()
    val movementId: Long,
    val weight: Float,
    val offsetDateTime: OffsetDateTime,
)

fun Result.asEntity() =
    ResultEntity(
        resultId = id,
        movementId = movementId,
        weightInKilos = weight,
        date = offsetDateTime,
    )
