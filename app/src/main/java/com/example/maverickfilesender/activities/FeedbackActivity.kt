package com.example.maverickfilesender.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.maverickfilesender.R
import com.example.maverickfilesender.databinding.ActivityFeedbackBinding
import javax.security.auth.Subject

class FeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFeedbackBack.setOnClickListener {

            onBackPressed()

        }

binding.btnSendFeedback.setOnClickListener {

    if(!binding.etFeedbackSubject.text.toString().isNullOrEmpty() && !binding.etFeedbackMessage.text.toString().isNullOrEmpty()){
val subject=binding.etFeedbackSubject.text.toString()
        val body=binding.etFeedbackMessage.text.toString()
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