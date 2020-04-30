package com.tarmsbd.schoolofthought.codered.app.ui.sos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tarmsbd.schoolofthought.codered.app.R
import kotlinx.android.synthetic.main.activity_s_o_s.*

class SOSActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_o_s)

        sos_button.setOnTouchListener { view, motionEvent ->
            return@setOnTouchListener true
        }
    }
}
