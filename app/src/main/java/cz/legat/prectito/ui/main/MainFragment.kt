package cz.legat.prectito.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject lateinit var viewModel: MainViewModel

    lateinit var booksRv: RecyclerView
    lateinit var booksAdapter: BooksAdapter

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.pt_main_fragment, container, false)
        booksAdapter = BooksAdapter()
        booksRv = view.findViewById(R.id.pt_books_rw)
        booksRv.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = booksAdapter
            setHasFixedSize(true)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.popularBooks.observe(viewLifecycleOwner, Observer {
            booksAdapter.update(it)
        })
    }
}
