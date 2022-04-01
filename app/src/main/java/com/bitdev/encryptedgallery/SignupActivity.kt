package com.bitdev.encryptedgallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var createAccountBTN: Button

    private lateinit var inputGroup: Array<EditText>

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        db = Firebase.firestore
        auth = Firebase.auth

        loadComponents()
    }

    private fun loadComponents(){
        firstName = findViewById(R.id.first_name_input)
        lastName = findViewById(R.id.last_name_input)
        email = findViewById(R.id.email_input)
        password = findViewById(R.id.password_input)
        confirmPassword = findViewById(R.id.confirm_password_input)
        createAccountBTN = findViewById(R.id.create_account_btn)

        inputGroup = arrayOf(firstName, lastName, email, password, confirmPassword)

        updateButton()
        for(input in inputGroup){
            input.doAfterTextChanged {
                updateButton()
            }
        }

        findViewById<TextView>(R.id.go_to_login).setOnClickListener{
            finish()
        }

        createAccountBTN.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            val confirmPassword = confirmPassword.text.toString()

            if(password != confirmPassword){
                Toast.makeText(applicationContext, "Passwords don't match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = it.user?.uid ?: return@addOnSuccessListener

                    val user = hashMapOf(
                        "first_name" to "${firstName.text}",
                        "last_name" to "${lastName.text}",
                        "email" to email,
                        "password" to password
                    )

                    db.collection("users").document(uid).set(user)
                        .addOnSuccessListener {
                            finish()
                        }
            }
        }
    }

    private fun updateButton(){
        if(!this::createAccountBTN.isInitialized) return

        var isEmpty = false
        for(input in inputGroup){
            if(input.text.isEmpty()) {
                isEmpty = true
                break
            }
        }

        if(isEmpty && !createAccountBTN.isEnabled) return

        if(isEmpty){
            disableCreateAccountButton()
        }else{
            enableCreateAccountButton()
        }
    }

    private fun disableCreateAccountButton(){
        if(!this::createAccountBTN.isInitialized) return
        createAccountBTN.alpha = .5f
        createAccountBTN.isEnabled = false
        createAccountBTN.isClickable = false
    }

    private fun enableCreateAccountButton(){
        if(!this::createAccountBTN.isInitialized) return
        createAccountBTN.alpha = 1f
        createAccountBTN.isEnabled = true
        createAccountBTN.isClickable = true
    }

}