package com.bitdev.encryptedgallery.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2
import com.bitdev.encryptedgallery.R
import com.bitdev.encryptedgallery.Utils
import com.bitdev.encryptedgallery.adapters.GalleryPageAdapter
import com.bitdev.encryptedgallery.models.Action
import com.bitdev.encryptedgallery.models.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class GalleryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var user: User

    private lateinit var mPager: ViewPager2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        auth = Firebase.auth
        db = Firebase.firestore

        mPager = findViewById(R.id.gallery_pager)

        if(auth.currentUser == null){
            finish()
            return
        }

        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {

            if(it.data == null) return@addOnSuccessListener

            user = User(
                auth.currentUser!!.uid,
                it.data!!["email"] as String,
                it.data!!["first_name"] as String,
                it.data!!["last_name"] as String,
                it.data!!["gallery"] as List<String>
            )

            var a = mutableListOf<Action>()

            for(i in 0..user.gallery.size){
                a.add(Action("$i", user.gallery))
            }

            //updateGallery()

            val pagerAdapter = GalleryPageAdapter(a,user, this)
            mPager.adapter = pagerAdapter
        }

    }



}