package cz.legat.authors.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.authors.databinding.PtFragmentAuthorsBinding
import cz.legat.core.extensions.gone
import cz.legat.core.extensions.visible
import cz.legat.core.model.Author
import cz.legat.core.model.Countries
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.AuthorsNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthorsFragment : BindingFragment<PtFragmentAuthorsBinding>(PtFragmentAuthorsBinding::inflate) {

    @Inject lateinit var navigator: AuthorsNavigator

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
                startActivity(navigator.getOpenDetailIntent(requireContext(), author.authorId))
            }
        })

        binding.ptAddFilter.setOnClickListener {
            startActivityForResult(navigator.getCountryFilterIntent(requireContext()), 0)
        }

        binding.ptCoutryChip.setOnClickListener {
            startActivityForResult(navigator.getCountryFilterIntent(requireContext()), 0)
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
