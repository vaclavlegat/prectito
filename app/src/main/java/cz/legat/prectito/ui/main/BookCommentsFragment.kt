package cz.legat.prectito.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.R
import cz.legat.prectito.model.Book
import cz.legat.prectito.model.Comment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookCommentsFragment : Fragment() {

    private val viewModel: BookDetailViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: CommentsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.pt_comments_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.pt_comments_rv)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = CommentsAdapter()

        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        viewModel.comments.observe(viewLifecycleOwner, Observer<List<Comment>> {
            viewAdapter.update(it)
        })
    }


    companion object {
        fun newInstance(book: Book): BookCommentsFragment {
            val args = Bundle()
            args.putParcelable("book", book)
            val fragment = BookCommentsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
