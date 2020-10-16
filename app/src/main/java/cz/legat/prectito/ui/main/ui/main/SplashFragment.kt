package cz.legat.prectito.ui.main.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cz.legat.prectito.MainActivity
import cz.legat.prectito.R
import cz.legat.prectito.databinding.PtSplashFragmentBinding
import cz.legat.prectito.extensions.gone
import cz.legat.prectito.ui.main.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BindingFragment<PtSplashFragmentBinding>() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PtSplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.goHome.observe(viewLifecycleOwner, Observer {
            if (it) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        })
    }
}