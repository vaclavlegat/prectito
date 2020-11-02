package cz.legat.prectito.ui.main.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.legat.navigation.AuthorsNavigator
import cz.legat.navigation.BooksNavigator
import cz.legat.prectito.databinding.PtActivitySearchResultsBinding
//import cz.legat.prectito.navigation.goToDetailIntent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultsActivity : AppCompatActivity(), SearchResultsFragment.OnResultCallback {

    lateinit var binding: PtActivitySearchResultsBinding
    @Inject lateinit var booksNavigator: BooksNavigator
    @Inject lateinit var authorsNavigator: AuthorsNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
