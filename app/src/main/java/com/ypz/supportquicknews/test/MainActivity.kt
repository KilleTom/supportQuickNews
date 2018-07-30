package com.ypz.supportquicknews.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CompoundButton
import com.ypz.supportquicknews.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val iamges = findViewById<ImageView>(R.id.iamges)
        Glide.with(this).load("http://imgs.juheapi.com/comic_xin/u/DTsMjM1d8=/139833/10-MTM5ODMzMTA=.jpg").into(iamges)
        var finshes = mutableListOf<String>("全部","连载","完结")*/
        checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener({ compoundButton: CompoundButton, b: Boolean ->
            if (b) checkbox.background = getDrawable(R.drawable.comic_is_check)
            else checkbox.background = getDrawable(R.drawable.comic_not_check)
        }))

    }

}
