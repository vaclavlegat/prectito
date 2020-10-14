package cz.legat.prectito

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation.findNavController
import cz.legat.prectito.ui.main.HomeFragment
import cz.legat.prectito.ui.main.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HomeFragment.TabChangeListener{

    private lateinit var toolbar: Toolbar
    private var navController: NavController? = null
    private var searchHolder: View? = null
    private var currentTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pt_main_activity)
        toolbar = findViewById(R.id.toolbar)
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.pt_color_background))
        setSupportActionBar(toolbar)
        navController = findNavController(this, R.id.nav_host_fragment)

        searchHolder = toolbar.findViewById(R.id.pt_search_holder_ll)
        searchHolder?.setOnClickListener {
            val bookSearchAction = HomeFragmentDirections.actionHomeFragmentToSearchResultsFragment("")
            navController?.navigate(bookSearchAction)
        }

        navController?.addOnDestinationChangedListener { _, _, _ ->
            if(navController?.currentDestination?.id == navController?.graph?.findNode(R.id.homeFragment)?.id) {
                toolbar.visibility = View.VISIBLE
                searchHolder?.visibility = View.VISIBLE
            } else {
                toolbar.visibility = View.GONE
                searchHolder?.visibility = View.GONE
            }
        }
    }

    override fun onTabChanged(position: Int) {
        currentTab = position
    }
}
