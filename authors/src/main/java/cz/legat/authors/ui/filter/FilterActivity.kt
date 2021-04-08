package cz.legat.authors.ui.filter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cz.legat.authors.databinding.PtActivityFilterBinding
import cz.legat.core.base.BaseAdapter
import cz.legat.core.model.Countries

class FilterActivity : AppCompatActivity() {

    private lateinit var binding: PtActivityFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtActivityFilterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        val filterAdapter = FilterAdapter(object : BaseAdapter.OnItemClickedListener<Countries> {
            override fun onItem(item: Countries) {
                val returnIntent = Intent()
                returnIntent.putExtra("country", item)
                setResult(RESULT_OK, returnIntent)
                finish()
            }
        })

        filterAdapter.update(Countries.values().toList())

        binding.rvCountries.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = filterAdapter
            setHasFixedSize(true)
        }
    }
}

