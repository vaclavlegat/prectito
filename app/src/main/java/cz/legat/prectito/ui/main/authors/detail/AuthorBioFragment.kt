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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorBioFragment : Fragment() {

    private val viewModel: AuthorDetailViewModel by viewModels()

    lateinit var nameTv: TextView
    lateinit var imageIv: ImageView
    lateinit var lifeTv: TextView
    lateinit var cvTv: TextView

    companion object {
        fun newInstance(id: String): AuthorBioFragment {
            return AuthorBioFragment().apply {
                arguments = Bundle().apply {
                    putString("id", id)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pt_author_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        nameTv = view.findViewById(R.id.pt_author_name_tv)
        lifeTv = view.findViewById(R.id.pt_author_life_tv)
        imageIv = view.findViewById(R.id.pt_author_image_iv)
        cvTv = view.findViewById(R.id.pt_author_cv_tv)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.author.observe(viewLifecycleOwner,
            Observer<cz.legat.core.model.Author?> {
                it?.let {
                    nameTv.fadeInText(it.name)
                    lifeTv.fadeInText(it.life)
                    cvTv.fadeInText(it.cv)
                    imageIv.loadImg(it.authorImgLink)
                }
            })
    }
}
