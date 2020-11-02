package cz.legat.books.ui.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.books.databinding.PtCommentsFragmentBinding
import cz.legat.core.model.Comment
import cz.legat.core.extensions.withId
import cz.legat.core.ui.BindingFragment
import cz.legat.core.base.BaseAdapter
import cz.legat.core.extensions.gone
import cz.legat.core.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookCommentsFragment : BindingFragment<PtCommentsFragmentBinding>(PtCommentsFragmentBinding::inflate) {

    private val viewModel: BookDetailViewModel by viewModels()

    private lateinit var viewAdapter: CommentsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = CommentsAdapter(object: BaseAdapter.OnItemClickedListener<Comment>{
            override fun onItem(item: Comment) {

            }
        })

        binding.ptCommentsRv.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        binding.ptProgress.visible()
        viewModel.allComments.observe(viewLifecycleOwner, Observer<List<Comment>> {
            binding.ptProgress.gone()
            viewAdapter.update(it)
        })
    }


    companion object {
        fun newInstance(id: String) = BookCommentsFragment().withId(id)
    }
}
