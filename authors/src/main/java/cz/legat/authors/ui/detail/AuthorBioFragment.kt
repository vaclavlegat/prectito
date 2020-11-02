package cz.legat.authors.ui.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.legat.authors.databinding.PtAuthorDetailFragmentBinding
import cz.legat.core.extensions.fadeInText
import cz.legat.core.extensions.withId
import cz.legat.core.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorBioFragment : BindingFragment<PtAuthorDetailFragmentBinding>(PtAuthorDetailFragmentBinding::inflate) {

    private val viewModel: AuthorDetailViewModel by viewModels()

    companion object {
        fun newInstance(id: String) = AuthorBioFragment().withId(id)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.author.observe(viewLifecycleOwner,
            Observer<cz.legat.core.model.Author?> {
                it?.let {
                    binding.ptAuthorLifeTv.fadeInText(it.life)
                    binding.ptAuthorCvTv.fadeInText(it.cv)
                }
            })
    }
}
