package com.example.tuha.zingseekbar

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var state = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btn_start_progress.setOnClickListener {
            if (state == 0) {
                state = 1
                startProgress()
            }
        }
        btn_stop_progress.setOnClickListener {
            if (state == 1) {
                state = 0
                stopProgress()
            }
        }
    }
    private val runnable = object : Runnable {
        override fun run() {
            val progress = if (zing_seek_bar.progress >= zing_seek_bar.max) {
                0
            } else {
                zing_seek_bar.progress + 1
            }
            zing_seek_bar.progress = progress

            handler.postDelayed(this,1000)
        }
    }

    private fun startProgress() {
        handler.post(runnable)
    }

    private fun stopProgress() {
        handler.removeCallbacks(runnable)
    }
}
