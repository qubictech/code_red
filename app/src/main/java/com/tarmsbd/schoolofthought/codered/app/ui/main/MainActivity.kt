package com.tarmsbd.schoolofthought.codered.app.ui.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.adapter.RecentStatusAdapter
import com.tarmsbd.schoolofthought.codered.app.data.models.RecentStatus
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.MainViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityMainBinding

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

        activityMainBinding.genderSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 -> mainViewModel.setGender("NONE")
                        1 -> mainViewModel.setGender("M")
                        2 -> mainViewModel.setGender("F")
                    }
                }

            }

        mainViewModel.getRecentStatus.observe(this, Observer { showRecentStatus(it) })
    }

    private fun showRecentStatus(data: List<RecentStatus>) {
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
    }
}
