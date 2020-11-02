package cz.legat.scanner.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.core.extensions.gone
import cz.legat.core.extensions.visible
import cz.legat.core.persistence.SavedBook
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.BooksNavigator
import cz.legat.navigation.MyBooksNavigator
import cz.legat.scanner.barcode.Utils
import cz.legat.scanner.databinding.PtMyBooksFragmentBinding
import cz.legat.scanner.navigation.MyBooksNavigatorImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyBooksFragment : BindingFragment<PtMyBooksFragmentBinding>(PtMyBooksFragmentBinding::inflate) {

    private val viewModel: ISBNViewModel by viewModels()
    lateinit var booksAdapter: MyBooksAdapter

    @Inject lateinit var navigator: MyBooksNavigator
    @Inject lateinit var booksNavigator: BooksNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksAdapter = MyBooksAdapter(object :
            MyBooksAdapter.OnBookClickedListener {
            override fun onBook(book: SavedBook) {
                book.bookId?.let {
                    startActivity(booksNavigator.getOpenDetailIntent(requireContext(), it))
                }
            }

            override fun onBookRemoved(book: SavedBook) {
                viewModel.removeBook(book)
            }
        })
        binding.ptBooksRw.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
            setHasFixedSize(true)
        }

        binding.ptAddBookBtn.setOnClickListener {
            startActivity(navigator.getOpenScannerIntent(requireContext()))
        }

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                booksAdapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.ptBooksRw)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Utils.requestRuntimePermissions(requireActivity())

        binding.ptProgress.visible()

        viewModel.myBooks.observe(viewLifecycleOwner, Observer { books ->
            binding.ptProgress.gone()
            var count = 0
            for (book in books) {
                count++
                println("${book.title} - ${book.author}, ${book.publishedDate?.take(4)} ISBN: ${book.isbn}")
            }
            println("Books Count $count")
            booksAdapter.update(books)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyBooks()
    }
}
