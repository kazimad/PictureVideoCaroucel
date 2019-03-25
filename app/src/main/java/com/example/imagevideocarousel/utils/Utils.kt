package com.example.imagevideocarousel.utils

object Utils {
    fun splitter(input: String, splitParam: String): Array<String> {
        val string = input
        val parts = string.split(splitParam.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return parts
    }
}