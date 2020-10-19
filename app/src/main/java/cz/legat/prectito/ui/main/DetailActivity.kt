package cz.legat.prectito.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import cz.legat.prectito.R
import cz.legat.prectito.SEARCH_RESULT_ID_KEY
import cz.legat.prectito.SEARCH_RESULT_TYPE_KEY
import cz.legat.prectito.databinding.PtActivityDetailBinding
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: PtActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtActivityDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.detail_nav_host_fragment)
        val id = intent.extras?.getString(SEARCH_RESULT_ID_KEY)
        id?.let {
            if (intent.extras?.getBoolean(SEARCH_RESULT_TYPE_KEY) == true) {
                navController.navigate(R.id.bookDetailFragment, bundleOf("id" to id))
            } else {
                navController.navigate(R.id.authorsDetailFragment, bundleOf("id" to id))
            }
        }

    }
}