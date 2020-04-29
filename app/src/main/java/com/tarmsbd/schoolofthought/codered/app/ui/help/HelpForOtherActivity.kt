package com.tarmsbd.schoolofthought.codered.app.ui.help

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
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.HelpForOtherViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityHelpForOtherBinding

class HelpForOtherActivity : AppCompatActivity() {
    private lateinit var activityHelpForOtherBinding: ActivityHelpForOtherBinding
    private lateinit var helpForOtherViewModel: HelpForOtherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHelpForOtherBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_help_for_other)
        helpForOtherViewModel = ViewModelProvider(this)[HelpForOtherViewModel::class.java]

        activityHelpForOtherBinding.apply {
            viewModel = helpForOtherViewModel
            lifecycleOwner = this@HelpForOtherActivity
        }

        activityHelpForOtherBinding.genderSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 -> helpForOtherViewModel.setGender("NONE")
                        1 -> helpForOtherViewModel.setGender("M")
                        2 -> helpForOtherViewModel.setGender("F")
                    }
                }

            }

        helpForOtherViewModel.getRecentStatus.observe(
            this,
            Observer { showRecentStatus(it) })
    }

    private fun showRecentStatus(data: List<RecentStatus>) {
        // list adapter
        val recentStatusAdapter = RecentStatusAdapter()

        // submit the list data
        recentStatusAdapter.submitList(data)

        // recyclerview
        activityHelpForOtherBinding.recentStatusRecyclerView.apply {
            adapter = recentStatusAdapter
            layoutManager = LinearLayoutManager(this@HelpForOtherActivity)
            hasFixedSize()
        }
    }
}
