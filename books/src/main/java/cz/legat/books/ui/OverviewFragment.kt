package cz.legat.books.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.legat.books.R
import cz.legat.books.databinding.PtHomeFragmentBinding
import cz.legat.books.databinding.PtOverviewItemBinding
import cz.legat.core.base.BaseAdapter
import cz.legat.core.extensions.simpleGrid
import cz.legat.core.model.Book
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.BooksNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : BindingFragment<PtHomeFragmentBinding>(PtHomeFragmentBinding::inflate) {

    @Inject lateinit var navigator: BooksNavigator

    private val viewModel: OverviewViewModel by viewModels()

    private val bookListener = object : BaseAdapter.OnItemClickedListener<Book> {
        override fun onItem(item: Book) {
            startActivity(navigator.getOpenDetailIntent(requireContext(), item.id))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ptOverviewContainer.removeAllViews()
        val popularBooksAdapter = prepareLayout(POPULAR)
        val newBooksAdapter = prepareLayout(NEW)
        val eBooksAdapter = prepareLayout(EBOOK)
        viewModel.overview.observe(viewLifecycleOwner, Observer {
            popularBooksAdapter.update(it.popularBooks)
            newBooksAdapter.update(it.newBooks)
            eBooksAdapter.update(it.eBooks)
        })
    }

    private fun prepareLayout(type: String) : BooksAdapter {
        val itemBinding = PtOverviewItemBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false)
        val booksAdapter = BooksAdapter(bookListener)
        itemBinding.ptBooksRv.simpleGrid(booksAdapter)
        itemBinding.ptTitleTv.text = when (type){
            POPULAR -> getString(R.string.pt_popular_books)
            NEW -> getString(R.string.pt_new_books)
            EBOOK -> getString(R.string.pt_e_books)
            else -> ""
        }
        val emptyBook = Book(id = "",title = "", imgLink = "")
        val emptyBooks = listOf<Book>(emptyBook, emptyBook, emptyBook)
        booksAdapter.update(emptyBooks)
        itemBinding.ptMoreBtn.setOnClickListener {
            startActivity(navigator.getOpenBooksIntent(requireContext(), type))
        }
        binding.ptOverviewContainer.addView(itemBinding.root)
        return booksAdapter
    }
}
