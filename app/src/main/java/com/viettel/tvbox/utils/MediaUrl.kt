package com.viettel.tvbox.utils

import com.viettel.tvbox.BuildConfig

fun getImageUrl(imageName: String): String {
    val imageUrl = BuildConfig.IMAGE_URL
    return if (imageName.startsWith("https")) {
        imageName
    } else {
        "$imageUrl$imageName"
    }
}


fun getVideoUrl(videoName: String): String {
    val videoUrl = BuildConfig.VIDEO_URL
    return if (videoName.startsWith("https")) {
        videoName
    } else {
        "$videoUrl$videoName"
    }
}