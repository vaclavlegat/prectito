package cz.legat.prectito.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import cz.legat.core.extensions.ID_KEY
import cz.legat.core.extensions.IS_BOOK_KEY
import cz.legat.prectito.R
import cz.legat.prectito.databinding.PtActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: PtActivityDetailBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtActivityDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.detail_nav_host_fragment)
        val id = intent.extras?.getString(ID_KEY)
        id?.let {
            if (intent.extras?.getBoolean(IS_BOOK_KEY) == true) {
                navController.navigate(R.id.bookDetailFragment, bundleOf(ID_KEY to id))
            } else {
                navController.navigate(R.id.authorsDetailFragment, bundleOf(ID_KEY to id))
            }
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.bookCommentsFragment) {
            super.onBackPressed()
        } else {
            finish()
        }
    }
}