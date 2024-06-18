package net.martinlundberg.a1repmaxtracker.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovementEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val weight: Int,
)
