package cz.legat.prectito.ui.main.other

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import cz.legat.prectito.R
import cz.legat.prectito.databinding.PtOtherFragmentBinding
import cz.legat.prectito.ui.main.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherFragment : BindingFragment<PtOtherFragmentBinding>() {

    companion object {
        fun newInstance() = OtherFragment()
    }

    private val viewModel: OtherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PtOtherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}