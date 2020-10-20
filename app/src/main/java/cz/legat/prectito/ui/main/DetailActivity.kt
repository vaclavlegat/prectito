package cz.legat.prectito.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import cz.legat.prectito.R
import cz.legat.prectito.databinding.PtActivityDetailBinding
import cz.legat.prectito.navigation.ID_KEY
import cz.legat.prectito.navigation.IS_BOOK_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: PtActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtActivityDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.detail_nav_host_fragment)
        val id = intent.extras?.getString(ID_KEY)
        id?.let {
            if (intent.extras?.getBoolean(IS_BOOK_KEY) == true) {
                navController.navigate(R.id.bookDetailFragment, bundleOf("id" to id))
            } else {
                navController.navigate(R.id.authorsDetailFragment, bundleOf("id" to id))
            }
        }

    }
}