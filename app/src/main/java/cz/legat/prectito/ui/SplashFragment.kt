package cz.legat.prectito.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.MainNavigator
import cz.legat.prectito.databinding.PtSplashFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BindingFragment<PtSplashFragmentBinding>(PtSplashFragmentBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()
    @Inject lateinit var mainNavigator: MainNavigator

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.goHome.observe(viewLifecycleOwner, Observer {
            if (it) {
                startActivity(mainNavigator.openMainIntent(requireContext()))
            }
        })
    }
}