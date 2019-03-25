package com.example.imagevideocarousel.view

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.io.File


class MainFragment : Fragment() {

    lateinit var mainView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(com.example.imagevideocarousel.R.layout.fragment_main, container, false)
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val cameraFolder = "Camera"
//        var cameraDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString())
//        Log.d("myLog", "cameraDirectory is $cameraDirectory")
//
//        val files = cameraDirectory.listFiles()
//        for (curFile in files) {
//            if (curFile.isDirectory) {
//                cameraDirectory = curFile
//                break
//            }
//        }
//        val completeCameraFolder =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + cameraFolder
//        Log.d("myLog", "CompleteCameraFolder is $completeCameraFolder")


        val dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
        val directory = File(dcim)
        Log.d("myLog","dcim is $dcim")
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
}