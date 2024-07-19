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
    logEvent("movement_click", createMovementParams(movement))
}

fun AnalyticsHelper.logMovementList_MovementLongClick(movement: Movement) {
    logEvent("movement_long_click", createMovementParams(movement))
}

fun AnalyticsHelper.logMovementList_EditMovementClick(movement: Movement) {
    logEvent("movement_edit_click", createMovementParams(movement))
}

fun AnalyticsHelper.logMovementList_DeleteMovementClick(movement: Movement) {
    logEvent("movement_delete_click", createMovementParams(movement))
}

fun AnalyticsHelper.logDeleteMovementConfirmDialog_DeleteClick(movementId: Long) {
    logEvent(
        "delete_movement_confirm_dialog_delete_click", listOf(
            Param(key = "movement_id", value = movementId.toString()),
        )
    )
}

fun AnalyticsHelper.logDeleteMovementConfirmDialog_CancelClick(movementId: Long) {
    logEvent(
        "delete_movement_confirm_dialog_cancel_click", listOf(
            Param(key = "movement_id", value = movementId.toString()),
        )
    )
}

fun AnalyticsHelper.logDeleteMovementConfirmDialog_Dismissed(movementId: Long) {
    logEvent(
        "delete_movement_confirm_dialog_dismissed", listOf(
            Param(key = "movement_id", value = movementId.toString()),
        )
    )
}

// TODO: Track Text Field focused?

fun AnalyticsHelper.logAddMovement(movement: Movement) {
    logEvent("add_movement", createMovementParams(movement))
}

fun AnalyticsHelper.logEditMovement(movement: Movement) {
    logEvent("edit_movement", createMovementParams(movement))
}

fun AnalyticsHelper.logAddMovementDialog_CancelClick(movement: Movement) {
    logEvent("add_movement_dialog_cancel_click", createMovementParams(movement))
}

fun AnalyticsHelper.logAddMovementDialog_Dismissed(movement: Movement) {
    logEvent("add_movement_dialog_dismissed", createMovementParams(movement))
}

fun AnalyticsHelper.logAddMovementDialog_ConfirmClick(movement: Movement) {
    logEvent("add_movement_dialog_confirm_click", createMovementParams(movement))
}

fun AnalyticsHelper.logEditMovementDialog_CancelClick(movement: Movement) {
    logEvent("edit_movement_dialog_cancel_click", createMovementParams(movement))
}

fun AnalyticsHelper.logEditMovementDialog_Dismissed(movement: Movement) {
    logEvent("edit_movement_dialog_dismissed", createMovementParams(movement))
}

fun AnalyticsHelper.logEditMovementDialog_ConfirmClick(movement: Movement) {
    logEvent("edit_movement_dialog_confirm_click", createMovementParams(movement))
}

fun AnalyticsHelper.logEditMovementDialog_DeleteMovementClick(movement: Movement) {
    logEvent("edit_movement_dialog_delete_movement_click", createMovementParams(movement))
}

fun AnalyticsHelper.logAddMovementClick() {
    logEvent("add_movement_click")
}

fun AnalyticsHelper.logWeightUnitToggled(weightUnit: WeightUnit) {
    logEvent(
        "weight_unit_toggled", listOf(
            Param(key = "weight_unit", value = weightUnit.toString()),
        )
    )
}

fun AnalyticsHelper.logAnalyticsEnabledToggled(isEnabled: Boolean) {
    logEvent(
        "analytics_enabled_toggled", listOf(
            Param(key = "analytics_enabled", value = isEnabled.toString()),
        )
    )
}

// MovementDetailScreen

fun AnalyticsHelper.logMovementDetail_NavBackClick() {
    logEvent("nav_back_click")
}

fun AnalyticsHelper.logMovementDetail_ResultClick(result: Result) {
    logEvent("result_click", createResultParams(result))
}

fun AnalyticsHelper.logAddResultDialog_CancelClick(result: Result) {
    logEvent("add_result_dialog_cancel_click", createResultParams(result))
}

fun AnalyticsHelper.logAddResultDialog_ConfirmClick(result: Result) {
    logEvent("add_result_dialog_confirm_click", createResultParams(result))
}

fun AnalyticsHelper.logAddResultDialog_Dismissed(result: Result) {
    logEvent("add_result_dialog_dismissed", createResultParams(result))
}

fun AnalyticsHelper.logAddResultDialog_DeleteResultClick(resultId: Long) {
    logEvent(
        "add_result_dialog_delete_result_click", listOf(
            Param(key = "result_id", value = resultId.toString()),
        )
    )
}

fun AnalyticsHelper.logEditResultDialog_CancelClick(result: Result) {
    logEvent("edit_result_dialog_cancel_click", createResultParams(result))
}

fun AnalyticsHelper.logEditResultDialog_ConfirmClick(result: Result) {
    logEvent("edit_result_dialog_confirm_click", createResultParams(result))
}

fun AnalyticsHelper.logEditResultDialog_Dismissed(result: Result) {
    logEvent("edit_result_dialog_dismissed", createResultParams(result))
}

fun AnalyticsHelper.logEditResultDialog_DeleteResultClick(resultId: Long) {
    logEvent(
        "edit_result_dialog_delete_result_click", listOf(
            Param(key = "result_id", value = resultId.toString()),
        )
    )
}

fun AnalyticsHelper.logDeleteResultConfirmDialog_CancelClick(resultId: Long) {
    logEvent(
        "delete_result_confirm_dialog_cancel_click", listOf(
            Param(key = "result_id", value = resultId.toString()),
        )
    )
}

fun AnalyticsHelper.logDeleteResultConfirmDialog_Dismissed(resultId: Long) {
    logEvent(
        "delete_result_confirm_dialog_dismissed", listOf(
            Param(key = "result_id", value = resultId.toString()),
        )
    )
}

fun AnalyticsHelper.logDeleteResultConfirmDialog_ConfirmClick(resultId: Long) {
    logEvent(
        "delete_result_confirm_dialog_confirm_click", listOf(
            Param(key = "result_id", value = resultId.toString()),
        )
    )
}

fun AnalyticsHelper.logDeleteResult(resultId: Long) {
    logEvent(
        "delete_result", listOf(
            Param(key = "result_id", value = resultId.toString()),
        )
    )
}

fun AnalyticsHelper.logMovementDetail_EditMovementClick(movementId: Long, movementName: String) {
    logEvent(
        "edit_movement_click", listOf(
            Param(key = "movement_id", value = movementId.toString()),
            Param(key = "movement_name", value = movementName),
        )
    )
}

fun AnalyticsHelper.logMovementDetail_AddResultClick(movementId: Long, movementName: String) {
    logEvent(
        "add_result_click", listOf(
            Param(key = "movement_id", value = movementId.toString()),
            Param(key = "movement_name", value = movementName),
        )
    )
}

private fun AnalyticsHelper.logEvent(type: String, params: List<Param> = emptyList()) {
    logEvent(
        AnalyticsEvent(
            type = type,
            extras = params,
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

private fun createResultParams(result: Result): List<Param> {
    val paramKeyId = "result_id"
    val paramKeyName = "movement_id"
    val paramKeyWeight = "result_weight"
    val paramKeyDateTime = "result_date_time"
    val extras = listOf(
        Param(key = paramKeyId, value = result.id.toString()),
        Param(key = paramKeyName, value = result.movementId.toString()),
        Param(key = paramKeyWeight, value = result.weight.toString()),
        Param(key = paramKeyDateTime, value = result.offsetDateTime.toString()),
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
