package net.martinlundberg.onerepmaxtracker.feature.movementdetail

import kotlinx.coroutines.test.runTest
import net.martinlundberg.onerepmaxtracker.MainDispatcherRule
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.fakes.FakeAnalyticsHelper
import net.martinlundberg.onerepmaxtracker.fakes.FakeClockService
import net.martinlundberg.onerepmaxtracker.fakes.FakeMovementsRepository
import net.martinlundberg.onerepmaxtracker.fakes.FakeResultRepository
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.KILOGRAMS
import net.martinlundberg.onerepmaxtracker.util.millisToOffsetDateTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@RunWith(RobolectricTestRunner::class)
class MovementDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val resultRepository = FakeResultRepository()
    private val movementsRepository = FakeMovementsRepository(resultRepository)
    private val clockService = FakeClockService()
    private val analyticsHelper = FakeAnalyticsHelper()

    private lateinit var viewModel: MovementDetailViewModel

    @Before
    fun setUp() {
        viewModel = MovementDetailViewModel(
            movementsRepository,
            resultRepository,
            clockService,
            analyticsHelper,
        )
    }

    @Test
    fun stateIsInitiallyLoading() {
        assertEquals(
            MovementDetailUiState.Loading(),
            viewModel.uiState.value,
        )
    }

    @Test
    fun givenNoMovementWithId_stateIsNoMovementDetail() = runTest {
        viewModel.getMovementInfo(0)

        assertEquals(
            MovementDetailUiState.NoMovementDetail(),
            viewModel.uiState.value,
        )
    }

    @Test
    fun givenMovementWithResults_stateIsSuccess() = runTest {
        resultRepository.setMovementDetail(sampleMovementDetail)

        viewModel.getMovementInfo(1)

        assertEquals(
            MovementDetailUiState.Success(
                sampleMovementDetail,
                KILOGRAMS,
                clockService.getCurrentTimeMillis().millisToOffsetDateTime(ZoneId.systemDefault())
            ),
            viewModel.uiState.value,
        )
    }

    @Test
    fun givenMovementWithNoResults_stateIsNoMovementDetail() = runTest {
        resultRepository.setMovementDetail(sampleMovementDetailWithNoResults)

        viewModel.getMovementInfo(0)

        assertEquals(
            MovementDetailUiState.Success(
                sampleMovementDetailWithNoResults,
                KILOGRAMS,
                clockService.getCurrentTimeMillis().millisToOffsetDateTime(ZoneId.systemDefault())
            ),
            viewModel.uiState.value,
        )
    }

    private val sampleMovementDetail = MovementDetail(
        movementName = "Movement name",
        results = listOf(
            Result(
                id = 1,
                movementId = 3,
                weight = 55f,
                offsetDateTime = OffsetDateTime.of(2023, 9, 20, 0, 0, 0, 0, ZoneOffset.UTC),
                comment = "This is a nice comment",
            )
        ),
    )

    private val sampleMovementDetailWithNoResults = MovementDetail(
        movementName = "Movement name",
        results = emptyList(),
    )
}