package cz.legat.books.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.legat.books.databinding.PtHomeFragmentBinding
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
    private var popularBooksAdapter: BooksAdapter? = null
    private var newBooksAdapter: BooksAdapter? = null
    private var eBooksAdapter: BooksAdapter? = null

    private val bookListener = object : BaseAdapter.OnItemClickedListener<Book> {
        override fun onItem(item: Book) {
            startActivity(navigator.getOpenDetailIntent(requireContext(), item.id))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popularBooksAdapter = BooksAdapter(bookListener)
        newBooksAdapter = BooksAdapter(bookListener)
        eBooksAdapter = BooksAdapter(bookListener)

        binding.ptPopularBooksRv.simpleGrid(popularBooksAdapter)
        binding.ptNewBooksRv.simpleGrid(newBooksAdapter)
        binding.ptEBooksRv.simpleGrid(eBooksAdapter)

        val emptyBook = Book(id = "",title = "", imgLink = "")
        val emptyBooks = listOf<Book>(emptyBook, emptyBook, emptyBook)

        popularBooksAdapter?.update(emptyBooks)
        newBooksAdapter?.update(emptyBooks)
        eBooksAdapter?.update(emptyBooks)

        binding.ptPopularMoreBtn.setOnClickListener {
            startActivity(navigator.getOpenBooksIntent(requireContext(), POPULAR))
        }
        binding.ptNewMoreBtn.setOnClickListener {
            startActivity(navigator.getOpenBooksIntent(requireContext(), NEW))
        }
        binding.ptEMoreBtn.setOnClickListener {
            startActivity(navigator.getOpenBooksIntent(requireContext(), EBOOK))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.overview.observe(viewLifecycleOwner, Observer {
            popularBooksAdapter?.update(it.popularBooks)
            newBooksAdapter?.update(it.newBooks)
            eBooksAdapter?.update(it.eBooks)
        })
    }
}
