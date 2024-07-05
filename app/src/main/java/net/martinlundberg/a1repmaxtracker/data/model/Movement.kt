package net.martinlundberg.a1repmaxtracker.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.martinlundberg.a1repmaxtracker.data.database.model.MovementEntity

@Parcelize
data class Movement(
    val id: Long = 0, // Needs to be 0 to be overwritten by Room https://stackoverflow.com/questions/44109700/how-to-make-primary-key-as-autoincrement-for-room-persistence-lib#comment107992156_44109700
    val name: String = "",
    val weight: Int? = null,
) : Parcelable

fun Movement.asEntity() =
    MovementEntity(
        id = id,
        name = name,
    )


