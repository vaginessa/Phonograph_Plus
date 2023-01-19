/*
 * Copyright (c) 2022~2023 chr_56
 */

package player.phonograph.ui.compose.tag

import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.customView
import com.vanpra.composematerialdialogs.title
import player.phonograph.App
import player.phonograph.R
import player.phonograph.ui.compose.ColorTools
import player.phonograph.ui.compose.components.CoverImage
import player.phonograph.util.SongDetailUtil
import player.phonograph.util.tageditor.applyEdit
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import android.app.Activity
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File

@Composable
internal fun TagBrowserScreen(viewModel: TagBrowserScreenViewModel, context: Context?) {
    val wrapper by viewModel.artwork.collectAsState()
    val paletteColor =
        ColorTools.makeSureContrastWith(MaterialTheme.colors.surface) {
            if (wrapper != null) {
                Color(wrapper!!.paletteColor)
            } else {
                MaterialTheme.colors.primaryVariant
            }
        }

    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .fillMaxSize()
    ) {
        if (wrapper != null || viewModel is TagEditorScreenViewModel) {
            CoverImage(
                bitmap = wrapper?.bitmap,
                backgroundColor = paletteColor,
                modifier = Modifier.clickable {
                    viewModel.coverImageDetailDialogState.show()
                }
            )
        }
        InfoTable(viewModel.infoTableState)
    }
    CoverImageDetailDialog(
        state = viewModel.coverImageDetailDialogState,
        artwork = wrapper,
        onSave = { viewModel.saveArtwork(context!!) },
        onDelete = { (viewModel as? TagEditorScreenViewModel)?.deleteArtwork() },
        onUpdate = { (viewModel as? TagEditorScreenViewModel)?.replaceArtwork(context!!) },
        editMode = viewModel is TagEditorScreenViewModel
    )
    // edit mode
    if (viewModel is TagEditorScreenViewModel) {
        SaveConfirmationDialog(viewModel, context)
        ExitWithoutSavingDialog(viewModel, context as? Activity)
    }
}

@Composable
internal fun CoverImageDetailDialog(
    state: MaterialDialogState,
    artwork: SongDetailUtil.BitmapPaletteWrapper?,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit,
    editMode: Boolean
) = MaterialDialog(
    dialogState = state,
    buttons = {
        positiveButton(res = android.R.string.ok) { state.hide() }
    }
) {
    title(res = R.string.label_details)
    Column(
        modifier = Modifier
            .padding(bottom = 28.dp, start = 24.dp, end = 24.dp)
            .wrapContentWidth()
    ) {
        if (artwork != null) {
            MenuItem(textRes = R.string.save, onSave)
            if (editMode) {
                MenuItem(textRes = R.string.remove_cover, onDelete)
            }
        }
        if (editMode) {
            MenuItem(textRes = R.string.update_image, onUpdate)
        }
    }
}

@Composable
private fun MenuItem(@StringRes textRes: Int, onClick: () -> Unit) =
    Text(
        text = stringResource(textRes),
        color = MaterialTheme.colors.primary,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.button,
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(32.dp)
            .clickable(onClick = onClick),
    )

@Composable
internal fun ExitWithoutSavingDialog(model: TagEditorScreenViewModel, activity: Activity?) {
    val dismiss = { model.exitWithoutSavingDialogState.hide() }
    MaterialDialog(
        dialogState = model.exitWithoutSavingDialogState,
        elevation = 0.dp,
        autoDismiss = false,
        buttons = {
            positiveButton(res = android.R.string.cancel, onClick = dismiss)
            button(res = android.R.string.ok) {
                dismiss()
                activity?.let { model.requestExit(it) }
            }
        }
    ) {
        title(res = R.string.exit_without_saving)
    }
}

@Composable
internal fun SaveConfirmationDialog(model: TagEditorScreenViewModel, context: Context?) {
    val dismiss = { model.saveConfirmationDialogState.hide() }
    val save = {
        dismiss()
        saveImpl(model, context)
    }
    MaterialDialog(
        dialogState = model.saveConfirmationDialogState,
        elevation = 0.dp,
        autoDismiss = false,
        buttons = {
            button(res = R.string.save, onClick = save)
            button(res = android.R.string.cancel, onClick = dismiss)
        }
    ) {
        title(res = R.string.save)
        customView {
            DiffScreen(model)
        }
    }
}

private fun saveImpl(model: TagEditorScreenViewModel, context: Context?) =
    applyEdit(
        CoroutineScope(Dispatchers.Unconfined),
        context ?: App.instance,
        File(model.song.data),
        model.infoTableState.allEditRequests,
        model.needDeleteCover,
        model.needReplaceCover,
        model.newCover
    )
