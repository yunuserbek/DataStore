package com.example.datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var settingsManager: SettingsManager
    private var isDarkMode = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingsManager = SettingsManager(applicationContext)
        observaUiPreferences()
        binding.imageView.setOnClickListener {
            setupUi()
        }
    }

    private fun setupUi() {
        lifecycleScope.launch {
            when(isDarkMode){
                true ->settingsManager.setUİMode(UiMode.LIGHT)
                false ->settingsManager.setUİMode(UiMode.DARK)
            }
        }
    }

    private fun observaUiPreferences() {
        settingsManager.uiModelFlow.asLiveData().observe(this){uiMode->
            uiMode?.let {
                when(uiMode){
                    UiMode.LIGHT->onLightMode()
                    UiMode.DARK->onDarkMode()
                }
            }
        }
    }

    private fun onDarkMode() {
        isDarkMode = true
        binding.layoutMain.setBackgroundColor(ContextCompat.getColor(this,android.R.color.black))
        binding.imageView.setImageResource(R.drawable.ic_baseline_wb_sunny_24)
    }

    private fun onLightMode() {
        isDarkMode = false
        binding.layoutMain.setBackgroundColor(ContextCompat.getColor(this,android.R.color.white))
        binding.imageView.setImageResource(R.drawable.ic_moon)
    }
}