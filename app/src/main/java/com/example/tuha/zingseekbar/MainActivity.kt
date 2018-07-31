package com.example.tuha.zingseekbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val random = Random(0)
        btn_change_progress.setOnClickListener {
            val progress = random.nextInt(zing_seek_bar.max)
            zing_seek_bar.progress = progress
        }
    }
}
