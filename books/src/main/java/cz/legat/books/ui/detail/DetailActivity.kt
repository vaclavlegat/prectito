package cz.legat.books.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import cz.legat.books.R
import cz.legat.books.databinding.PtBookActivityDetailBinding
import cz.legat.core.base.BaseActivity
import cz.legat.core.extensions.ID_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity() {

    private lateinit var binding: PtBookActivityDetailBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtBookActivityDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.book_detail_nav_host_fragment)
        val id = intent.extras?.getString(ID_KEY)
        id?.let {
            navController.navigate(R.id.bookDetailFragment, bundleOf(ID_KEY to id))
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.bookDetailFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}