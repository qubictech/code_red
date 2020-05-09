package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tarmsbd.schoolofthought.codered.app.data.models.Question
import java.util.logging.Logger

class QuesViewModel : ViewModel() {

    private val questionList = mutableListOf(
        Question(
            1,
            "Are you suffering from mild or severe fever/ cold or only cough with sore throat?",
            "(আপনার কি জ্বর বা সর্দি অথবা শুধুমাত্র কাশির সাথে গলা ব্যথা হচ্ছে?)",
            ""
        ),
        Question(
            2,
            "Did you develop excessive fatigue or stomach pain or headache with or without diarrhoea and vomiting?",
            "(আপনি কি অতিরিক্ত দূর্বলতা অথবা পেট বা মাথা ব্যাথা হচ্ছে কিংবা সাথে ডায়রিয়া এবং বমিও হচ্ছে?)",
            ""
        ),
        Question(
            3,
            "Do you think your ability for smelling and testing food has been reduced or completely lost? Or Have you noticed any red rashes on your body?",
            "(আপনার কি ইদানিং গন্ধ কিংবা স্বাদ অনুভব করতে অসুবিধা হচ্ছে? অথবা আপনার শরীরে কি লাল লাল ফুসকুড়ি দেখা যাচ্ছে?)",
            ""
        ),
        Question(
            4,
            "Are you feeling excessive weakness/ palpitation and difficulties during breathing?",
            "আপনি কি অতিরিক্ত দূর্বলতা, বুক ধরফরানি এবং শ্বাস-প্রশ্বাস নিতে সমস্যা অনুভব করছেন?",
            ""
        ),
        Question(
            5,
            "Are you already suffering from asthma?",
            "আপনার কি আগে থেকে গুরুতর ওজমা/ হাঁপানি আছে?",
            ""
        )
    )

    private var mAnsweredList = MutableLiveData<List<Question>>()

    private var mQuestion = MutableLiveData<Question>()

    var getQuestions = mQuestion

    fun btnYesClick(position: Int) {
        updateQuesAns(position, "Yes")
    }

    fun btnNoClick(position: Int) {
        updateQuesAns(position, "No")
    }

    fun setPreviousQues(position: Int) {
        // position is decreasing on updateQuesAns function body.
        updateQuesAns(position, "")

        // position is starts from 1. and index starts from 0.
        mQuestion.value = questionList[position - 2]
    }

    var answeredList: LiveData<List<Question>> = mAnsweredList

    private fun updateQuesAns(position: Int, ans: String) {
        // update the answered ques ans
        val question = questionList[position - 1]
        question.ans = ans
        questionList[position - 1] = question
        Logger.getLogger("QuesRepo: Update")
            .warning("Ques-${position}: Ans: ${questionList[position - 1].ans}\n")
        mQuestion.value = question
    }

    fun loadNextQuestion(position: Int) {
        if (questionList[position - 1].ans.isEmpty()) return

        mAnsweredList.value = questionList
        if (position == 4 && questionList[position - 1].ans == "No") {
            return
        }

        if (position == questionList.size) return
        mQuestion.value = questionList[position]
    }

    init {
        mQuestion.value = questionList.first()
        mAnsweredList.value = questionList
    }

    companion object {
        @JvmStatic
        @BindingAdapter("yesButtonClicked")
        fun setBackgroundColorYes(view: TextView, ans: String) {
            if (ans == "Yes") {
                view.background.setColorFilter(
                    Color.parseColor("#EE2B60"),
                    PorterDuff.Mode.SRC_ATOP
                )
                view.setTextColor(Color.WHITE)

            } else {
                view.background.setColorFilter(
                    Color.parseColor("#CCCCCC"),
                    PorterDuff.Mode.SRC_ATOP
                )
                view.setTextColor(Color.BLACK)
            }

        }

        @JvmStatic
        @BindingAdapter("noButtonClicked")
        fun setBackgroundColorNo(view: TextView, ans: String) {
            if (ans == "No") {
                view.background.setColorFilter(
                    Color.parseColor("#EE2B60"),
                    PorterDuff.Mode.SRC_ATOP
                )
                view.setTextColor(Color.WHITE)
            } else {
                view.background.setColorFilter(
                    Color.parseColor("#CCCCCC"),
                    PorterDuff.Mode.SRC_ATOP
                )
                view.setTextColor(Color.BLACK)
            }

        }
    }
}