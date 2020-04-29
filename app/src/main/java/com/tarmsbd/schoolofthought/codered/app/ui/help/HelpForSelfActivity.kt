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
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.HelpForSelfViewModel
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.MainViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityHelpForSelfBinding
import java.util.logging.Logger

class HelpForSelfActivity : AppCompatActivity() {
    private lateinit var activityHelpForSelfBinding: ActivityHelpForSelfBinding
    private lateinit var helpForSelfViewModel: HelpForSelfViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHelpForSelfBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_help_for_self)

        helpForSelfViewModel =
            ViewModelProvider(this)[HelpForSelfViewModel::class.java]
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        activityHelpForSelfBinding.apply {
            lifecycleOwner = this@HelpForSelfActivity
            viewModel = helpForSelfViewModel
        }

        val map = hashMapOf<String, String>()
        map["Question1_answer"] = "No"
        map["Question2_answer"] = "No"
        map["Question3_answer"] = "Yes"
        map["Question4_answer"] = "No"
        map["Question5_answer"] = "No"

        mainViewModel.getResponse(map).observe(this, Observer {
            Logger.getLogger("Response").warning(it.toString())
        })

        activityHelpForSelfBinding.genderSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 -> helpForSelfViewModel.setGender("NONE")
                        1 -> helpForSelfViewModel.setGender("M")
                        2 -> helpForSelfViewModel.setGender("F")
                    }
                }

            }

        helpForSelfViewModel.getRecentStatus.observe(
            this,
            Observer { showRecentStatus(it) })
    }

    private fun showRecentStatus(data: List<RecentStatus>) {
        // list adapter
        val recentStatusAdapter = RecentStatusAdapter()

        // submit the list data
        recentStatusAdapter.submitList(data)

        // recyclerview
        activityHelpForSelfBinding.recentStatusRecyclerView.apply {
            adapter = recentStatusAdapter
            layoutManager = LinearLayoutManager(this@HelpForSelfActivity)
            hasFixedSize()
        }
    }
}
