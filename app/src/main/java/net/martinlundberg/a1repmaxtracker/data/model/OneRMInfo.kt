package net.martinlundberg.a1repmaxtracker.data.model

import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity

data class OneRMInfo(
    val weight: Int,
    val date: String,
)

fun OneRMInfo.asEntity(movementId: Int) = OneRMEntity(
    movementId = movementId,
    weight = weight,
    date = date,
)