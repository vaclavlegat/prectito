package cz.legat.prectito

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import cz.legat.prectito.databinding.PtMainActivityBinding
import cz.legat.books.HomeFragment
import cz.legat.prectito.ui.main.search.SearchResultsActivity
import dagger.hilt.android.AndroidEntryPoint

const val RESULT_ID = 0

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HomeFragment.TabChangeListener {

    private lateinit var navController: NavController
    private var currentTab = 0
    lateinit var binding: PtMainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ptSearchFab.setOnClickListener {
            startActivityForResult(Intent(this, SearchResultsActivity::class.java), RESULT_ID)
        }

        navController = findNavController(this, R.id.nav_host_fragment)

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->

            val fragmentId = when (item.itemId) {
                R.id.home -> R.id.homeFragment
                R.id.authors -> R.id.authorsFragment
                R.id.other -> R.id.otherFragment
                R.id.settings -> R.id.settingsFragment
                else -> R.id.homeFragment
            }

            navController.navigate(fragmentId)
            true
        }


        setSupportActionBar(binding.bottomAppBar)
    }

    override fun onTabChanged(position: Int) {
        currentTab = position
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.homeFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
