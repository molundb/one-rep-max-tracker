package net.martinlundberg.onerepmaxtracker.data.model

class Percentage(
    val percentage: Int,
    val weight: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Percentage) return false

        if (percentage != other.percentage) return false
        if (weight != other.weight) return false

        return true
    }

    override fun hashCode(): Int {
        var result = percentage
        result = 31 * result + weight
        return result
    }
}