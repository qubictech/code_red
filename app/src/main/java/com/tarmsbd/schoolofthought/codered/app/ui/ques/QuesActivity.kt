package com.tarmsbd.schoolofthought.codered.app.ui.ques

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.QuesViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityQuesBinding

class QuesActivity : AppCompatActivity() {
    private lateinit var quesViewModel: QuesViewModel
    private var id: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quesViewModel = ViewModelProvider(this)[QuesViewModel::class.java]

        val activityQuesBinding: ActivityQuesBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_ques)

        activityQuesBinding.apply {
            viewModel = quesViewModel
            lifecycleOwner = this@QuesActivity
        }

        quesViewModel.getQuestions.observe(this, Observer {
            id = it.id
        })
    }

    override fun onBackPressed() {
        if (id < 2) super.onBackPressed()
        else quesViewModel.setPreviousQues(id)
    }
}