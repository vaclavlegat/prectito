package cz.legat.prectito.ui.main.authors.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.core.model.Book
import cz.legat.prectito.R
import cz.legat.prectito.ui.main.base.BaseAdapter
import dagger.hilt.android.AndroidEntryPoint

const val BOOKS_TYPE_KEY = "BOOKS_TYPE_KEY"
const val POPULAR_BOOKS = 0
const val NEW_BOOKS = 1
const val AUTHORS = 2

@AndroidEntryPoint
class AuthorBooksFragment : Fragment() {

    private val viewModel: AuthorDetailViewModel by viewModels()

    lateinit var booksRv: RecyclerView
    lateinit var booksAdapter: AuthorBooksAdapter
    lateinit var progress: ProgressBar

    companion object {
        fun newInstance(id: String) = AuthorBooksFragment().apply {
            arguments = Bundle().apply {
                putString("id", id)
            }
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
        booksAdapter = AuthorBooksAdapter(object :
            AuthorBooksAdapter.OnBookClickedListener {
            override fun onBook(book: Book) {
                val action =
                    AuthorDetailFragmentDirections.actionAuthorBooksFragmentToBookDetailFragment(
                        book.id
                    )
                findNavController().navigate(action)
            }
        })
        booksRv = view.findViewById(R.id.pt_books_rw)
        progress = view.findViewById(R.id.pt_progress)
        booksRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
            setHasFixedSize(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progress.visibility = View.VISIBLE
        viewModel.books.observe(viewLifecycleOwner, Observer<PagedList<Book>> { books ->
            progress.visibility = View.GONE
            booksAdapter.submitList(books)
        })
    }
}
