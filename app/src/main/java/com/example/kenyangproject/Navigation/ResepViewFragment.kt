package com.example.kenyangproject.Navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kenyangproject.R
import com.google.firebase.firestore.FirebaseFirestore

class ResepViewFragment : Fragment(){

    var firestore : FirebaseFirestore? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_resep,container,false)

        firestore = FirebaseFirestore.getInstance()


        return view
    }

}
