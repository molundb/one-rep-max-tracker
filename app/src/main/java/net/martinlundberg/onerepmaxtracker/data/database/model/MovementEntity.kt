package net.martinlundberg.onerepmaxtracker.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResults
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResults.BEST
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResults.LATEST

@Entity
data class MovementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
)

fun Map.Entry<MovementEntity, List<ResultEntity>>.asExternalMovement(latestOrBestResults: LatestOrBestResults) =
    Movement(
    id = key.id,
    name = key.name,
        weight = when (latestOrBestResults) {
            LATEST -> value.maxByOrNull { it.date }?.weightInKilos
            BEST -> value.maxByOrNull { it.weightInKilos }?.weightInKilos
        }
)

fun Map.Entry<MovementEntity, List<ResultEntity>>.asExternalMovementDetail() = MovementDetail(
    movementName = key.name,
    results = value.map { it.asExternalModel() }
)
