package net.martinlundberg.onerepmaxtracker.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.repository.MovementsRepository

class FakeMovementsRepository(resultRepository: FakeResultRepository) : MovementsRepository {

    private val _movements = MutableStateFlow<List<Movement>>(emptyList())
    override val movements: Flow<List<Movement>> = combine(
        _movements,
        resultRepository.results, // TODO: Feels hacky. Maybe there's a better way?
    ) { movements, results ->
        movements.map { movement ->
            if (movement.weight == null) {
                movement.copy(weight = results.find { it.movementId == movement.id }?.weight)
            } else {
                movement
            }
        }
    }

    fun setMovements(movements: List<Movement>) {
        _movements.tryEmit(movements)
    }

    override suspend fun setMovement(movement: Movement): Long {
        val movementToEdit = _movements.value.find { it.id == movement.id }

        val updatedMovements = if (movementToEdit != null) {
            val movements = _movements.value.filter { it.id != movement.id }
            movements + movement
        } else {
            _movements.value + movement.copy(weight = null)
        }

        _movements.tryEmit(updatedMovements)
        return movement.id
    }

    override suspend fun deleteMovement(movementId: Long) {
        TODO("Not yet implemented")
    }
}