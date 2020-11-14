package cz.legat.books.ui.detail

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.legat.books.R
import cz.legat.books.databinding.PtBookDetailAboutFragmentBinding
import cz.legat.books.databinding.PtBookDetailFragmentBinding
import cz.legat.core.base.BaseAdapter
import cz.legat.core.extensions.*
import cz.legat.core.model.Comment
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.AuthorsNavigator
import cz.legat.navigation.BooksNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BookDetailAboutFragment : BindingFragment<PtBookDetailAboutFragmentBinding>(PtBookDetailAboutFragmentBinding::inflate) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val about = arguments?.getString("about") ?: throw IllegalArgumentException()
        binding.ptAboutTv.fadeInText(about)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
