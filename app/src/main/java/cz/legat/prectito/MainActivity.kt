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

    private val topLevelDestinations = listOf(R.id.overview_nav, R.id.authors_nav, R.id.my_nav, R.id.search_nav)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(this, R.id.nav_host_fragment)

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->

            val fragmentId = when (item.itemId) {
                R.id.home -> R.id.overview_nav
                R.id.authors -> R.id.authors_nav
                R.id.mybooks -> R.id.my_nav
                R.id.search -> R.id.search_nav
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
