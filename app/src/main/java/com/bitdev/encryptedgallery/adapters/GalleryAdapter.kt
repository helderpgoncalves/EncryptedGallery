package com.bitdev.encryptedgallery.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bitdev.encryptedgallery.R
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlin.math.log

class GalleryAdapter(val photos: List<String>): RecyclerView.Adapter<GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {

        val cPos = position*2

        var photo1 = photos[position*2]
        var photo2: String = ""

        Log.d("BITDEBUG", "$cPos $position")

        if(cPos+1 != photos.size){
            photo2 = photos[position*2+1]
        }

        val storageImages = FirebaseStorage.getInstance().getReference("files")


        holder.apply {

            storageImages.child(photo1).downloadUrl.addOnSuccessListener {
                Glide.with(holder.itemView.context).load(it).into(imageTop)
            }

            if(cPos+1 != photos.size){
                storageImages.child(photo2).downloadUrl.addOnSuccessListener {
                    Glide.with(holder.itemView.context).load(it).into(imageBottom)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        if(photos.size%2 == 0) return photos.size/2

        return photos.size/2 + 1
    }

}

class GalleryViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
    val imageTop = itemview.findViewById<ImageView>(R.id.photo_01)
    val imageBottom = itemview.findViewById<ImageView>(R.id.photo_02)
}