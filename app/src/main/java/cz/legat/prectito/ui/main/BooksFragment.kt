package cz.legat.prectito.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val BOOKS_TYPE_KEY = "BOOKS_TYPE_KEY"
const val POPULAR_BOOKS = 0
const val NEW_BOOKS = 0

@AndroidEntryPoint
class BooksFragment : Fragment() {

    @Inject lateinit var viewModel: BooksViewModel

    lateinit var booksRv: RecyclerView
    lateinit var booksAdapter: BooksAdapter

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
        booksAdapter = BooksAdapter()
        booksRv = view.findViewById(R.id.pt_books_rw)
        booksRv.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = booksAdapter
            setHasFixedSize(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.takeIf { it.containsKey(BOOKS_TYPE_KEY) }?.let {
            if (it[BOOKS_TYPE_KEY] as Int == 0) {
                viewModel.popularBooks.observe(viewLifecycleOwner, Observer { books ->
                    booksAdapter.update(books)
                })
            } else {
                viewModel.newBooks.observe(viewLifecycleOwner, Observer { books ->
                    booksAdapter.update(books)
                })
            }
        }
    }
}
