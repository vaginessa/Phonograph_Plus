/*
 * Copyright (c) 2022 chr_56
 */

package player.phonograph.ui.modules.settings

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.Maxr1998.modernpreferences.Preference
import de.Maxr1998.modernpreferences.PreferencesAdapter
import lib.phonograph.activity.ToolbarActivity
import mt.tint.setActivityToolbarColorAuto
import player.phonograph.databinding.ActivitySettingBinding
import player.phonograph.misc.menuProvider
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.view.LayoutInflater

class SettingsActivity : ToolbarActivity() {

    private lateinit var preferencesAdapter: PreferencesAdapter
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        registerSettingActivityResultLauncher(this)
        setupToolbar()
        setupPreference()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setActivityToolbarColorAuto(binding.toolbar)
        addMenuProvider(menuProvider { setupSettingMenu(it, this) })
    }

    private fun setupPreference() {
        Preference.Config.dialogBuilderFactory = { context ->
            MaterialAlertDialogBuilder(context)
        }
        preferencesAdapter = PreferencesAdapter(setupPreferenceScreen(this))
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = preferencesAdapter
        }
    }
}