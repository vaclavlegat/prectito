package cz.legat.prectito

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import cz.legat.core.base.BaseActivity
import cz.legat.navigation.SearchNavigator
import cz.legat.prectito.databinding.PtMainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    lateinit var binding: PtMainActivityBinding

    @Inject
    lateinit var searchNavigator: SearchNavigator

    private val topLevelDestinations = listOf(R.id.homeFragment, R.id.authorsFragment, R.id.myBooksFragment, R.id.searchResultsFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        navController = findNavController(this, R.id.nav_host_fragment)

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->

            val fragmentId = when (item.itemId) {
                R.id.home -> R.id.homeFragment
                R.id.authors -> R.id.authorsFragment
                R.id.mybooks -> R.id.myBooksFragment
                R.id.search -> R.id.searchResultsFragment
                else -> null
            }

            fragmentId?.let {
                navController.navigate(it)
            }
            true
        }
    }

    override fun onBackPressed() {
        if (topLevelDestinations.contains(navController.currentDestination?.id)) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
