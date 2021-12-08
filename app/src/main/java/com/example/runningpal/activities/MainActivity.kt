package com.example.runningpal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.runningpal.R
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private  lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //przkierowanie usera w przypadku gdy jet on zalogowany lub nie
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser



        Handler().postDelayed({
            user?.let{
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
             if(user==null)  {
                 Timber.d("Jest NULL")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        },2000)

    }

}