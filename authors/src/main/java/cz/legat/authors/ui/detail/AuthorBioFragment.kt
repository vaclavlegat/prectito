package cz.legat.authors.ui.detail

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import cz.legat.authors.databinding.PtAuthorDetailFragmentBinding
import cz.legat.core.extensions.fadeInText
import cz.legat.core.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorBioFragment : BindingFragment<PtAuthorDetailFragmentBinding>(PtAuthorDetailFragmentBinding::inflate) {

    val args: AuthorBioFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.ptAuthorCvTv.fadeInText(args.id)
    }
}
