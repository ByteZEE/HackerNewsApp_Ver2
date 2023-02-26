package com.rafiadly.hackernewsapp.ui.top

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rafiadly.hackernewsapp.databinding.ActivityTopStoriesBinding

class TopStoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopStoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}