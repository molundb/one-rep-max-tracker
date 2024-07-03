package net.martinlundberg.a1repmaxtracker.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.martinlundberg.a1repmaxtracker.data.database.model.MovementEntity

@Parcelize
data class Movement(
    val id: Long = -1, // Will be overwritten by Room
    val name: String,
    val weight: Int? = null,
) : Parcelable

fun Movement.asEntity() = if (id == -1L) {
    MovementEntity(name = name)
} else {
    MovementEntity(id = id, name = name)
}

