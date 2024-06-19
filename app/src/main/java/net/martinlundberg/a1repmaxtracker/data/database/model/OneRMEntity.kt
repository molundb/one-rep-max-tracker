package net.martinlundberg.a1repmaxtracker.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo

@Entity
data class OneRMEntity(
    @PrimaryKey(autoGenerate = true) val oneRMid: Int = 0,
    val movementId: Int,
    val weight: Int,
    val date: String,
)

fun OneRMEntity.asExternalModel() = OneRMInfo(
    weight = weight,
    date = date
)