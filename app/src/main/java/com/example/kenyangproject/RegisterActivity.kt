package com.example.kenyangproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.kenyangproject.databinding.FragmentProfilBinding
import com.example.kenyangproject.databinding.RegisterActivityBinding
import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btSave.setOnClickListener {
            register()
        }


    }

    private fun register() {
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "createUserWithEmail: success")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Succesfully New Account Create", Toast.LENGTH_SHORT).show()
            } else {
                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

    }


}
