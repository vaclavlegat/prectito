package cz.legat.prectito.ui.main.settings

import androidx.fragment.app.viewModels
import cz.legat.prectito.databinding.PtSettingsFragmentBinding
import cz.legat.core.ui.BindingFragment

class SettingsFragment : BindingFragment<PtSettingsFragmentBinding>(PtSettingsFragmentBinding::inflate) {

    private val viewModel: SettingsViewModel by viewModels()
}