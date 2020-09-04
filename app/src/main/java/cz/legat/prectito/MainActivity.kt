package cz.legat.prectito

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.SearchManager
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import cz.legat.prectito.ui.main.BooksViewModel
import cz.legat.prectito.ui.main.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: BooksViewModel by viewModels()

    private lateinit var toolbar: Toolbar
    private var searchItem: MenuItem? = null
    private var navController: NavController? = null
    private var timer: Timer? = null

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
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        //menu?.clear()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (navController?.currentDestination?.id == navController?.graph?.startDestination) {
            menuInflater.inflate(R.menu.pt_main_menu, menu)
            searchItem = menu.findItem(R.id.m_search)
            val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
            val to = intArrayOf(android.R.id.text1)
            val cursorAdapter = SimpleCursorAdapter(
                this,
                R.layout.pt_suggestions_row,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            )

            val searchView = searchItem?.actionView as SearchView
            searchView.suggestionsAdapter = cursorAdapter


            viewModel.searchBooks.observe(this, Observer { books ->
                val cursor = MatrixCursor(
                    arrayOf(
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_TEXT_2
                    )
                )
                books.forEachIndexed { index, book ->
                    var author = ""
                    var published = ""

                    val split = book.description.split(",")
                    if (split.size == 2) {
                        author = split[1].trim()
                        published = split[0].trim()
                    } else {
                        author = split[0]
                    }

                    cursor.addRow(arrayOf(index, "${book.title} - $author ($published)", book.id))
                }
                cursorAdapter.changeCursor(cursor)
                cursorAdapter.notifyDataSetChanged()

            })

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToSearchResultsFragment(query)
                        navController?.navigate(action)
                    }
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (timer != null) {
                        timer?.cancel()
                    }
                    timer = Timer()
                    timer?.schedule(object : TimerTask() {
                        override fun run() {
                            viewModel.searchBook(query)
                        }
                    }, 400) // 600ms delay be
                    return true
                }
            })

            searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                    val selection =
                        cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2))
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToDetailFragment(selection)
                    navController?.navigate(action)
                    // Do something with selection
                    return true
                }
            })

            MenuItemCompat.setOnActionExpandListener(
                searchItem,
                object : MenuItemCompat.OnActionExpandListener {
                    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                        // Called when SearchView is collapsing
                        if (searchItem?.isActionViewExpanded == true) {
                            animateSearchToolbar(1, false, false)
                        }
                        return true
                    }

                    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                        // Called when SearchView is expanding
                        animateSearchToolbar(1, true, true)
                        return true
                    }
                })

            return true
        } else {
            return false
        }
    }

    fun animateSearchToolbar(
        numberOfMenuIcon: Int,
        containsOverflow: Boolean,
        show: Boolean
    ) {
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.pt_search_bar_active))

        if (show) {

            val width: Int = toolbar.width -
                    (if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0) -
                    resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon / 2
            val createCircularReveal: Animator = ViewAnimationUtils.createCircularReveal(
                toolbar,
                if (isRtl(resources)) toolbar.width - width else width,
                toolbar.height / 2,
                0.0f,
                width.toFloat()
            )
            createCircularReveal.duration = 250
            createCircularReveal.start()
        } else {
            val width: Int = toolbar.width -
                    (if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0) -
                    resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon / 2
            val createCircularReveal: Animator = ViewAnimationUtils.createCircularReveal(
                toolbar,
                if (isRtl(resources)) toolbar.width - width else width,
                toolbar.height / 2,
                width.toFloat(),
                0.0f
            )
            createCircularReveal.duration = 250
            createCircularReveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    toolbar.setBackgroundColor(
                        getThemeColor(
                            this@MainActivity,
                            R.attr.colorPrimary
                        )
                    )
                }
            })
            createCircularReveal.start()
        }
    }

    private fun isRtl(resources: Resources): Boolean {
        return resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    private fun getThemeColor(context: Context, id: Int): Int {
        val theme: Resources.Theme = context.theme
        val a: TypedArray = theme.obtainStyledAttributes(intArrayOf(id))
        val result = a.getColor(0, 0)
        a.recycle()
        return result
    }
}
