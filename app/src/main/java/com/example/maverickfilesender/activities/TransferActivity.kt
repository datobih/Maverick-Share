package com.example.maverickfilesender.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.maverickfilesender.R
import kotlinx.android.synthetic.main.activity_transfer.*

class TransferActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)


imv_transfer_back.setOnClickListener {
    onBackPressed()

}


    }
}