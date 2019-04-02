package com.example.imagevideocarousel.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.example.imagevideocarousel.R
import com.example.imagevideocarousel.dialogs.ChangeDialog
import com.example.imagevideocarousel.interfaces.AmountChangeListener
import com.example.imagevideocarousel.interfaces.MainFragmentInterface
import com.example.imagevideocarousel.utils.ActivityUtils
import com.example.imagevideocarousel.utils.Constants
import com.example.imagevideocarousel.utils.glide.Glider
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.File


class MainFragment : Fragment(), MainFragmentInterface, AmountChangeListener {

    private lateinit var mainView: View
    private lateinit var mHandler: Handler
    private lateinit var fadeOut: Animation
    private lateinit var fadeIn: Animation
    private var mInterval = 6000L // 6 seconds by default, can be changed later
    private var pics = arrayOf<File>()
    private var currentPick = 0
    var loadTo: Int = first


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mHandler = Handler()
        fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeOut.duration = 3000
        fadeOut.fillAfter = true

        fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = 3000
        fadeIn.fillAfter = true


        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                performVisibleStuff()
                loadTo = first

            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                performInvisibleStuff()
                loadTo = second

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })

    }

    fun performInvisibleStuff() {
        mHandler.postDelayed({
            if (currentPick == pics.size - 1) {
                currentPick = 0
            } else {
                currentPick++
            }
            performLoad()
            loadTo = if (loadTo == first) {
                second
            } else {
                first
            }
            imageView.startAnimation(fadeIn)
            imageView2.startAnimation(fadeOut)
        }, mInterval)
    }

    fun performVisibleStuff() {
        mHandler.postDelayed({
            imageView.startAnimation(fadeOut)
            imageView2.startAnimation(fadeIn)

        }, mInterval)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(com.example.imagevideocarousel.R.layout.fragment_main, container, false)
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isStoragePermissionGranted()) {
            findCameraDirectory()
            startRepeatingTask()
        }
    }

    private fun performLoad() {
        if (loadTo != first) {
            Glider.downloadImage(pics[currentPick], imageView)
        } else {
            Glider.downloadImage(pics[currentPick], imageView2)
        }
    }

    private fun startRepeatingTask() {
        performLoad()
        currentPick++
        performInvisibleStuff()
        loadTo = second
    }


    private fun findCameraDirectory() {
        val cameraFolder = "Camera"
        val cameraDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString())
        val files = cameraDirectory.listFiles()
        for (curFile in files) {
            if (curFile.isDirectory) {
                break
            }
        }
        val completeCameraFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + cameraFolder
        val directoryCamera = File(completeCameraFolder)
        pics = directoryCamera.listFiles()
        if (pics.isNotEmpty()) {
        } else {
            activity?.let {
                ActivityUtils.showPopErrorMessage(activity!!, activity!!.getString(R.string.error_no_pics))
            }
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                false
            }
        } else {
            true
        }
    }

    override fun onReadPermissionGranted() {
        findCameraDirectory()
        startRepeatingTask()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                createAndShowChangeDialog(
                    getString(
                        R.string.dialog_header,
                        (mInterval / 1000).toString(),
                        Constants.minTime.toString(),
                        Constants.maxTime.toString()
                    ),
                    mInterval,
                    this
                )
                return true
            }
        }
        return false
    }

    override fun onIntervalChanged(amount: Int) {
        mInterval = (amount*1000).toLong()
    }

    private fun createAndShowChangeDialog(
        headerText: String,
        amountText: Long,
        amountChangeListener: AmountChangeListener
    ) {
        activity?.let {
            val changeDialog = ChangeDialog.newInstance(headerText, amountText)
            changeDialog.setParams(amountChangeListener)
            changeDialog.show(activity!!.supportFragmentManager, "changeDialog")
        }
    }

    companion object {
        const val first = 1
        const val second = 2
    }
}