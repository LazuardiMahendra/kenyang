package com.example.kenyangproject.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.request.RequestOptions
import com.example.kenyangproject.Model.ContentMain
import com.example.kenyangproject.R
import com.example.kenyangproject.databinding.RegisterActivityBinding
import com.example.kenyangproject.databinding.ViewListDetailBinding
import com.google.firebase.firestore.FirebaseFirestore


class DetailViewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var contentMains: ArrayList<ContentMain> = arrayListOf()
    private var contentMainUidList: ArrayList<String> = arrayListOf()

    private var firestore: FirebaseFirestore? = null
    private var uid: String? = null

    init {
        firestore?.collection("images")?.orderBy("timestamp")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentMains.clear()
                contentMainUidList.clear()
                //somtimes, this code return null of querysnapshot when it signout
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot.documents) {
                    val item = snapshot.toObject(ContentMain::class.java)
                    contentMains.add(item!!)
                    contentMainUidList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
    }

    inner class ViewHolder(val binding: ViewListDetailBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ViewListDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        FirebaseFirestore.getInstance().collection("profileImage")
            .document(contentMains[position].uid!!).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val url = task.result!!["image"]
                    with(holder.itemView.context).load(url)
                        .apply(RequestOptions().circleCrop())
                        .into(holder.itemView.findViewById(R.id.iv_detailviewitem_profile))
                }
            }


    }


}