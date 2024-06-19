package net.martinlundberg.a1repmaxtracker.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail

@Entity
data class MovementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val weight: Int?,
)

fun MovementEntity.asExternalMovement() = Movement(
    id = id,
    name = name,
    weight = weight,
)

fun Map<MovementEntity, List<OneRMEntity>>.asExternalMovementDetail() = MovementDetail(
    oneRMs = this.entries.firstOrNull()?.value?.map { it.asExternalModel() } ?: emptyList()
)
