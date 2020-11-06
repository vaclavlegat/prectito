package cz.legat.books.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import cz.legat.books.R
import cz.legat.books.databinding.PtBookActivityDetailBinding
import cz.legat.books.databinding.PtBooksActivityBinding
import cz.legat.core.extensions.ID_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksActivity : AppCompatActivity() {

    private lateinit var binding: PtBooksActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtBooksActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.books_nav_graph_host)
        val type = intent.extras?.getString("type")
        type?.let {
            navController.navigate(R.id.booksFragment, bundleOf("type" to type))
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.booksFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}