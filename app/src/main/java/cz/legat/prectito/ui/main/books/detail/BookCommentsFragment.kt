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
import cz.legat.prectito.databinding.PtCommentsFragmentBinding
import cz.legat.prectito.extensions.gone
import cz.legat.prectito.extensions.visible
import cz.legat.prectito.extensions.withId
import cz.legat.prectito.navigation.ID_KEY
import cz.legat.core.ui.BindingFragment
import cz.legat.prectito.ui.main.base.BaseAdapter
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
