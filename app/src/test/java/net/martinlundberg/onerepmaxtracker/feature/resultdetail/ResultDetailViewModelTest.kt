package net.martinlundberg.onerepmaxtracker.feature.resultdetail

import kotlinx.coroutines.test.runTest
import net.martinlundberg.onerepmaxtracker.MainDispatcherRule
import net.martinlundberg.onerepmaxtracker.data.model.Percentage
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.fakes.FakeAnalyticsService
import net.martinlundberg.onerepmaxtracker.fakes.FakeClockService
import net.martinlundberg.onerepmaxtracker.fakes.FakeResultRepository
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.KILOGRAMS
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.OffsetDateTime
import java.time.ZoneOffset

@RunWith(RobolectricTestRunner::class)
class ResultDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val resultRepository = FakeResultRepository()
    private val clockService = FakeClockService()
    private val analyticsService = FakeAnalyticsService()

    private lateinit var viewModel: ResultDetailViewModel

    @Before
    fun setUp() {
        viewModel = ResultDetailViewModel(
            resultRepository = resultRepository,
            clockService = clockService,
            analyticsService = analyticsService,
        )
    }

    @Test
    fun stateIsInitiallyLoading() {
        assertEquals(
            ResultDetailUiState.Loading,
            viewModel.uiState.value,
        )
    }

    @Test
    fun givenNoResultWithId_whenGetResult_stateIsNoResultDetail() = runTest {
        viewModel.getResult(0)

        assertEquals(
            ResultDetailUiState.NoResultDetail,
            viewModel.uiState.value,
        )
    }

    @Test
    fun givenResultWithId_whenGetResult_stateIsSuccess() = runTest {
        resultRepository.setResult(sampleResult, KILOGRAMS)

        viewModel.getResult(1)

        assertEquals(
            ResultDetailUiState.Success(
                result = sampleResult,
                percentagesOf1RM = listOf(
                    Percentage(100, 100),
                    Percentage(90, 90),
                    Percentage(80, 80),
                    Percentage(70, 70),
                    Percentage(60, 60),
                    Percentage(50, 50),
                    Percentage(40, 40),
                ),
                KILOGRAMS,
            ),
            viewModel.uiState.value,
        )
    }

    private val sampleResult = Result(
        id = 1,
        movementId = 3,
        weight = 100f,
        offsetDateTime = OffsetDateTime.of(2023, 9, 20, 0, 0, 0, 0, ZoneOffset.UTC),
        comment = "This is a nice comment",
    )
}