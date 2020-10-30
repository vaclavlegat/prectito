package cz.legat.prectito.ui.main.authors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.core.extensions.gone
import cz.legat.core.extensions.visible
import cz.legat.core.model.Author
import cz.legat.core.model.Countries
import cz.legat.prectito.databinding.PtFragmentAuthorsBinding
import cz.legat.prectito.navigation.goToAuthorDetailIntent
import cz.legat.core.ui.BindingFragment
import cz.legat.prectito.ui.main.FilterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthorsFragment : BindingFragment<PtFragmentAuthorsBinding>(PtFragmentAuthorsBinding::inflate) {

    companion object {
        private const val GRID_COLUMNS = 3
    }

    private val viewModel: AuthorsViewModel by viewModels()
    private lateinit var viewAdapter: AuthorsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = GridLayoutManager(activity, GRID_COLUMNS)
        viewAdapter = AuthorsAdapter(object : AuthorsAdapter.OnAuthorClickedListener {
            override fun onAuthor(author: Author) {
                startActivity(requireContext().goToAuthorDetailIntent(author.authorId))
            }
        })

        binding.ptAddFilter.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), FilterActivity::class.java), 0)
        }

        binding.ptCoutryChip.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), FilterActivity::class.java), 0)
        }

        binding.rvAuthors.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        binding.ptAddFilter.visible()
        binding.ptCoutryChip.gone()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.extras?.containsKey("country") == true) {
            binding.ptProgress.visible()
            binding.rvAuthors.gone()
            val country = data.extras?.getSerializable("country") as Countries
            viewModel.filter(country.id.toString())
            viewAdapter.refresh()
            binding.ptAddFilter.gone()
            binding.ptCoutryChip.visible()
            binding.ptCoutryChip.text = country.country
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest { pagingData ->
                binding.ptProgress.gone()
                binding.rvAuthors.visible()
                viewAdapter.submitData(pagingData)
            }
        }
    }
}
