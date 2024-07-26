package net.martinlundberg.onerepmaxtracker.ui.model

class MovementDetailUiModel(
    val movementName: String,
    val results: List<ResultUiModel> = emptyList(),
)