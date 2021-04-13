package cz.legat.books.ui.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.books.databinding.PtCommentsFragmentBinding
import cz.legat.core.extensions.gone
import cz.legat.core.extensions.visible
import cz.legat.core.extensions.visibleIf
import cz.legat.core.extensions.withId
import cz.legat.core.model.Comment
import cz.legat.core.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookCommentsFragment :
    BindingFragment<PtCommentsFragmentBinding>(PtCommentsFragmentBinding::inflate) {

    private val viewModel: BookCommentsViewModel by viewModels()

    private lateinit var viewAdapter: PagedCommentsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = PagedCommentsAdapter(object : PagedCommentsAdapter.OnCommentClickedListener {
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
                viewAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewAdapter.loadStateFlow.map {
                it.refresh
            }.distinctUntilChanged().collect {
                if (it is LoadState.NotLoading) {
                    binding.ptProgress.gone()
                    binding.ptCommentsRv.visibleIf(viewAdapter.itemCount > 0)
                    binding.emptyMessage.visibleIf(viewAdapter.itemCount == 0)
                }
            }
        }
    }


    companion object {
        fun newInstance(id: String) = BookCommentsFragment().withId(id)
    }
}
