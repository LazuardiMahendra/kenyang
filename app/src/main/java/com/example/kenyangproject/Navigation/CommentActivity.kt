package com.example.kenyangproject.Navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kenyangproject.Model.ContentMain
import com.example.kenyangproject.R
import com.example.kenyangproject.databinding.CommentActivityBinding
import com.example.kenyangproject.databinding.ItemCommentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class CommentActivity : AppCompatActivity() {
    var contentUid: String? = null

    private lateinit var binding: CommentActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CommentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        contentUid = intent.getStringExtra("contentUid")

        binding.rvComment.adapter = CommentAdapter()
        binding.rvComment.layoutManager = LinearLayoutManager(this)

        binding.btCommentSend?.setOnClickListener {
            var comment = ContentMain.Comment()
            comment.userId = FirebaseAuth.getInstance().currentUser?.email
            comment.uid = FirebaseAuth.getInstance().currentUser?.uid
            comment.comment = binding.etCommentMessage.text.toString()
            comment.timestamp = System.currentTimeMillis()

            FirebaseFirestore.getInstance().collection("images").document(contentUid!!)
                .collection("comment").document().set(comment)
            binding.etCommentMessage.setText("")
        }
    }

    inner class CommentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var comments: ArrayList<ContentMain.Comment> = arrayListOf()

        init {
            FirebaseFirestore.getInstance().collection("images").document(contentUid!!)
                .collection("comment").orderBy("timestamp")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    comments.clear()
                    if (querySnapshot == null) return@addSnapshotListener
                    for (snapshot in querySnapshot.documents!!) {
                        comments.add(snapshot.toObject(ContentMain.Comment::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        inner class CustomViewHolder(val binding: ItemCommentBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            val binding =
                ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomViewHolder(binding)
        }


        override fun getItemCount(): Int {
            return comments.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            val viewHolder = (p0 as CustomViewHolder)
            viewHolder.binding.tvCommentMessage.text = comments[p1].comment
            viewHolder.binding.tvCommentProfileId.text = comments[p1].userId


            FirebaseFirestore.getInstance().collection("profileImage").document(comments[p1].uid!!)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var url = task.result!!["image"]
                        Glide.with(p0.itemView.context).load(url)
                            .apply(RequestOptions().circleCrop())
                            .into(viewHolder.binding.ivCommentProfile)
                    }

                }
        }

    }
}
