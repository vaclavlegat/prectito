package cz.legat.prectito.ui.main.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.legat.prectito.MainActivity
import cz.legat.prectito.databinding.PtSplashFragmentBinding
import cz.legat.core.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BindingFragment<PtSplashFragmentBinding>(PtSplashFragmentBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.goHome.observe(viewLifecycleOwner, Observer {
            if (it) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        })
    }
}