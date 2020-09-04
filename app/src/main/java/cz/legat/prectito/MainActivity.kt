package cz.legat.prectito

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation.findNavController
import cz.legat.prectito.ui.main.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private var navController: NavController? = null
    private var searchHolder: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pt_main_activity)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navController = findNavController(this, R.id.nav_host_fragment)
        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            invalidateOptionsMenu()
        }

        searchHolder = toolbar.findViewById(R.id.pt_search_holder_ll)
        searchHolder?.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchResultsFragment("")
            navController?.navigate(action)
        }

        navController?.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { controller, destination, arguments ->
            if(navController?.currentDestination?.id == navController?.graph?.startDestination) {
                toolbar.visibility = View.VISIBLE
                searchHolder?.visibility = View.VISIBLE
            } else {
                toolbar.visibility = View.GONE
                searchHolder?.visibility = View.GONE
            }
        })
    }
}
