package com.example.maverickfilesender.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.maverickfilesender.R
import kotlinx.android.synthetic.main.activity_feedback.*
import javax.security.auth.Subject

class FeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

btn_sendFeedback.setOnClickListener {

    if(!et_feedback_subject.text.toString().isNullOrEmpty() && !et_feedback_message.text.toString().isNullOrEmpty()){
val subject=et_feedback_subject.text.toString()
        val body=et_feedback_message.text.toString()
sendEmail(arrayOf("dayodele89@gmail.com"),subject,body)


    }


}

    }


    fun sendEmail(address:Array<String>,subject: String,body:String){
        val intent=Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("mailto:"))
        intent.putExtra(Intent.EXTRA_EMAIL,address)
        intent.putExtra(Intent.EXTRA_SUBJECT,subject)
        intent.putExtra(Intent.EXTRA_TEXT,subject)
        startActivity(intent)



    }

}