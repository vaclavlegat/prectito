package cz.legat.books.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.legat.books.databinding.PtBooksFragmentBinding
import cz.legat.books.databinding.PtHomeFragmentBinding
import cz.legat.core.base.BaseAdapter
import cz.legat.core.extensions.simpleGrid
import cz.legat.core.model.Book
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.BooksNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BooksFragment : BindingFragment<PtBooksFragmentBinding>(PtBooksFragmentBinding::inflate) {

    @Inject lateinit var navigator: BooksNavigator

    private val viewModel: BooksViewModel by viewModels()
    private var booksAdapter: BooksAdapter? = null

    private val bookListener = object : BaseAdapter.OnItemClickedListener<Book> {
        override fun onItem(item: Book) {
            startActivity(navigator.getOpenDetailIntent(requireContext(), item.id))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ptToolbar.title = "Popular"
        booksAdapter = BooksAdapter(bookListener)
        binding.ptBooksRv.simpleGrid(booksAdapter)

        val emptyBook = Book(id = "",title = "", imgLink = "")
        val emptyBooks = listOf(emptyBook, emptyBook, emptyBook,emptyBook, emptyBook, emptyBook,emptyBook, emptyBook, emptyBook)

        booksAdapter?.update(emptyBooks)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.books.observe(viewLifecycleOwner, Observer {
            booksAdapter?.update(it)
        })
    }
}
