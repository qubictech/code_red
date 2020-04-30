package com.tarmsbd.schoolofthought.codered.app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tarmsbd.schoolofthought.codered.app.data.models.Question

object QuesRepository {
    private val questionList = mutableListOf(
        Question(
            1,
            "Did you come in contact with a Corona Virus infected person" +
                    "or any of his/ her family member of that infected person recently?",
            ""
        ),
        Question(
            2,
            "Are you suffering from only mild or serve Cold/ Fever/ Cough or Frequent Diarrhoea and Vomiting" +
                    "from 7th to 8th days along with any of these three symptoms?",
            ""
        ),
        Question(
            3,
            "Are you feeling any sudden Alteration or Complete loss of sense of smell and Alteration or" +
                    "Complete loss of sense of taste",
            ""
        ),
        Question(
            4,
            "Are you feeling excessive Weakness/ Fatigue/ Heart Palpitation and extreme difficulties" +
                    "during Breathing?",
            ""
        ),
        Question(
            5,
            "Are you already suffering from asthma?",
            ""
        )
    )

    private var mQuestion = MutableLiveData<Question>()

    init {
        mQuestion.value = questionList.first()
    }

    var getQuestion: LiveData<Question> = mQuestion

    fun setPreviousQues(position: Int) {
        mQuestion.value = questionList[position - 2]
    }

    fun updateQuesAns(position: Int, ans: String) {
        // update the answered ques ans
        val question = questionList[position - 1]
        question.ans = ans
        questionList[position - 1] = question

        if (position == questionList.size) return
        mQuestion.value = questionList[position]
    }
}