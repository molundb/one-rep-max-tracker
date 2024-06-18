package net.martinlundberg.a1repmaxtracker.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo

@Entity
data class OneRMEntity(
    @PrimaryKey val oneRMid: Int,
    val movementId: Int,
    val weight: Int,
    val date: String,
)

fun OneRMEntity.asExternalModel() = OneRMInfo(
    weight = weight,
    date = date
)