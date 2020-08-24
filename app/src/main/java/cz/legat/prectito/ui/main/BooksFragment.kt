package cz.legat.prectito.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.R
import cz.legat.prectito.model.Book
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val BOOKS_TYPE_KEY = "BOOKS_TYPE_KEY"
const val POPULAR_BOOKS = 0
const val NEW_BOOKS = 1

@AndroidEntryPoint
class BooksFragment : Fragment() {

    @Inject lateinit var viewModel: BooksViewModel

    lateinit var booksRv: RecyclerView
    lateinit var booksAdapter: BooksAdapter
    lateinit var progress: ProgressBar

    companion object {
        fun newInstance(booksType: Int) = BooksFragment().apply {
            arguments = Bundle().apply { putInt(BOOKS_TYPE_KEY, booksType) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.pt_main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksAdapter = BooksAdapter(object:BooksAdapter.OnBookClickedListener{
            override fun onBook(book: Book) {
                findNavController().navigate(R.id.action_homeFragment_to_detailFragment, Bundle().apply {
                    putParcelable("book", book)
                })
            }
        })
        booksRv = view.findViewById(R.id.pt_books_rw)
        progress = view.findViewById(R.id.pt_progress)
        booksRv.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = booksAdapter
            setHasFixedSize(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progress.visibility = View.VISIBLE
        arguments?.takeIf { it.containsKey(BOOKS_TYPE_KEY) }?.let {
            if (it[BOOKS_TYPE_KEY] as Int == 0) {
                viewModel.popularBooks.observe(viewLifecycleOwner, Observer { books ->
                    progress.visibility = View.GONE
                    booksAdapter.update(books)
                })
            } else {
                viewModel.newBooks.observe(viewLifecycleOwner, Observer { books ->
                    progress.visibility = View.GONE
                    booksAdapter.update(books)
                })
            }
        }
    }
}
