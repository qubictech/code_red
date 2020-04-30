package com.tarmsbd.schoolofthought.codered.app.ui.sos

import android.os.Bundle
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tarmsbd.schoolofthought.codered.app.R
import kotlinx.android.synthetic.main.activity_s_o_s.*
import java.util.logging.Logger

class SOSActivity : AppCompatActivity() {
    private var down: Long = 0
    private var up: Long = 0
    private lateinit var background: RelativeLayout

    private val colors = arrayOf(R.color.colorPrimary, R.color.colorPrimaryDark)
    private var temp = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_o_s)
        background = findViewById(R.id.background)

        sos_button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    up = System.currentTimeMillis()
                    if (up - down > 3000) {
                        blink()
                    } else {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.press_and_hold_the_sos_button_for_3_seconds),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                MotionEvent.ACTION_DOWN -> {
                    down = System.currentTimeMillis()
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun blink() {
        val start = System.currentTimeMillis()
        var loop = System.currentTimeMillis()

        while ((loop - start) < 3000) {
            loop = System.currentTimeMillis()

            if (temp % 2 == 0) {
                background.setBackgroundColor(colors[0])
            } else if (temp % 2 == 1) background.setBackgroundColor(colors[1])
            else Logger.getLogger("SOS").warning("${temp % 2}")

            temp++
        }
    }
}
