package com.example.kenyangproject.Adapter

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.kenyangproject.MainActivity
import com.example.kenyangproject.R
import com.example.kenyangproject.Users
import com.google.firebase.database.FirebaseDatabase

class ResepAdapter(val mCtx: Context, val layoutResId: Int, val list: List<Users> )
    : ArrayAdapter<Users>(mCtx,layoutResId,list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val textUsername = view.findViewById<TextView>(R.id.tv_item_username)
        val textEmail = view.findViewById<TextView>(R.id. tv_item_email)
        val textTelp = view.findViewById<TextView>(R.id. tv_item_telp)

        val textUpdate = view.findViewById<Button>(R.id.tv_user_update)
        val textDelete = view.findViewById<Button>(R.id.tv_user_delete)

        val user = list[position]

        textUsername.text = user.us
        textEmail.text = user.email
        textTelp.text = user.telp

        textUpdate.setOnClickListener {
            showUpdateDialog(user)
        }
        textDelete.setOnClickListener {
            Deleteinfo(user)
        }

        return view
    }

    private fun Deleteinfo(user: Users) {
        val progressDialog = ProgressDialog(context,
            com.google.android.material.R.style.Theme_MaterialComponents_Light_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Deleting...")
        progressDialog.show()
        val mydatabase = FirebaseDatabase.getInstance().getReference("USERS")
        mydatabase.child(user.id).removeValue()
        Toast.makeText(mCtx,"Deleted!!",Toast.LENGTH_SHORT).show()
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)

    }

    private fun showUpdateDialog(user: Users) {
        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle("Update")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.update_user, null)

        val textUsername = view.findViewById<EditText>(R.id.add_username)
        val textEmail = view.findViewById<EditText>(R.id.add_email)
        val textTelp = view.findViewById<EditText>(R.id.add_telp)

        textUsername.setText(user.us)
        textEmail.setText(user.email)
        textTelp.setText(user.telp)

        builder.setView(view)

        builder.setPositiveButton("Update") { dialog, which ->

            val dbUsers = FirebaseDatabase.getInstance().getReference("USERS")

            val us = textUsername.text.toString().trim()

            val email = textEmail.text.toString().trim()

            val telp = textTelp.text.toString().trim()

            if (us.isEmpty()){
                textUsername.error = "please enter name"
                textUsername.requestFocus()
                return@setPositiveButton
            }

            if (email.isEmpty()){
                textEmail.error = "please enter status"
                textEmail.requestFocus()
                return@setPositiveButton
            }

            if (telp.isEmpty()){
                textTelp.error = "please enter status"
                textTelp.requestFocus()
                return@setPositiveButton
            }

            val user =
                Users(user.id, us, email, telp)

            dbUsers.child(user.id).setValue(user).addOnCompleteListener {
                Toast.makeText(mCtx,"Updated",Toast.LENGTH_SHORT).show()
            }

        }

        builder.setNegativeButton("No") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()

    }
}


