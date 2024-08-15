package com.example.kenyangproject.Navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kenyangproject.R

class DetailContentActivity : AppCompatActivity(){
    var contentUidD: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_resep_activity)
        contentUidD = intent.getStringExtra("contentUidD")


    }
}