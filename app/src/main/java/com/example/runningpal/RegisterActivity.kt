package com.example.runningpal

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        btnApplyRegister.setOnClickListener {
            when {
                TextUtils.isEmpty(etRegisterEmail.text.toString()) -> {
                    Toast.makeText(
                            this,
                            "Proszę podać email",
                            Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(etRegisterPass.text.toString()) -> {
                    Toast.makeText(
                            this,
                            "Proszę podać hasło",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                else ->{

                    val nick:String = etRegisterNick.text.toString()
                    val email:String = etRegisterEmail.text.toString()
                    val password :String = etRegisterPass.text.toString()
                    //Dodaj jeszcze Walidację


                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener()
                        { task ->

                            if(task.isSuccessful)
                            {

                            val firebaseUser:FirebaseUser = task.result!!.user!!

                                Toast.makeText(
                                        this,
                                        "Poprawnie dodano użyytkownika do bazy",
                                        Toast.LENGTH_SHORT
                                ).show()


                               // addUserData(firebaseUser)
                                addUserToBase(email,nick,firebaseUser.uid)
                                // przekierowanie do main activity oraz dodanie flag aby nie powrócić tutaj spowrotem


                                ////
                                val intent =
                                        Intent(this@RegisterActivity, DashboardActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("email_id", email)
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

    fun addUserToBase(email:String,nick:String,uid:String){

        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference().child(uid)
        myRef.setValue(User(nick,email,uid))

    }

    fun addUserData(user : FirebaseUser) {

        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")

        val email = encodeUserEmail(user.email!!)

        val myRef = database.getReference().child("UserData").child(email!!)


        // tutaj inne randomowe dane
    /*   val userProfileChangeRequest= UserProfileChangeRequest.Builder()
                .setDisplayName("Test")
                .build()


        user!!.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Udalo sie update",Toast.LENGTH_SHORT).show()



                    }
                }
*/

        val  userData = UserData("", mutableListOf())

        myRef.setValue(userData)

    }

    fun encodeUserEmail(userEmail: String): String? {
        return userEmail.replace(".", ",")
    }

    fun decodeUserEmail(userEmail: String): String? {
        return userEmail.replace(",", ".")
    }
}