package com.example.imagevideocarousel.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.imagevideocarousel.R


object ActivityUtils {

    fun addFragmentToActivity(activity: FragmentActivity, fragment: Fragment, addToBackStack: Boolean) {
        addFragmentToActivity(activity, fragment, addToBackStack, R.id.fragmentContainer)
    }

    private fun addFragmentToActivity(activity: FragmentActivity, fragment: Fragment, addToBackStack: Boolean, containerId: Int) {
        val fragmentManager = activity.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commitAllowingStateLoss()
    }
}