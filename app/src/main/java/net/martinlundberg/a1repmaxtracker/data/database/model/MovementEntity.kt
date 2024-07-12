package net.martinlundberg.a1repmaxtracker.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail

@Entity
data class MovementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
)

fun Map.Entry<MovementEntity, List<ResultEntity>>.asExternalMovement() = Movement(
    id = key.id,
    name = key.name,
    weight = value.maxByOrNull { it.weightInKilos }?.weightInKilos
)

fun List<ResultEntity>.asExternalMovementDetail() = MovementDetail(
    results = this.map { it.asExternalModel() }
)
