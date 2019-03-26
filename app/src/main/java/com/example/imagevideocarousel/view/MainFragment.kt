package com.example.imagevideocarousel.view

import android.Manifest
import android.R
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.example.imagevideocarousel.interfaces.MainFragmentInterface
import com.kazimad.movieparser.utils.glide.Glider
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.File
import java.net.URI


class MainFragment : Fragment(), MainFragmentInterface {
    private lateinit var mainView: View
    private val mInterval = 3000L // 5 seconds by default, can be changed later
    private var mHandler: Handler? = null
    private var pics = arrayOf<File>()
    private var currentPick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler = Handler()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(com.example.imagevideocarousel.R.layout.fragment_main, container, false)
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isStoragePermissionGranted()) {
            findCameraDirrectory()
            startRepeatingTask()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        stopRepeatingTask()
    }

    private var mStatusChecker: Runnable = object : Runnable {
        override fun run() {

            Glider.downloadImage(pics[currentPick], imageView)
//            Glider.downloadImage(pics[currentPick], imageView2)

            val fadeOut = AnimationUtils.loadAnimation(activity!!, R.anim.fade_out)
            fadeOut.duration = mInterval

            val fadeIn = AnimationUtils.loadAnimation(activity!!, R.anim.fade_in)
            fadeIn.duration = mInterval

            imageView.animation = fadeOut
//            imageView2.animation = fadeIn
            fadeOut.start()
//            fadeIn.start()

            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    Log.d("myLog", "fadeOut onAnimationEnd")
                    if (imageView.alpha == 0f) {
                        Log.d("myLog", "fadeOut case 1")
                        imageView.animation = fadeIn
                    } else {
                        Log.d("myLog", "fadeOut case 2")
                        imageView2.animation = fadeIn
                    }
                    fadeIn.start()
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })

//            fadeIn.setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationRepeat(animation: Animation?) {
//                }
//
//                override fun onAnimationEnd(animation: Animation?) {
//                    Log.d("myLog", "fadeIn onAnimationEnd")
//                    if (imageView.alpha != 0f) {
//                        Log.d("myLog", "fadeIn case 1")
//                        imageView.animation = fadeOut
//                    } else {
//                        Log.d("myLog", "fadeIn case 2")
//                        imageView2.animation = fadeOut
//                    }
//                    fadeOut.start()
//                }
//
//                override fun onAnimationStart(animation: Animation?) {
//                }
//            })

//            if (imageView.visibility == View.INVISIBLE) {
//                imageView.visibility = View.VISIBLE
//                imageView.startAnimation(fadeOut)
//                imageView2.startAnimation(fadeIn)
//            } else{
//                imageView2.visibility = View.VISIBLE
//                imageView2.startAnimation(fadeOut)
//                imageView.startAnimation(fadeIn)
//            }

            currentPick++
            // 100% guarantee that this always happens, even if
            // your update method throws an exception
            mHandler?.postDelayed(this, mInterval)
        }
    }

    private fun startRepeatingTask() {
//        mStatusChecker.run()


        val fadeOut = AnimationUtils.loadAnimation(activity!!, R.anim.fade_out)
        fadeOut.duration = mInterval

        val fadeIn = AnimationUtils.loadAnimation(activity!!, R.anim.fade_in)
        fadeIn.duration = mInterval

        imageView.animation = fadeOut
//            imageView2.animation = fadeIn
        fadeOut.start()
//            fadeIn.start()

        Glider.downloadImage(pics[currentPick], imageView)
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                Log.d("myLog", "fadeOut onAnimationEnd")
                Glider.downloadImage(pics[currentPick], imageView2)
                if (currentPick == pics.size - 1) {
                    currentPick = 0
                } else {
                    currentPick++
                }
                if (imageView.alpha == 0f) {
                    Log.d("myLog", "fadeOut case 1")
                    imageView.visibility = View.INVISIBLE
                    imageView2.animation = fadeIn
                } else {
                    Log.d("myLog", "fadeOut case 2")
                    imageView.visibility = View.INVISIBLE
                    imageView2.animation = fadeIn
                }
                fadeIn.start()
//                Glider.downloadImage(pics[currentPick], imageView)


            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                Log.d("myLog", "fadeIn onAnimationEnd")
                fadeOut.start()
                Glider.downloadImage(pics[currentPick], imageView)
//                Glider.downloadImage(pics[currentPick], imageView2)
                if (currentPick == pics.size - 1) {
                    currentPick = 0
                } else {
                    currentPick++
                }
                if (imageView.alpha == 0f) {
                    Log.d("myLog", "fadeIn case 1")
                    imageView.animation = fadeOut
                    imageView2.visibility = View.INVISIBLE
                } else {
                    Log.d("myLog", "fadeIn case 2")
                    imageView2.animation = fadeOut
                    imageView.visibility = View.INVISIBLE

                }

            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

//        if (imageView.visibility == View.INVISIBLE) {
//            imageView.visibility = View.VISIBLE
//            imageView.startAnimation(fadeOut)
//            imageView2.startAnimation(fadeIn)
//        } else {
//            imageView2.visibility = View.VISIBLE
//            imageView2.startAnimation(fadeOut)
//            imageView.startAnimation(fadeIn)
//        }

    }

    private fun stopRepeatingTask() {
        mHandler?.removeCallbacks(mStatusChecker)
    }


    private fun findCameraDirrectory() {
        val cameraFolder = "Camera"
        var cameraDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString())
        Log.d("myLog", "cameraDirectory is $cameraDirectory")

        val files = cameraDirectory.listFiles()
        for (curFile in files) {
            if (curFile.isDirectory) {
                cameraDirectory = curFile
                break
            }
        }
        val completeCameraFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + cameraFolder
        val directoryCamera = File(completeCameraFolder)
        pics = directoryCamera.listFiles()
        if (pics.isNotEmpty()) {


            Log.d("myLog", "MainFragment pics.size ${pics.size}")
            Log.d("myLog", "MainFragment Uri.parse(pics[0].path) ${URI(pics[0].path)}")
            for (pic in pics) {
                Log.d("myLog", "MainFragment $pic")
            }
        } else {
            Log.d("myLog", "MainFragment pics == null")
        }

    }

    private fun readStorage() {
        Log.d("myLog", "MainFragment readStorage permission granted ")
        val dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
        val directory = File(dcim)
        Log.d("myLog", "dcim is $dcim")
//        if (dcim != null) {
        val pics = directory.listFiles()
        if (pics != null) {
            Log.d("myLog", "MainFragment pics.size ${pics.size}")
            for (pic in pics) {
                Log.d("myLog", "MainFragment $pic")
            }
        } else {
            Log.d("myLog", "MainFragment pics == null")
        }
//        } else {
//            Log.d("myLog", "MainFragment dcim == null")
//        }
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
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    override fun onReadPermissionGranted() {
        readStorage()
    }
}