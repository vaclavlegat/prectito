package cz.legat.prectito

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import cz.legat.prectito.databinding.PtMainActivityBinding
import cz.legat.prectito.ui.main.HomeFragment
import cz.legat.prectito.ui.main.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

const val SEARCH_RESULT_ID_KEY = "RESULT_ID_KEY"
const val SEARCH_RESULT_TYPE_KEY = "SEARCH_RESULT_TYPE_KEY"
const val RESULT_ID = 0

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HomeFragment.TabChangeListener {

    private var navController: NavController? = null
    private var currentTab = 0
    lateinit var binding: PtMainActivityBinding
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(this, R.id.nav_host_fragment)

        id = intent.extras?.getString(SEARCH_RESULT_ID_KEY)
        id?.let {
            if (intent.extras?.getBoolean(SEARCH_RESULT_TYPE_KEY) == true) {
                navController?.navigate(HomeFragmentDirections.actionHomeFragmentToBookDetailFragment(it))
            } else {
                navController?.navigate(HomeFragmentDirections.actionHomeFragmentToAuthorsFragment(it))
            }
        }
    }

    override fun onTabChanged(position: Int) {
        currentTab = position
    }

    override fun onBackPressed() {
        if (id != null
            && (navController?.currentDestination?.id == navController?.graph?.findNode(R.id.bookDetailFragment)?.id
                    || navController?.currentDestination?.id == navController?.graph?.findNode(R.id.authorsDetailFragment)?.id)
        ) {
            finish()
        }
        super.onBackPressed()
    }
}
