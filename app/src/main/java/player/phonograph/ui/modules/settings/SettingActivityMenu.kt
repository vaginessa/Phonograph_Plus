/*
 * Copyright (c) 2022 chr_56
 */

package player.phonograph.ui.modules.settings

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.github.chr56.android.menu_dsl.attach
import com.github.chr56.android.menu_dsl.menuItem
import player.phonograph.App
import player.phonograph.R
import player.phonograph.misc.OpenDocumentContract
import player.phonograph.provider.DatabaseManger
import player.phonograph.settings.SettingManager
import player.phonograph.util.CoroutineUtil
import player.phonograph.util.TimeUtil
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Process
import android.view.Menu
import android.view.MenuItem
import kotlin.system.exitProcess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun setupSettingMenu(menu: Menu, context: Context) {
    attach(from = menu, context = context) {
        menuItem {
            itemId = R.id.action_export_data
            title = "${context.getString(R.string.export_)}${context.getString(R.string.databases)}"
            showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
            onClick {
                createAction = { uri -> exportDatabase(uri) }
                createLauncher.launch("phonograph_plus_databases_${TimeUtil.currentDateTime()}.zip")
                true
            }
        }
        menuItem {
            itemId = R.id.action_import_data
            title = "${context.getString(R.string.import_)}${context.getString(R.string.databases)}"
            showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
            onClick {
                openAction = { uri -> importDatabase(uri) }
                openLauncher.launch(OpenDocumentContract.Cfg(null, arrayOf("application/zip")))
                true
            }
        }
        menuItem {
            itemId = R.id.action_export_preferences
            title =
                "${context.getString(R.string.export_)}${context.getString(R.string.preferences)}"
            showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
            onClick {
                createAction = { uri -> exportSetting(uri) }
                createLauncher.launch("phonograph_plus_settings_${TimeUtil.currentDateTime()}.json")
                true
            }
        }
        menuItem {
            itemId = R.id.action_import_preferences
            title =
                "${context.getString(R.string.import_)}${context.getString(R.string.preferences)}"
            showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
            onClick {
                openAction = { uri -> importSetting(uri) }
                openLauncher.launch(OpenDocumentContract.Cfg(null, arrayOf("application/json")))
                true
            }
        }
        menuItem {
            itemId = R.id.action_clear_all_preference
            title = context.getString(R.string.clear_all_preference)
            showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
            onClick {
                MaterialDialog(context).show {
                    title(R.string.clear_all_preference)
                    message(R.string.clear_all_preference_msg)
                    negativeButton(android.R.string.cancel)
                    positiveButton(R.string.clear_all_preference) {
                        SettingManager(context.applicationContext).clearAllPreference()
                        Handler().postDelayed({
                                                  Process.killProcess(Process.myPid())
                                                  exitProcess(1)
                                              }, 4000)
                    }
                    cancelOnTouchOutside(true)
                    getActionButton(WhichButton.POSITIVE)
                        .updateTextColor(context.getColor(R.color.md_red_A700))
                }
                true
            }
        }
    }
}

private lateinit var createAction: (Uri) -> Boolean
private lateinit var openAction: (Uri) -> Boolean

private lateinit var createLauncher: ActivityResultLauncher<String>
private lateinit var openLauncher: ActivityResultLauncher<OpenDocumentContract.Cfg>

internal fun registerSettingActivityResultLauncher(activity: SettingsActivity) {
    createLauncher =
        activity.registerForActivityResult(ActivityResultContracts.CreateDocument()) {
            it?.let { uri ->
                CoroutineScope(Dispatchers.IO).launch {
                    createAction(uri).andReport()
                }
            }
        }
    openLauncher =
        activity.registerForActivityResult(OpenDocumentContract()) {
            it?.let { uri ->
                CoroutineScope(Dispatchers.IO).launch {
                    openAction(uri).andReport()
                }
            }
        }
}

private fun exportDatabase(uri: Uri): Boolean =
    DatabaseManger(App.instance).exportDatabases(uri)

private fun importDatabase(uri: Uri): Boolean =
    DatabaseManger(App.instance).importDatabases(uri)

private fun exportSetting(uri: Uri): Boolean =
    SettingManager(App.instance).exportSettings(uri)

private fun importSetting(uri: Uri): Boolean =
    SettingManager(App.instance).importSetting(uri)

private suspend fun Boolean.andReport() {
    CoroutineUtil.coroutineToast(App.instance, if (this) R.string.success else R.string.failed)
}