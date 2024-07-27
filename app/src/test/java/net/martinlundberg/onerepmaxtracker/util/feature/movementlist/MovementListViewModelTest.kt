package net.martinlundberg.onerepmaxtracker.util.feature.movementlist

import kotlinx.coroutines.test.runTest
import net.martinlundberg.onerepmaxtracker.MainDispatcherRule
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent
import net.martinlundberg.onerepmaxtracker.analytics.createMovementParams
import net.martinlundberg.onerepmaxtracker.analytics.createResultParams
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.fakes.FakeAnalyticsHelper
import net.martinlundberg.onerepmaxtracker.fakes.FakeClockService
import net.martinlundberg.onerepmaxtracker.fakes.FakeMovementsRepository
import net.martinlundberg.onerepmaxtracker.fakes.FakeResultRepository
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListViewModel
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.KILOGRAMS
import net.martinlundberg.onerepmaxtracker.util.millisToOffsetDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId

class MovementListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val resultRepository = FakeResultRepository()
    private val movementsRepository = FakeMovementsRepository(resultRepository)
    private val clockService = FakeClockService()
    private val analyticsHelper = FakeAnalyticsHelper()

    private lateinit var viewModel: MovementListViewModel

    @Before
    fun setUp() {
        viewModel = MovementListViewModel(
            movementsRepository,
            resultRepository,
            clockService,
            analyticsHelper,
        )
    }

    @Test
    fun stateIsInitiallyLoading() {
        assertEquals(MovementListUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun whenAddMovementWithoutWeight_thenMovementIsAddedAndTracked() = runTest {
        viewModel.getMovements()

        assertEquals(
            MovementListUiState.Success(
                movements = emptyList(),
                weightUnit = KILOGRAMS,
                isAnalyticsEnabled = false,
            ),
            viewModel.uiState.value
        )

        viewModel.addMovement(
            movement = sampleMovementWithoutWeight,
            weightUnit = KILOGRAMS,
        )

        assertEquals(
            MovementListUiState.Success(
                movements = listOf(
                    sampleMovementWithoutWeight,
                ),
                weightUnit = KILOGRAMS,
                isAnalyticsEnabled = false,
            ),
            viewModel.uiState.value
        )

        assertEquals(1, analyticsHelper.numberOfEvents())

        assertTrue(
            analyticsHelper.hasLogged(
                AnalyticsEvent(
                    type = "add_movement",
                    extras = createMovementParams(sampleMovementWithoutWeight),
                ),
            ),
        )
    }

    @Test
    fun whenAddMovementWithWeight_thenMovementAndResultAreAddedAndTracked() = runTest {
        viewModel.getMovements()

        assertEquals(
            MovementListUiState.Success(
                movements = emptyList(),
                weightUnit = KILOGRAMS,
                isAnalyticsEnabled = false,
            ),
            viewModel.uiState.value
        )

        viewModel.addMovement(
            movement = sampleMovementWithWeight,
            weightUnit = KILOGRAMS,
        )

        assertEquals(
            MovementListUiState.Success(
                movements = listOf(
                    sampleMovementWithWeight,
                ),
                weightUnit = KILOGRAMS,
                isAnalyticsEnabled = false,
            ),
            viewModel.uiState.value
        )

        assertEquals(2, analyticsHelper.numberOfEvents())

        assertTrue(
            analyticsHelper.hasLogged(
                AnalyticsEvent(
                    type = "add_movement",
                    extras = createMovementParams(sampleMovementWithWeight),
                ),
            ),
        )

        assertTrue(
            analyticsHelper.hasLogged(
                AnalyticsEvent(
                    type = "add_result",
                    extras = createResultParams(
                        Result(
                            id = 0,
                            movementId = sampleMovementWithWeight.id,
                            weight = sampleMovementWithWeight.weight!!,
                            offsetDateTime = clockService
                                .getCurrentTimeMillis()
                                .millisToOffsetDateTime(ZoneId.systemDefault()),
                            comment = "",
                        ),
                    ),
                ),
            )
        )
    }

    // TODO: Is this test even a good idea? It feels like it's just testing FakeMovementsRepository.
    @Test
    fun whenEditMovement_thenMovementIsEditedAndTracked() = runTest {
        movementsRepository.setMovements(listOf(sampleMovementWithWeight))
        viewModel.getMovements()

        assertEquals(
            MovementListUiState.Success(
                movements = listOf(sampleMovementWithWeight),
                weightUnit = KILOGRAMS,
                isAnalyticsEnabled = false,
            ),
            viewModel.uiState.value
        )

        val editedMovement = sampleMovementWithWeight.copy(
            name = "edited movement",
            weight = 101f,
        )
        viewModel.editMovement(
            movement = editedMovement,
        )

        assertEquals(
            MovementListUiState.Success(
                movements = listOf(
                    editedMovement,
                ),
                weightUnit = KILOGRAMS,
                isAnalyticsEnabled = false,
            ),
            viewModel.uiState.value
        )

        assertEquals(1, analyticsHelper.numberOfEvents())

        assertTrue(
            analyticsHelper.hasLogged(
                AnalyticsEvent(
                    type = "edit_movement",
                    extras = createMovementParams(editedMovement),
                ),
            ),
        )
    }

    private val sampleMovementWithoutWeight = Movement(
        id = 5,
        name = "added movement without weight",
        weight = null,
    )

    private val sampleMovementWithWeight = Movement(
        id = 17,
        name = "added movement",
        weight = 55f,
    )
}