package com.tarmsbd.schoolofthought.codered.app.ui.sos

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.ui.emergency.EmergencyActivity
import kotlinx.android.synthetic.main.fragment_result_green.*

class ResultGreen : Fragment(R.layout.fragment_result_green) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emergency_contacts.setOnClickListener {
            activity!!.startActivity(Intent(activity!!, EmergencyActivity::class.java))
        }
    }
}