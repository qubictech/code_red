package com.tarmsbd.schoolofthought.codered.app.ui.sos

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.tarmsbd.schoolofthought.codered.app.R
import kotlinx.android.synthetic.main.activity_s_o_s.*
import java.util.*
import java.util.logging.Logger


class SOSActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_RESULT = "com.tarmsbd.schoolofthought.codered.app.ui.sos.EXTRA_RESULT"
        private const val RED = "Red"
    }

    private var down: Long = 0
    private var up: Long = 0
    private lateinit var background: RelativeLayout
    private lateinit var btnWrapper: CoordinatorLayout
    private lateinit var progress: ProgressBar
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_o_s)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        background = findViewById(R.id.background)
        btnWrapper = findViewById(R.id.sos_btn_layout)
        progress = findViewById(R.id.progress_circular)
        progress.progress = 0

        if (intent.getStringExtra(EXTRA_RESULT) == RED) {
            sos_button.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_UP -> {
                        progress.progress = 0
                        up = System.currentTimeMillis()
                        if (up - down > 3000) {
                            blink()
//                        showFragment()
                        } else {
                            timer.cancel()
                            Toast.makeText(
                                this,
                                resources.getString(R.string.press_and_hold_the_sos_button_for_3_seconds),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    MotionEvent.ACTION_DOWN -> {
                        down = System.currentTimeMillis()
                        timer = getTimer(down + (30 * 1000))
                    }
                }
                return@setOnTouchListener true
            }
        } else {
            showFragment()
        }
    }

    private fun getTimer(milis: Long): Timer {
        val timer = Timer()
        var temp = 0
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                try {
                    val time = System.currentTimeMillis()
                    if (time < milis) {
                        runOnUiThread {
                            progress.progress = temp++
                        }
                    } else {
                        cancel()
                        progress.progress = 0
                    }

                    Logger.getLogger("PROGRESS").warning(".....")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }, 0, 30)

        return timer
    }

    private fun blink() {
        btnWrapper.visibility = View.GONE
        val anim: ObjectAnimator = ObjectAnimator
            .ofInt(background, "backgroundColor", R.color.colorPrimary, R.color.white)

        anim.duration = 2000
        anim.setEvaluator(ArgbEvaluator())
        anim.repeatMode = ValueAnimator.REVERSE
        anim.repeatCount = Animation.INFINITE
        anim.start()

        Handler().postDelayed({
            showFragment()
        }, 2000)
    }

    private fun showFragment() {
        background.visibility = View.GONE
        when (intent.getStringExtra(EXTRA_RESULT)) {
            RED -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ResultRedFragment())
                    .commit()
            }
            "Green" -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ResultGreen())
                    .commit()
            }
            else -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ResultNonRedFragment())
                    .commit()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
