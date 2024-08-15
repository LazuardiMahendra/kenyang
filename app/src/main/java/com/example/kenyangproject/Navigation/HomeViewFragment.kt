package com.example.kenyangproject.Navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kenyangproject.Model.ContentMain
import com.example.kenyangproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.kenyangproject.databinding.FragmentHomeBinding
import com.example.kenyangproject.databinding.ViewListDetailBinding
import com.google.firebase.firestore.Query
import java.util.ArrayList

class HomeViewFragment : Fragment() {

    private var firestore: FirebaseFirestore? = null
    private var uid: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        binding.rvDetailviewitem.adapter = DetailViewAdapter()
        binding.rvDetailviewitem.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    inner class DetailViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var contentMains: ArrayList<ContentMain> = arrayListOf()
        private var contentMainUidList: ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("images")?.orderBy("timestamp", Query.Direction.DESCENDING)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    contentMains.clear()
                    contentMainUidList.clear()
                    //somtimes, this code return null of querysnapshot when it signout
                    if (querySnapshot == null) return@addSnapshotListener
                    for (snapshot in querySnapshot.documents) {
                        var item = snapshot.toObject(ContentMain::class.java)
                        contentMains.add(item!!)
                        contentMainUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
        }

        //        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        inner class CustomViewHolder(val binding: ViewListDetailBinding) :
            RecyclerView.ViewHolder(binding.root)

        //        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
//            val view = LayoutInflater.from(p0.context).inflate(R.layout.view_list_detail, p0, false)
//            return CustomViewHolder(view)
//        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding =
                ViewListDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return contentMains.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            val viewholder = (p0 as CustomViewHolder)

            //Photo User
            FirebaseFirestore.getInstance().collection("profileImage")
                .document(contentMains[p1].uid!!).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val url = task.result!!["image"]
                        Glide.with(p0.itemView.context).load(url)
                            .apply(RequestOptions().circleCrop())
                            .into(viewholder.binding.ivDetailviewitemProfile)
                    }

                }

            viewholder.binding.tvDetailviewitemIsiBahan.text = contentMains!![p1].resepBahan
            viewholder.binding.tvDetailviewitemIsiAlat.text = contentMains!![p1].resepAlat
            viewholder.binding.tvDetailviewitemIsiLangkah.text = contentMains!![p1].resepLangkah
            viewholder.binding.tvDetailviewitemProfile.text = contentMains!![p1].userId

            Glide.with(p0.itemView.context).load(contentMains!![p1].resepImage)
                .into(viewholder.binding.ivDetailviewitemContent)

            //name content
            viewholder.binding.tvDetailviewitemNamecontent.text = contentMains!![p1].resepNama

            //like
            viewholder.binding.tvDetailviewitemFavoritecouter.text =
                "Likes " + contentMains!![p1].favoriteCount

            //this code is when the page loaded
            viewholder.binding.ivDetailviewitemFavorite.setOnClickListener {
                favoriteEvent(p1)
            }
            if (contentMains!![p1].favorites.containsKey(uid)) {
                //this is like status
                viewholder.binding.ivDetailviewitemFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                //this is if unlike
                viewholder.binding.ivDetailviewitemFavorite.setImageResource((R.drawable.ic_favorite_border))
            }

            //toolbar
            //this code is when the profile image in frame is clicked
            viewholder.binding.ivDetailviewitemProfile.setOnClickListener {
                var fragment = ProfilViewFragment()
                var bundle = Bundle()
                bundle.putString("destinationUid", contentMains[p1].uid)
                bundle.putString("userId", contentMains[p1].userId)
                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_main, fragment)?.commit()
            }

            viewholder.binding.ivDetailviewitemComment.setOnClickListener { v ->
                var intent = Intent(v.context, CommentActivity::class.java)
                intent.putExtra("contentUid", contentMainUidList[p1])
                startActivity(intent)
            }
            viewholder.binding.ivDetailviewitemDetail.setOnClickListener { v ->
                var intent = Intent(v.context, DetailContentActivity()::class.java)
                intent.putExtra("contentUidD", contentMainUidList[p1])
                startActivity(intent)
            }

        }

        fun favoriteEvent(position: Int) {
            var tsDoc = firestore?.collection("images")?.document(contentMainUidList[position])
            firestore?.runTransaction { transaction ->

                var contentMain = transaction.get(tsDoc!!).toObject(ContentMain::class.java)

                if (contentMain!!.favorites.containsKey(uid)) {
                    //when the button is clicked
                    contentMain.favoriteCount = contentMain?.favoriteCount?.minus(1)!!
                    contentMain.favorites?.remove(uid)
                } else {
                    //when yhe button is not clicked
                    contentMain?.favoriteCount = contentMain?.favoriteCount?.plus(1)!!
                    contentMain?.favorites?.set(uid!!, true)
                }
                transaction.set(tsDoc, contentMain)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
