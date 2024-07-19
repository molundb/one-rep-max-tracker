package net.martinlundberg.onerepmaxtracker.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent.Param
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent.ParamKeys
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent.Types
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit

fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        AnalyticsEvent(
            type = Types.SCREEN_VIEW,
            extras = listOf(
                Param(ParamKeys.SCREEN_NAME, screenName),
            ),
        ),
    )
}

fun AnalyticsHelper.logMovementList_MovementClick(movement: Movement) {
    val type = "movement_click"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logMovementList_MovementLongClick(movement: Movement) {
    val type = "movement_long_click"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logMovementList_EditMovementClick(movement: Movement) {
    val type = "movement_edit_click"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logMovementList_DeleteMovementClick(movement: Movement) {
    val type = "movement_delete_click"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logDeleteMovementConfirmDialog_DeleteClick(movementId: Long) {
    val type = "delete_movement_confirm_dialog_delete_click"
    val paramKeyId = "movement_id"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = movementId.toString()),
            ),
        )
    )
}

fun AnalyticsHelper.logDeleteMovementConfirmDialog_CancelClick(movementId: Long) {
    val type = "delete_movement_confirm_dialog_cancel_click"
    val paramKeyId = "movement_id"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = movementId.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logDeleteMovementConfirmDialog_Dismissed(movementId: Long) {
    val type = "delete_movement_confirm_dialog_dismissed"
    val paramKeyId = "movement_id"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = movementId.toString()),
            )
        )
    )
}

// TODO: Track Text Field focused?

fun AnalyticsHelper.logAddMovement(movement: Movement) {
    val type = "add_movement"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logEditMovement(movement: Movement) {
    val type = "edit_movement"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras
        )
    )
}

