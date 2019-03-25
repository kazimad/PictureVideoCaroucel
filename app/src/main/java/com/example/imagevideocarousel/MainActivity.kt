package com.example.imagevideocarousel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.imagevideocarousel.interfaces.AmountChangeListener
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

}
