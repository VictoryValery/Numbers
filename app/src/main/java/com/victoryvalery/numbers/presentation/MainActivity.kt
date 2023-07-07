package com.victoryvalery.numbers.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.victoryvalery.numbers.R
import com.victoryvalery.numbers.databinding.ActivityMainBinding
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, WelcomeFragment.newInstance())
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}