package com.example.runningpal.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants.ACTION_FROM_REGISTER
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.ext.android.get
import timber.log.Timber

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel : RunnersViewModel

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
                    val weight :String = etRegisterWeight.text.toString()
                    //Dodaj jeszcze Walidację


                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener()
                        { task ->
                            if(task.isSuccessful)
                            {

                            val firebaseUser:FirebaseUser = task.result!!.user!!

                                Toast.makeText(
                                        this,
                                        "Rejestracja poprawna",
                                        Toast.LENGTH_SHORT
                                ).show()

                                Timber.d(   "rejestracja "+ firebaseUser.uid)
                                val user = User(email,nick,null,null,firebaseUser.uid,weight, mutableListOf())

                                viewModel = get()
                                viewModel.insertUser(user)

                                 Intent(this, LoginActivity::class.java).also {
                                    it.action = ACTION_FROM_REGISTER
                                    startActivity(it) }

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

}