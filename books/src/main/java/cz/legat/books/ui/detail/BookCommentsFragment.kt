package cz.legat.books.ui.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookCommentsFragment : BindingFragment<PtCommentsFragmentBinding>(PtCommentsFragmentBinding::inflate) {

    private val viewModel: BookCommentsViewModel by viewModels()

    private lateinit var viewAdapter: PagedCommentsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = PagedCommentsAdapter(object: PagedCommentsAdapter.OnCommentClickedListener{
            override fun onComment(comment: Comment) {

            }
        })

        binding.ptCommentsRv.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        binding.ptProgress.visible()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest {
                binding.ptProgress.gone()
                viewAdapter.submitData(it)
            }
        }
    }


    companion object {
        fun newInstance(id: String) = BookCommentsFragment().withId(id)
    }
}
