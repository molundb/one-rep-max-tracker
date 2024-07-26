package net.martinlundberg.onerepmaxtracker.data.model

import net.martinlundberg.onerepmaxtracker.data.database.model.MovementEntity

data class Movement(
    val id: Long = 0, // Needs to be 0 to be overwritten by Room https://developer.android.com/reference/kotlin/androidx/room/PrimaryKey#autoGenerate()
    val name: String = "",
    val weight: Float? = null,
) {
    fun asEntity() =
        MovementEntity(
            id = id,
            name = name,
        )
}
