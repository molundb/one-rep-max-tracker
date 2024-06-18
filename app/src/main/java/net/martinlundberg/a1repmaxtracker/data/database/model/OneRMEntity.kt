package net.martinlundberg.a1repmaxtracker.data.database.model

import androidx.room.Entity
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.OneRMInfo

@Entity
data class OneRMEntity(
    val movementId: Int,
    val weight: Int,
    val date: String,
)

fun OneRMEntity.asExternalModel() = OneRMInfo(
    weight = weight,
    date = date
)