package cz.legat.prectito.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.legat.prectito.databinding.PtActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    lateinit var binding: PtActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PtActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
