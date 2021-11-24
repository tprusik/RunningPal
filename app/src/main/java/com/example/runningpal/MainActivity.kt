package com.example.runningpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.runningpal.fragments.TrackingFragment
import com.example.runningpal.others.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private  lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        
        //przkierowanie usera w przypadku gdy jet on zalogowany lub nie
        mAuth = FirebaseAuth.getInstance()

        val user = mAuth.currentUser

        Handler().postDelayed({
            if(user!=null){
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }

            else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        },2000)

    }

}