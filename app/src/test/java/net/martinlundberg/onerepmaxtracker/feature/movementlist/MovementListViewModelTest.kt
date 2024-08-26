@file:OptIn(ExperimentalCoroutinesApi::class)

package net.martinlundberg.onerepmaxtracker.feature.movementlist

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import net.martinlundberg.onerepmaxtracker.MainDispatcherRule
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent
import net.martinlundberg.onerepmaxtracker.analytics.createMovementParams
import net.martinlundberg.onerepmaxtracker.analytics.createResultParams
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.fakes.FakeAnalyticsService
import net.martinlundberg.onerepmaxtracker.fakes.FakeClockService
import net.martinlundberg.onerepmaxtracker.fakes.FakeMovementsRepository
import net.martinlundberg.onerepmaxtracker.fakes.FakeResultRepository
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.KILOGRAMS
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
    private val analyticsService = FakeAnalyticsService()

    private lateinit var viewModel: MovementListViewModel

    @Before
    fun setUp() {
        viewModel = MovementListViewModel(
            movementsRepository,
            resultRepository,
            clockService,
            analyticsService,
        )
    }

    @Test
    fun stateIsInitiallyLoading() {
        assertEquals(MovementListUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun whenAddMovementWithoutWeight_thenMovementIsAddedAndTracked() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        assertEquals(
            MovementListUiState.Success(
                movements = emptyList(),
                weightUnit = KILOGRAMS,
                isAnalyticsEnabled = false,
                showBestResults = true,
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
                showBestResults = true,
            ),
            viewModel.uiState.value
        )

        assertEquals(1, analyticsService.numberOfEvents())

        assertTrue(
            analyticsService.hasLogged(
                AnalyticsEvent(
                    type = "add_movement",
                    extras = createMovementParams(sampleMovementWithoutWeight),
                ),
            ),
        )

        collectJob.cancel()
    }

    @Test
    fun whenAddMovementWithWeight_thenMovementAndResultAreAddedAndTracked() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        assertEquals(
            MovementListUiState.Success(
                movements = emptyList(),
                weightUnit = KILOGRAMS,
                isAnalyticsEnabled = false,
                showBestResults = true,
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
                showBestResults = true,
            ),
            viewModel.uiState.value
        )

        assertEquals(2, analyticsService.numberOfEvents())

        assertTrue(
            analyticsService.hasLogged(
                AnalyticsEvent(
                    type = "add_movement",
                    extras = createMovementParams(sampleMovementWithWeight),
                ),
            ),
        )

        assertTrue(
            analyticsService.hasLogged(
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

        collectJob.cancel()
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