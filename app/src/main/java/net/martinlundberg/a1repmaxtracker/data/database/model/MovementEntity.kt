package net.martinlundberg.a1repmaxtracker.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail

@Entity
data class MovementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
)

fun Map.Entry<MovementEntity, List<OneRMEntity>>.asExternalMovement() = Movement(
    id = key.id,
    name = key.name,
    weight = value.maxByOrNull { it.weight }?.weight
)

fun Map<MovementEntity, List<OneRMEntity>>.asExternalMovementDetail() = MovementDetail(
    oneRMs = this.entries.firstOrNull()?.value?.map { it.asExternalModel() } ?: emptyList()
)
