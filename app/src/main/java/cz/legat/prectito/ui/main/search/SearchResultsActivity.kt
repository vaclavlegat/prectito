package cz.legat.prectito.ui.main.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.legat.prectito.MainActivity
import cz.legat.prectito.SEARCH_RESULT_ID_KEY
import cz.legat.prectito.SEARCH_RESULT_TYPE_KEY
import cz.legat.prectito.databinding.PtActivitySearchResultsBinding
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
        // setResult(Activity.RESULT_OK, Intent().apply {
        //     putExtras(bundleOf("SEARCH_RESULT_ID_KEY" to id, "SEARCH_RESULT_TYPE_KEY" to isBook))
        // })
        // finish()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(SEARCH_RESULT_ID_KEY, id)
        intent.putExtra(SEARCH_RESULT_TYPE_KEY, isBook)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
