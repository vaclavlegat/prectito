package cz.legat.prectito.ui.main.authors.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.legat.prectito.R
import cz.legat.prectito.extensions.fadeInText
import cz.legat.prectito.extensions.loadImg
import cz.legat.core.model.Author
import cz.legat.prectito.databinding.PtAuthorDetailFragmentBinding
import cz.legat.prectito.ui.main.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorBioFragment : BindingFragment<PtAuthorDetailFragmentBinding>(PtAuthorDetailFragmentBinding::inflate) {

    private val viewModel: AuthorDetailViewModel by viewModels()

    companion object {
        fun newInstance(id: String): AuthorBioFragment {
            return AuthorBioFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.author.observe(viewLifecycleOwner,
            Observer<cz.legat.core.model.Author?> {
                it?.let {
                    binding.ptAuthorNameTv.fadeInText(it.name)
                    binding.ptAuthorLifeTv.fadeInText(it.life)
                    binding.ptAuthorCvTv.fadeInText(it.cv)
                    binding.ptAuthorImageIv.loadImg(it.authorImgLink)
                }
            })
    }
}
