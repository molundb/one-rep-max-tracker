package net.martinlundberg.onerepmaxtracker.data.model

data class MovementDetail(
    val movementName: String,
    val results: List<Result> = emptyList(),
) {
    fun formatResultDates(currentTimeMillis: Long) {
        results.map { result ->
            result.formatDate(currentTimeMillis)
        }
    }
}