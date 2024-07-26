package net.martinlundberg.onerepmaxtracker.ui.model

import net.martinlundberg.onerepmaxtracker.data.model.Movement

data class MovementUiModel(
    val id: Long = 0,
    val name: String,
    val weight: String? = null,
) {
    fun asDomain() = Movement(
        id = id,
        name = name,
        weight = weight?.toFloatOrNull(),
    )
}