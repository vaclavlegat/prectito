package cz.legat.prectito.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.R
import cz.legat.prectito.persistence.SavedBook
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyBooksFragment : Fragment() {

    private val viewModel: BooksViewModel by viewModels()

    lateinit var booksRv: RecyclerView
    lateinit var booksAdapter: MyBooksAdapter
    lateinit var progress: ProgressBar

    companion object {
        fun newInstance() = MyBooksFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.pt_main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksAdapter = MyBooksAdapter(object : MyBooksAdapter.OnBookClickedListener {
            override fun onBook(book: SavedBook) {
            }

            override fun onBookRemoved(book: SavedBook) {
                viewModel.removeBook(book)
            }
        })
        booksRv = view.findViewById(R.id.pt_books_rw)
        progress = view.findViewById(R.id.pt_progress)
        booksRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
            setHasFixedSize(true)
        }

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                booksAdapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(booksRv)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progress.visibility = View.VISIBLE

        viewModel.myBooks.observe(viewLifecycleOwner, Observer { books ->
            progress.visibility = View.GONE
            booksAdapter.update(books)
        })
    }

    override fun onResume() {
        super.onResume()
    }
}
