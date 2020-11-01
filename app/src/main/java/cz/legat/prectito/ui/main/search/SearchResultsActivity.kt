package cz.legat.prectito.ui.main.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.legat.prectito.databinding.PtActivitySearchResultsBinding
//import cz.legat.prectito.navigation.goToDetailIntent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsActivity : AppCompatActivity(), SearchResultsFragment.OnResultCallback {

    lateinit var binding: PtActivitySearchResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResult(id: String, isBook: Boolean) {
        //startActivity(goToDetailIntent(id, isBook))
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
