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
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.RegistrationForSelfViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private lateinit var activityRegistrationBinding: ActivityRegistrationBinding
    private lateinit var registrationForSelfViewModel: RegistrationForSelfViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegistrationBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_registration)


        registrationForSelfViewModel =
            ViewModelProvider(this)[RegistrationForSelfViewModel::class.java]

        activityRegistrationBinding.apply {
            lifecycleOwner = this@RegistrationActivity
            viewModel = registrationForSelfViewModel
        }

        activityRegistrationBinding.genderSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 -> registrationForSelfViewModel.setGender("NONE")
                        1 -> registrationForSelfViewModel.setGender("M")
                        2 -> registrationForSelfViewModel.setGender("F")
                    }
                }

            }

        registrationForSelfViewModel.getRecentStatus.observe(
            this,
            Observer { showRecentStatus(it) })
    }

    private fun showRecentStatus(data: List<RecentStatus>) {
        // list adapter
        val recentStatusAdapter = RecentStatusAdapter()

        // submit the list data
        recentStatusAdapter.submitList(data)

        // recyclerview
        activityRegistrationBinding.recentStatusRecyclerView.apply {
            adapter = recentStatusAdapter
            layoutManager = LinearLayoutManager(this@RegistrationActivity)
            hasFixedSize()
        }
    }
}
