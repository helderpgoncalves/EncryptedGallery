package com.bitdev.encryptedgallery.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bitdev.encryptedgallery.fragments.GalleryPageFragment
import com.bitdev.encryptedgallery.models.Action
import com.bitdev.encryptedgallery.models.User

class GalleryPageAdapter(val actions: List<Action>, val user: User, fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return actions.size
    }

    override fun createFragment(position: Int): Fragment {
        return GalleryPageFragment(actions[position], user)
    }
}