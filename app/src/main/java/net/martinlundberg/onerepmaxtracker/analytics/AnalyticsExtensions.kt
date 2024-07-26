package net.martinlundberg.onerepmaxtracker.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent.Param
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent.ParamKeys
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEvent.Types
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.ui.model.MovementUiModel
import net.martinlundberg.onerepmaxtracker.ui.model.ResultUiModel
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit

// General

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

// Movement List Screen

fun AnalyticsHelper.logMovementList_MovementClick(movement: MovementUiModel) {
    logEvent("movement_list_movement_click", createMovementParams(movement))
}

fun AnalyticsHelper.logMovementList_MovementLongClick(movement: MovementUiModel) {
    logEvent("movement_list_movement_long_click", createMovementParams(movement))
}

fun AnalyticsHelper.logMovementList_AddMovementButtonClick() {
    logEvent("movement_list_add_movement_button_click")
}

// Movement Detail Screen

fun AnalyticsHelper.logMovementDetail_NavBackClick() {
    logEvent("movement_detail_nav_back_click")
}

fun AnalyticsHelper.logMovementDetail_ResultClick(result: ResultUiModel) {
    logEvent("movement_detail_result_click", createResultParams(result))
}

fun AnalyticsHelper.logMovementDetail_EditButtonClick(movementId: Long, movementName: String) {
    logEvent(
        "movement_detail_edit_button_click", listOf(
            Param(key = "movement_id", value = movementId.toString()),
            Param(key = "movement_name", value = movementName),
        )
    )
}

fun AnalyticsHelper.logMovementDetail_AddButtonClick(movementId: Long, movementName: String) {
    logEvent(
        "movement_detail_add_button_click", listOf(
            Param(key = "movement_id", value = movementId.toString()),
            Param(key = "movement_name", value = movementName),
        )
    )
}

// Result Detail Screen

fun AnalyticsHelper.logResultDetail_NavBackClick() {
    logEvent("result_detail_nav_back_click")
}

fun AnalyticsHelper.logResultDetail_AddResultClick(result: ResultUiModel) {
    logEvent("result_detail_add_result_click", createResultParams(result))
}

fun AnalyticsHelper.logResultDetail_EditResultClick(result: ResultUiModel) {
    logEvent("result_detail_edit_result_click", createResultParams(result))
}

// Movement List Drop Down Menu

fun AnalyticsHelper.logMovementListDropDownMenu_EditClick(movement: MovementUiModel) {
    logEvent("movement_list_drop_down_menu_edit_click", createMovementParams(movement))
}

fun AnalyticsHelper.logMovementListDropDownMenu_DeleteClick(movement: MovementUiModel) {
    logEvent("movement_list_drop_down_menu_delete_click", createMovementParams(movement))
}

// Add Movement Dialog

fun AnalyticsHelper.logAddMovementDialog_CancelClick(movement: MovementUiModel) {
    logEvent("add_movement_dialog_cancel_click", createMovementParams(movement))
}

fun AnalyticsHelper.logAddMovementDialog_Dismissed(movement: MovementUiModel) {
    logEvent("add_movement_dialog_dismissed", createMovementParams(movement))
}

fun AnalyticsHelper.logAddMovementDialog_ConfirmClick(movement: MovementUiModel) {
    logEvent("add_movement_dialog_confirm_click", createMovementParams(movement))
}

// Edit Movement Dialog

fun AnalyticsHelper.logEditMovementDialog_CancelClick(movement: MovementUiModel) {
    logEvent("edit_movement_dialog_cancel_click", createMovementParams(movement))
}

fun AnalyticsHelper.logEditMovementDialog_Dismissed(movement: MovementUiModel) {
    logEvent("edit_movement_dialog_dismissed", createMovementParams(movement))
}

fun AnalyticsHelper.logEditMovementDialog_ConfirmClick(movement: MovementUiModel) {
    logEvent("edit_movement_dialog_confirm_click", createMovementParams(movement))
}

fun AnalyticsHelper.logEditMovementDialog_DeleteMovementClick(movement: MovementUiModel) {
    logEvent("edit_movement_dialog_delete_movement_click", createMovementParams(movement))
}

// Delete Movement Confirm Dialog

fun AnalyticsHelper.logDeleteMovementConfirmDialog_ConfirmClick(movementId: Long) {
    logEvent(
        "delete_movement_confirm_dialog_confirm_click", listOf(
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

// Add Result Dialog

fun AnalyticsHelper.logAddResultDialog_CancelClick(result: Result) {
    logEvent("add_result_dialog_cancel_click", createResultParams(result))
}

fun AnalyticsHelper.logAddResultDialog_ConfirmClick(result: Result) {
    logEvent("add_result_dialog_confirm_click", createResultParams(result))
}

fun AnalyticsHelper.logAddResultDialog_Dismissed(result: Result) {
    logEvent("add_result_dialog_dismissed", createResultParams(result))
}

// Edit Result Dialog

fun AnalyticsHelper.logEditResultDialog_CancelClick(result: Result) {
    logEvent("edit_result_dialog_cancel_click", createResultParams(result))
}

fun AnalyticsHelper.logEditResultDialog_ConfirmClick(result: Result) {
    logEvent("edit_result_dialog_confirm_click", createResultParams(result))
}

fun AnalyticsHelper.logEditResultDialog_DeleteClick(result: ResultUiModel) {
    logEvent("edit_result_dialog_delete_click", createResultParams(result))
}

fun AnalyticsHelper.logEditResultDialog_Dismissed(result: Result) {
    logEvent("edit_result_dialog_dismissed", createResultParams(result))
}

// Delete Result Confirm Dialog

fun AnalyticsHelper.logDeleteResultConfirmDialog_ConfirmClick(resultId: Long) {
    logEvent(
        "delete_result_confirm_dialog_confirm_click", listOf(
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

// ViewModel & Repository events

fun AnalyticsHelper.logAddMovement(movement: Movement) {
    logEvent("add_movement", createMovementParams(movement))
}

fun AnalyticsHelper.logEditMovement(movement: Movement) {
    logEvent("edit_movement", createMovementParams(movement))
}

fun AnalyticsHelper.logDeleteMovement(movementId: Long) {
    logEvent(
        "edit_movement", listOf(
            Param(key = "movement_id", value = movementId.toString()),
        )
    )
}

fun AnalyticsHelper.logAddResult(result: Result) {
    logEvent("add_result", createResultParams(result))
}

fun AnalyticsHelper.logEditResult(result: Result) {
    logEvent("edit_result", createResultParams(result))
}

fun AnalyticsHelper.logDeleteResult(resultId: Long) {
    logEvent(
        "delete_result", listOf(
            Param(key = "result_id", value = resultId.toString()),
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

private fun createMovementParams(movement: MovementUiModel): List<Param> {
    val paramKeyId = "movement_id"
    val paramKeyName = "movement_name"
    val paramKeyWeight = "movement_weight"
    val extras = listOf(
        Param(key = paramKeyId, value = movement.id.toString()),
        Param(key = paramKeyName, value = movement.name),
        Param(key = paramKeyWeight, value = movement.weight ?: "-"),
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

private fun createResultParams(result: ResultUiModel): List<Param> {
    val paramKeyId = "result_id"
    val paramKeyName = "movement_id"
    val paramKeyWeight = "result_weight"
    val paramKeyDateTime = "result_date_time"
    val extras = listOf(
        Param(key = paramKeyId, value = result.id.toString()),
        Param(key = paramKeyName, value = result.movementId.toString()),
        Param(key = paramKeyWeight, value = result.weight.toString()),
        Param(key = paramKeyDateTime, value = result.toString()),
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
