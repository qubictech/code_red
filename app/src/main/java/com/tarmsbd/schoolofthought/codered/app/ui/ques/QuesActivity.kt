package com.tarmsbd.schoolofthought.codered.app.ui.ques

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.MainViewModel
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.QuesViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityQuesBinding
import com.tarmsbd.schoolofthought.codered.app.ui.sos.SOSActivity
import java.util.logging.Logger

class QuesActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
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

        quesViewModel.answeredList.observe(this, Observer { answers ->

            if (
                answers[0].ans.isNotEmpty() &&
                answers[1].ans.isNotEmpty() &&
                answers[2].ans.isNotEmpty() &&
                answers[3].ans.isNotEmpty() &&
                answers[4].ans.isNotEmpty()
            ) {
                val map = hashMapOf<String, String>()
                map["Question1_answer"] = answers[0].ans
                map["Question2_answer"] = answers[1].ans
                map["Question3_answer"] = answers[2].ans
                map["Question4_answer"] = answers[3].ans
                map["Question5_answer"] = answers[4].ans

                Logger.getLogger("QuesActivity:").warning("Requesting................")

                mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

                mainViewModel.getResponse(map).observe(this, Observer {
                    Logger.getLogger("QuesActivity: Result: ").warning(it.response)
                    Toast.makeText(this, it.response, Toast.LENGTH_LONG).show()

                    startActivity(Intent(this, SOSActivity::class.java))
                    finish()
                })
            }

        })


    }

    override fun onBackPressed() {
        if (id < 2) super.onBackPressed()
        else quesViewModel.setPreviousQues(id)
    }
}