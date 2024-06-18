package net.martinlundberg.a1repmaxtracker.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetail
import net.martinlundberg.a1repmaxtracker.feature.movementslist.Movement

@Entity
data class MovementEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val weight: Int,
)

fun MovementEntity.asExternalMovement() = Movement(
    name = name,
    weight = weight,
)

fun Map<MovementEntity, List<OneRMEntity>>.asExternalMovementDetail() = MovementDetail(
    oneRMs = this.entries.first().value.map { it.asExternalModel() }
)