fun AnalyticsHelper.logAddMovementDialog_CancelClick(movement: Movement) {
    val type = "add_movement_dialog_cancel_click"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logAddMovementDialog_Dismissed(movement: Movement) {
    val type = "add_movement_dialog_dismissed"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logAddMovementDialog_ConfirmClick(movement: Movement) {
    val type = "add_movement_dialog_confirm_click"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logEditMovementDialog_CancelClick(movement: Movement) {
    val type = "edit_movement_dialog_cancel_click"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logEditMovementDialog_Dismissed(movement: Movement) {
    val type = "edit_movement_dialog_dismissed"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logEditMovementDialog_ConfirmClick(movement: Movement) {
    val type = "edit_movement_dialog_confirm_click"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logEditMovementDialog_DeleteMovementClick(movement: Movement) {
    val type = "edit_movement_dialog_delete_movement_click"
    val extras = createMovementParams(movement)
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = extras,
        )
    )
}

fun AnalyticsHelper.logAddMovementClick() {
    val type = "add_movement_click"
    logEvent(
        AnalyticsEvent(
            type = type,
        )
    )
}

fun AnalyticsHelper.logWeightUnitToggled(weightUnit: WeightUnit) {
    val type = "weight_unit_toggled"
    val paramKey = "weight_unit"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKey, value = weightUnit.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logAnalyticsEnabledToggled(isEnabled: Boolean) {
    val type = "analytics_enabled_toggled"
    val paramKey = "analytics_enabled"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKey, value = isEnabled.toString()),
            )
        )
    )
}

// MovementDetailScreen

fun AnalyticsHelper.logMovementDetail_NavBackClick() {
    val type = "nav_back_click"
    logEvent(
        AnalyticsEvent(
            type = type,
        )
    )
}

fun AnalyticsHelper.logMovementDetail_ResultClick(result: Result) {
    val type = "result_click"
    val paramKeyId = "result_id"
    val paramKeyName = "movement_id"
    val paramKeyWeight = "result_weight"
    val paramKeyDateTime = "result_date_time"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = result.id.toString()),
                Param(key = paramKeyName, value = result.movementId.toString()),
                Param(key = paramKeyWeight, value = result.weight.toString()),
                Param(key = paramKeyDateTime, value = result.offsetDateTime.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logAddResultDialog_CancelClick(result: Result) {
    val type = "add_result_dialog_cancel_click"
    val paramKeyId = "result_id"
    val paramKeyName = "movement_id"
    val paramKeyWeight = "result_weight"
    val paramKeyDateTime = "result_date_time"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = result.id.toString()),
                Param(key = paramKeyName, value = result.movementId.toString()),
                Param(key = paramKeyWeight, value = result.weight.toString()),
                Param(key = paramKeyDateTime, value = result.offsetDateTime.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logAddResultDialog_ConfirmClick(result: Result) {
    val type = "add_result_dialog_confirm_click"
    val paramKeyId = "result_id"
    val paramKeyName = "movement_id"
    val paramKeyWeight = "result_weight"
    val paramKeyDateTime = "result_date_time"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = result.id.toString()),
                Param(key = paramKeyName, value = result.movementId.toString()),
                Param(key = paramKeyWeight, value = result.weight.toString()),
                Param(key = paramKeyDateTime, value = result.offsetDateTime.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logAddResultDialog_Dismissed(result: Result) {
    val type = "add_result_dialog_dismissed"
    val paramKeyId = "result_id"
    val paramKeyName = "movement_id"
    val paramKeyWeight = "result_weight"
    val paramKeyDateTime = "result_date_time"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = result.id.toString()),
                Param(key = paramKeyName, value = result.movementId.toString()),
                Param(key = paramKeyWeight, value = result.weight.toString()),
                Param(key = paramKeyDateTime, value = result.offsetDateTime.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logAddResultDialog_DeleteResultClick(resultId: Long) {
    val type = "add_result_dialog_delete_result_click"
    val paramKeyId = "result_id"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = resultId.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logEditResultDialog_CancelClick(result: Result) {
    val type = "edit_result_dialog_cancel_click"
    val paramKeyId = "result_id"
    val paramKeyName = "movement_id"
    val paramKeyWeight = "result_weight"
    val paramKeyDateTime = "result_date_time"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = result.id.toString()),
                Param(key = paramKeyName, value = result.movementId.toString()),
                Param(key = paramKeyWeight, value = result.weight.toString()),
                Param(key = paramKeyDateTime, value = result.offsetDateTime.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logEditResultDialog_ConfirmClick(result: Result) {
    val type = "edit_result_dialog_confirm_click"
    val paramKeyId = "result_id"
    val paramKeyName = "movement_id"
    val paramKeyWeight = "result_weight"
    val paramKeyDateTime = "result_date_time"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = result.id.toString()),
                Param(key = paramKeyName, value = result.movementId.toString()),
                Param(key = paramKeyWeight, value = result.weight.toString()),
                Param(key = paramKeyDateTime, value = result.offsetDateTime.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logEditResultDialog_Dismissed(result: Result) {
    val type = "edit_result_dialog_dismissed"
    val paramKeyId = "result_id"
    val paramKeyName = "movement_id"
    val paramKeyWeight = "result_weight"
    val paramKeyDateTime = "result_date_time"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = result.id.toString()),
                Param(key = paramKeyName, value = result.movementId.toString()),
                Param(key = paramKeyWeight, value = result.weight.toString()),
                Param(key = paramKeyDateTime, value = result.offsetDateTime.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logEditResultDialog_DeleteResultClick(resultId: Long) {
    val type = "edit_result_dialog_delete_result_click"
    val paramKeyId = "result_id"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = resultId.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logDeleteResultConfirmDialog_CancelClick(resultId: Long) {
    val type = "delete_result_confirm_dialog_cancel_click"
    val paramKeyId = "result_id"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = resultId.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logDeleteResultConfirmDialog_Dismissed(resultId: Long) {
    val type = "delete_result_confirm_dialog_dismissed"
    val paramKeyId = "result_id"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = resultId.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logDeleteResultConfirmDialog_ConfirmClick(resultId: Long) {
    val type = "delete_result_confirm_dialog_confirm_click"
    val paramKeyId = "result_id"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = resultId.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logDeleteResult(resultId: Long) {
    val type = "delete_result"
    val paramKeyId = "result_id"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = resultId.toString()),
            )
        )
    )
}

fun AnalyticsHelper.logMovementDetail_DeleteMovementClick(movementId: Long, movementName: String) {
    val type = "delete_movement_click"
    val paramKeyId = "movement_id"
    val paramKeyName = "movement_name"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = movementId.toString()),
                Param(key = paramKeyName, value = movementName),
            )
        )
    )
}

fun AnalyticsHelper.logMovementDetail_AddResultClick(movementId: Long, movementName: String) {
    val type = "add_result_click"
    val paramKeyId = "movement_id"
    val paramKeyName = "movement_name"
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = listOf(
                Param(key = paramKeyId, value = movementId.toString()),
                Param(key = paramKeyName, value = movementName),
            )
        )
    )
}

private fun createMovementParams(movement: Movement): List<Param> {
    val paramKeyId = "movement_id"
    val paramKeyName = "movement_name"
    val paramKeyWeight = "movement_weight"
    val extras = listOf(
        Param(key = paramKeyId, value = movement.id.toString()),
        Param(key = paramKeyName, value = movement.name),
        Param(key = paramKeyWeight, value = movement.weight?.toString() ?: "-"),
    )
    return extras
}

/**
 * A side-effect which records a screen view event.
 */
@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose {}
}
