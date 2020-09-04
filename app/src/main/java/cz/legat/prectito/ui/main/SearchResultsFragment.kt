package cz.legat.prectito.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.R
import cz.legat.prectito.model.Book
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : Fragment() {

    private val viewModel: BooksViewModel by viewModels()
    val args: SearchResultsFragmentArgs by navArgs()

    lateinit var booksRv: RecyclerView
    lateinit var booksAdapter: SearchResultsAdapter
    lateinit var progress: ProgressBar

    companion object {
        fun newInstance() = BooksFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.pt_main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksAdapter = SearchResultsAdapter(object : SearchResultsAdapter.OnBookClickedListener {
            override fun onBook(book: Book) {
                val action = SearchResultsFragmentDirections.actionSearchResultsFragmentToDetailFragment(book.id)
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
        viewModel.searchBook(args.query)
        progress.visibility = View.VISIBLE
        viewModel.searchBooks.observe(viewLifecycleOwner, Observer<List<Book>> {
            booksAdapter.update(it)
            progress.visibility = View.GONE
        })
    }
}
