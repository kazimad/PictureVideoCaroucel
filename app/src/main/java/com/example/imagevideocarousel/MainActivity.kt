package com.example.imagevideocarousel

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("myLog", "Permission: " + permissions[0] + "was " + grantResults[0])
            val f = ActivityUtils.getFragmentInActivity(this)
            if (f is MainFragmentInterface) {
                // do something with f
                (f as MainFragmentInterface).onReadPermissionGranted()
            }
        } else {
            ActivityUtils.showPopErrorMessage(this@MainActivity, getString(R.string.error_need_permission))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragmentManager = this.supportFragmentManager
        if (fragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

}
