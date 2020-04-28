package com.tarmsbd.coronavirus.prediction.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tarmsbd.coronavirus.prediction.app.R
import com.tarmsbd.coronavirus.prediction.app.adapter.RecentStatusAdapter
import com.tarmsbd.coronavirus.prediction.app.data.viewmodel.MainViewModel
import com.tarmsbd.coronavirus.prediction.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        activityMainBinding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = mainViewModel
        }

        mainViewModel.getRecentStatus.observe(this, Observer { data ->
            // list adapter
            val recentStatusAdapter = RecentStatusAdapter()

            // submit the list data
            recentStatusAdapter.submitList(data)

            // recyclerview
            activityMainBinding.recentStatusRecyclerView.apply {
                adapter = recentStatusAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
                hasFixedSize()
            }
        })
    }
}
