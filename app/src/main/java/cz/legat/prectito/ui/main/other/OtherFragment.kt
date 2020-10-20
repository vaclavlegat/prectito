package cz.legat.prectito.ui.main.other

import androidx.fragment.app.viewModels
import cz.legat.prectito.databinding.PtOtherFragmentBinding
import cz.legat.prectito.ui.main.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherFragment : BindingFragment<PtOtherFragmentBinding>(PtOtherFragmentBinding::inflate) {

    private val viewModel: OtherViewModel by viewModels()
}