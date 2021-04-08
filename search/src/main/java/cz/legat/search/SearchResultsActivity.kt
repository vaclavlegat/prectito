package cz.legat.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cz.legat.navigation.AuthorsNavigator
import cz.legat.navigation.BooksNavigator
import cz.legat.search.databinding.PtActivitySearchResultsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchResultsActivity : AppCompatActivity(), SearchResultsFragment.OnResultCallback {

    lateinit var binding: PtActivitySearchResultsBinding
    @Inject lateinit var booksNavigator: BooksNavigator
    @Inject lateinit var authorsNavigator: AuthorsNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    override fun onResult(id: String, isBook: Boolean) {
        val intent = if (isBook) {
            booksNavigator.getOpenDetailIntent(this, id)
        } else {
            authorsNavigator.getOpenDetailIntent(this, id)
        }
        startActivity(intent)
    }
}
