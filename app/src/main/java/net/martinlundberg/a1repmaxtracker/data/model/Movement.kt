package net.martinlundberg.a1repmaxtracker.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.martinlundberg.a1repmaxtracker.data.database.model.MovementEntity

@Parcelize
data class Movement(
    val id: Long? = null,
    val name: String = "",
    val weight: Int? = null,
) : Parcelable

fun Movement.asEntity() =
    MovementEntity(
        id = id,
        name = name,
    )


