package com.bitdev.encryptedgallery.activities

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.bitdev.encryptedgallery.R
import com.bitdev.encryptedgallery.Utils
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

    private val attachFileResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode == Activity.RESULT_OK){
            if(it.data != null){
                newPhoto(it.data?.data!!)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)


        auth = Firebase.auth
        db = Firebase.firestore

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

            updateGallery()
        }

        findViewById<ImageView>(R.id.gallery_add_btn).setOnClickListener {
            attachFileResult.launch(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"))
        }


    }

    private fun updateGallery() {
        val storageImages = FirebaseStorage.getInstance().getReference("files")


        if(user.gallery.size < 4){
            var gallery = user.gallery.toMutableList()
            while(gallery.size < 4) {
                gallery.add("default.jpg")
            }
            user.gallery = gallery
        }

        val photo1 = findViewById<ImageView>(R.id.gallery_photo_1)
        val photo2 = findViewById<ImageView>(R.id.gallery_photo_2)
        val photo3 = findViewById<ImageView>(R.id.gallery_photo_3)
        val photo4 = findViewById<ImageView>(R.id.gallery_photo_4)

        storageImages.child("${user.gallery[0]}").downloadUrl.addOnSuccessListener {
          Glide.with(applicationContext).load(it).into(photo1)
        }

        storageImages.child("${user.gallery[1]}").downloadUrl.addOnSuccessListener {
            Glide.with(applicationContext).load(it).into(photo2)
        }

        storageImages.child("${user.gallery[2]}").downloadUrl.addOnSuccessListener {
            Glide.with(applicationContext).load(it).into(photo3)
        }

        storageImages.child("${user.gallery[3]}").downloadUrl.addOnSuccessListener {
            Glide.with(applicationContext).load(it).into(photo4)
        }

    }


    private fun newPhoto(fileUri: Uri){
        var filename = Utils.getCurrentTime()

        val storageRef = FirebaseStorage.getInstance().getReference("files/${filename}")
            storageRef.putFile(fileUri).addOnSuccessListener {
                var gallery = user.gallery.toMutableList()


                if(gallery.size < 4){
                    gallery.add(filename)
                }else{
                    for(i in 3 downTo 1){
                        if(gallery.size - 1 < i) continue
                        gallery[i] = gallery[i-1]
                    }

                    gallery[0] = filename
                }


                db.collection("users").document(user.uid).update("gallery",gallery).addOnSuccessListener {
                    Log.d("BITDEBUG","Updated gallery succefully")
                    user.gallery = gallery
                    updateGallery()
                }
        }

    }
}