package cz.legat.prectito.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import cz.legat.prectito.R
import cz.legat.prectito.barcode.LiveBarcodeScanningActivity
import cz.legat.prectito.barcode.Utils
import cz.legat.prectito.ui.main.authors.AuthorsFragment
import cz.legat.prectito.ui.main.books.AUTHORS
import cz.legat.prectito.ui.main.books.BooksFragment
import cz.legat.prectito.ui.main.books.BooksViewModel
import cz.legat.prectito.ui.main.books.NEW_BOOKS
import cz.legat.prectito.ui.main.books.POPULAR_BOOKS
import cz.legat.prectito.ui.main.my.MyBooksFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    interface TabChangeListener {
        fun onTabChanged(position: Int)
    }

    private val viewModel: BooksViewModel by viewModels()

    lateinit var pager: ViewPager2
    lateinit var tabs: TabLayout
    lateinit var scanBtn: FloatingActionButton

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var onTabChangeListener: TabChangeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onTabChangeListener = context as TabChangeListener
    }

    override fun onDetach() {
        super.onDetach()
        onTabChangeListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.pt_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager = view.findViewById(R.id.pt_home_pager)
        tabs = view.findViewById(R.id.pt_home_tabs)
        scanBtn = view.findViewById(R.id.pt_barcode_scan_btn)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pager.adapter = HomeAdapter(this)
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text =
                getString(when(position) {
                    POPULAR_BOOKS -> R.string.pt_tab_title_popular
                    NEW_BOOKS -> R.string.pt_tab_title_new
                    AUTHORS -> R.string.pt_tab_title_authors
                    else -> R.string.pt_tab_title_my_books})
        }.attach()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                onTabChangeListener?.onTabChanged(tab.position)
            }
        })

        scanBtn.setOnClickListener {
            activity?.startActivity(Intent(activity, LiveBarcodeScanningActivity::class.java))
        }
        scanBtn.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.allPermissionsGranted(requireContext())) {
            Utils.requestRuntimePermissions(requireActivity())
        }
    }

    class HomeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            if (position == 2) {
                return AuthorsFragment.newInstance()
            }
            if (position == 3) {
                return MyBooksFragment.newInstance()
            }
            return BooksFragment.newInstance(position)
        }
    }
}
