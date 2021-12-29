package com.example.runningpal.activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants
import com.example.runningpal.others.Constants.KEY_USER_AVATAR
import com.example.runningpal.others.Constants.KEY_USER_BCK
import com.example.runningpal.others.Constants.KEY_USER_ID
import com.example.runningpal.others.Constants.KEY_USER_NAME
import com.example.runningpal.others.Constants.KEY_USER_WEIGHT
import com.example.runningpal.others.Constants.SHARED_PREFERENCES_NAME
import com.example.runningpal.others.DbConstants
import com.example.runningpal.repositories.RunnersRepository
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.get
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    companion object{ private const val RC_SIGN_IN = 120 }

    private  lateinit var mAuth : FirebaseAuth
    private  lateinit var googleSignInClient :GoogleSignInClient
    private lateinit var viewModel : RunnersViewModel
    private lateinit var repository : RunnersRepository
    private lateinit var user : User
    private lateinit var id : String
    private lateinit var email : String

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

                    email = et_login_email.text.toString()
                    val password :String = etlogin_pass.text.toString()
                    //Dodaj jeszcze Walidację

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener()
                            {   task ->
                                if(task.isSuccessful) {
                                    user = User()

                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    id = firebaseUser.uid
                                    getCurrentUserObject()
                                    Timber.d("logowania"+ firebaseUser.uid)


                                    // przekierowanie do main activity oraz dodanie flag aby nie powrócić tutaj spowrotem

                                    Timber.d("logowania"+ firebaseUser.uid)

                                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
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

   private fun getCurrentUserObject(){

       var database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL)

        database.getReference(DbConstants.DB_NODE_USER)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children) {

                            var currentUser = snap.getValue(User::class.java)

                            if (currentUser!!.uid == id) {
                                user= currentUser
                                getUserDataFromDb()
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

        Timber.d("user " + user.nick)

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



     fun getUserDataFromDb(){

        Timber.d("hejZapisuje "+  user.nick)
        user.nick?.let { saveUserSharedPrevs(KEY_USER_NAME,it) }
        user.uid?.let {saveUserSharedPrevs(KEY_USER_ID,it) }
         user.weight?.let {saveUserSharedPrevs(KEY_USER_WEIGHT,it) }
        user.profilePic?.let { saveUserSharedPrevs(KEY_USER_AVATAR,it) }
        user.backgroundPic?.let { saveUserSharedPrevs(KEY_USER_BCK,it) }

    }


    fun saveUserSharedPrevs(key: String,value: String){
        val sharedPreferences: SharedPreferences.Editor = applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        Timber.d("prefsLog "+ value + " sss")
        sharedPreferences.putString(key,value)

        sharedPreferences.commit()

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
                            Intent(this@LoginActivity, DashboardActivity::class.java)
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