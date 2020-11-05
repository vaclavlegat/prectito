package cz.legat.authors.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import cz.legat.authors.R
import cz.legat.authors.databinding.PtActivityDetailBinding
import cz.legat.core.extensions.ID_KEY
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
            navController.navigate(R.id.authorsDetailFragment, bundleOf(ID_KEY to id))
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.authorsDetailFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}