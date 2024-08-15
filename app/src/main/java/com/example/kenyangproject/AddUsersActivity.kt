package com.example.kenyangproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kenyangproject.databinding.AddUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUsersActivity : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    private lateinit var binding: AddUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ref = FirebaseDatabase.getInstance().getReference("USERS")

        binding.btSaveUser.setOnClickListener {
            savedata()
        }
    }

    private fun savedata() {
        val username = binding.addUsername.text.toString()
        val email = binding.addEmail.text.toString()
        val telp = binding.addTelp.text.toString()

        val userId = ref.push().key.toString()
        val user =
            Users(userId, username, email, telp)


        ref.child(userId).setValue(user).addOnCompleteListener {
            Toast.makeText(this, "Successs", Toast.LENGTH_SHORT).show()
            binding.addUsername.setText("")
            binding.addTelp.setText("")
            binding.addEmail.setText("")
        }
    }
}
