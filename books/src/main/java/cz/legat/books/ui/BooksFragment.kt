package cz.legat.books.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cz.legat.books.R
import cz.legat.books.databinding.PtBooksFragmentBinding
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

    private val bookListener = object : BooksAdapter.OnItemClickedListener<Book> {
        override fun onItem(item: Book) {
            startActivity(navigator.getOpenDetailIntent(requireContext(), item.id))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksAdapter = BooksAdapter(bookListener)
        with(binding.ptBooksRv){
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
            adapter = booksAdapter
        }

        val emptyBook = Book(id = "", title = "", imgLink = "")
        val emptyBooks = listOf(emptyBook, emptyBook, emptyBook, emptyBook, emptyBook, emptyBook, emptyBook, emptyBook, emptyBook)

        booksAdapter?.update(emptyBooks)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.books.observe(viewLifecycleOwner, Observer {
            booksAdapter?.update(it)
        })
    }
}
