package com.example.kenyangproject
import UsersAdapter
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.kenyangproject.databinding.ListUserActivityBinding
import com.google.firebase.database.*

class ListUserActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<Users>
    lateinit var listView: ListView

    private lateinit var binding: ListUserActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_user_activity)

        ref = FirebaseDatabase.getInstance().getReference("USERS")
        list = mutableListOf()
        listView = findViewById(R.id.rv_user)

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){

                    list.clear()
                    for (h in p0.children){
                        val user = h.getValue(Users::class.java)
                        list.add(user!!)
                    }
                    val adapter = UsersAdapter(this@ListUserActivity,R.layout.view_list_user,list)
                    listView.adapter = adapter
                }
            }
        })
    }
}