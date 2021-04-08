package cz.legat.books.ui.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.legat.books.databinding.PtBookDetailAboutFragmentBinding
import cz.legat.core.extensions.fadeInText
import cz.legat.core.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookDetailAboutFragment :
    BindingFragment<PtBookDetailAboutFragmentBinding>(PtBookDetailAboutFragmentBinding::inflate) {

    private val viewModel: BookAboutViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.book.observe(viewLifecycleOwner, Observer {
            binding.ptAboutTv.fadeInText(it?.description)
        })
    }
}
