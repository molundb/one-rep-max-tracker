package net.martinlundberg.onerepmaxtracker.feature.movementlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.martinlundberg.onerepmaxtracker.data.DataStorePreferences
import javax.inject.Inject

interface LatestOrBestResultsInMovementListScreenService {
    val latestOrBestResults: Flow<LatestOrBestResults>
    suspend fun setLatestOrBestResults(latestOrBestResults: LatestOrBestResults)
}

class LatestOrBestResultsInMovementListScreenServiceImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
) : LatestOrBestResultsInMovementListScreenService {

    override val latestOrBestResults =
        dataStorePreferences.showBestResultsInMovementListScreenFlow.map {
            if (it) LatestOrBestResults.BEST else LatestOrBestResults.LATEST
        }

    override suspend fun setLatestOrBestResults(latestOrBestResults: LatestOrBestResults) =
        dataStorePreferences.storeShowBestResultsInMovementListScreen(latestOrBestResults == LatestOrBestResults.BEST)

}

enum class LatestOrBestResults {
    LATEST,
    BEST,
}