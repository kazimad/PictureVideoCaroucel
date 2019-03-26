package com.example.imagevideocarousel

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.imagevideocarousel.interfaces.AmountChangeListener
import com.example.imagevideocarousel.interfaces.MainFragmentInterface
import com.example.imagevideocarousel.utils.ActivityUtils
import com.example.imagevideocarousel.view.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addMainFragment()
    }

    private fun addMainFragment() {
        ActivityUtils.addFragmentToActivity(this, MainFragment(), true)
    }

    private fun createAndShowChangeDialog(
        headerText: String,
        amountText: Int,
        amountChangeListener: AmountChangeListener
    ) {
        val changeDialog = ChangeDialog.newInstance(headerText, amountText)
        changeDialog.setParams(amountChangeListener)
        changeDialog.show(this@MainActivity.supportFragmentManager, "changeDialog")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("myLog", "Permission: " + permissions[0] + "was " + grantResults[0])
            val f = ActivityUtils.getFragmentInActivity(this)
            if (f is MainFragmentInterface) {
                // do something with f
                (f as MainFragmentInterface).onReadPermissionGranted()
            }
        }
    }
}
