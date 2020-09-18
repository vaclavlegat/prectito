package cz.legat.prectito.ui.main.books.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.R
import cz.legat.core.model.Comment
import cz.legat.prectito.ui.main.base.BaseAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookCommentsFragment : Fragment() {

    private val viewModel: BookDetailViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: CommentsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.pt_comments_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.pt_comments_rv)
        progressBar = rootView.findViewById(R.id.pt_progress)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = CommentsAdapter(object: BaseAdapter.OnItemClickedListener<cz.legat.core.model.Comment>{
            override fun onItem(item: cz.legat.core.model.Comment) {

            }
        })

        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        progressBar.visibility = View.VISIBLE
        viewModel.comments.observe(viewLifecycleOwner, Observer<List<cz.legat.core.model.Comment>> {
            progressBar.visibility = View.GONE
            viewAdapter.update(it)
        })
    }


    companion object {
        fun newInstance(id: String): BookCommentsFragment {
            return BookCommentsFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                }
            }
        }
    }
}
