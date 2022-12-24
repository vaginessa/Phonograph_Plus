/*
 * Copyright (c) 2022 chr_56
 */

package lib.phonograph.preferencedsl

import de.Maxr1998.modernpreferences.Preference
import de.Maxr1998.modernpreferences.PreferenceScreen
import de.Maxr1998.modernpreferences.PreferencesAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.content.Context

class DialogFragmentPreference(
    key: String,
    var dialog: (() -> DialogFragment),
    var fragmentManager: FragmentManager
) : Preference(key) {
    init {
        persistent = false
    }

    override fun onClick(holder: PreferencesAdapter.ViewHolder) {
        val fm = fragmentManager
        val dialogFragment = dialog.invoke()
        val tag = name ?: dialogFragment::class.java.simpleName
        dialogFragment.show(fm, tag)
    }

    var name: String? = null
    private var actualSummary: (() -> CharSequence?)? = null
    fun summary(block: () -> CharSequence?) {
        actualSummary = block
    }

    override fun resolveSummary(context: Context): CharSequence? =
        actualSummary?.invoke() ?: super.resolveSummary(context)
}

inline fun PreferenceScreen.Appendable.dialogFragment(
    key: String,
    noinline dialog: () -> DialogFragment,
    fragmentManager: FragmentManager,
    block: DialogFragmentPreference.() -> Unit
): DialogFragmentPreference =
    DialogFragmentPreference(key, dialog, fragmentManager)
        .apply(block)
        .also(::addPreferenceItem)