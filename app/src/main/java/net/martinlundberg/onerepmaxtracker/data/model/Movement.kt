package net.martinlundberg.onerepmaxtracker.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.martinlundberg.onerepmaxtracker.data.database.model.MovementEntity

@Parcelize
data class Movement(
    val id: Long = 0, // Needs to be 0 to be overwritten by Room https://developer.android.com/reference/kotlin/androidx/room/PrimaryKey#autoGenerate()
    val name: String = "",
    val weight: Float? = null,
) : Parcelable

fun Movement.asEntity() =
    MovementEntity(
        id = id,
        name = name,
    )
