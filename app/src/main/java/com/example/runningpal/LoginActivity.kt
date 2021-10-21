package com.example.runningpal

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {


    // ?
    companion object{
        private const val RC_SIGN_IN = 120

    }
    private  lateinit var mAuth : FirebaseAuth
    private  lateinit var googleSignInClient :GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = FirebaseAuth.getInstance()



        btnGoogleSignIn.setOnClickListener {
            signIn()
        }

        btn_signIn_GotoRegister.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }


        // Email SignIn

        btn_signIn_login.setOnClickListener {
            when {
                TextUtils.isEmpty(et_login_email.text.toString()) -> {
                    Toast.makeText(
                            this,
                            "Proszę podać email",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(etlogin_pass.text.toString()) -> {
                    Toast.makeText(
                            this,
                            "Proszę podać hasło",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                else ->{

                    val email:String = et_login_email.text.toString()
                    val password :String = etlogin_pass.text.toString()
                    //Dodaj jeszcze Walidację

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener()
                            {task ->

                                if(task.isSuccessful)
                                {

                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                            this,
                                            "Sukces logowanie emao;",
                                            Toast.LENGTH_SHORT
                                    ).show()

                                    // przekierowanie do main activity oraz dodanie flag aby nie powrócić tutaj spowrotem

                                    val intent =
                                            Intent(this@LoginActivity,DashboardActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id",firebaseUser.uid)
                                    intent.putExtra("email_id",email)
                                    startActivity(intent)
                                    finish()

                                } else{
                                    Toast.makeText(
                                            this,
                                            task.exception!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                }
            }
        }


    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }

            }
            else{
                Log.w("SignInGoogle",exception.toString())
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInGoogle", "signInWithCredential:success")
                    Toast.makeText(
                        this,
                        "Logowanie Google Sukces",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent =
                            Intent(this@LoginActivity,DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    finish()

                } else {

                    Toast.makeText(
                            this,
                            "Logowanie Google Niepoprawnie",
                            Toast.LENGTH_SHORT
                    ).show()

                    // If sign in fails, display a message to the user.
                    Log.w("SignInGoogle", "signInWithCredential:failure", task.exception)

                }
            }
    }

}