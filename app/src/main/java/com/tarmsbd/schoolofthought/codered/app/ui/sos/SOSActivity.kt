package com.tarmsbd.schoolofthought.codered.app.ui.sos

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.Animation
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tarmsbd.schoolofthought.codered.app.R
import kotlinx.android.synthetic.main.activity_s_o_s.*


class SOSActivity : AppCompatActivity() {
    private var down: Long = 0
    private var up: Long = 0
    private lateinit var background: RelativeLayout

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
        val anim: ObjectAnimator = ObjectAnimator
            .ofInt(background, "backgroundColor", Color.RED, Color.GREEN)

        anim.duration = 1500
        anim.setEvaluator(ArgbEvaluator())
        anim.repeatMode = ValueAnimator.REVERSE
        anim.repeatCount = Animation.INFINITE
        anim.start()
    }
}
