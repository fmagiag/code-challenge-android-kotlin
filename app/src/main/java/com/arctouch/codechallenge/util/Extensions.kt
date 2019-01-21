package com.arctouch.codechallenge.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arctouch.codechallenge.util.Constants.Companion.API_KEY
import com.arctouch.codechallenge.util.Constants.Companion.BACKDROP_URL
import com.arctouch.codechallenge.util.Constants.Companion.BUNDLE_ARGS
import com.arctouch.codechallenge.util.Constants.Companion.POSTER_URL

fun buildPosterUrl(posterPath: String): String {
    return POSTER_URL + posterPath + "?api_key=" + API_KEY
}

fun buildBackdropUrl(backdropPath: String): String {
    return BACKDROP_URL + backdropPath + "?api_key=" + API_KEY
}

fun redirectToActivity(newActivity: Class<*>, activity: Context, args: Bundle?, clearTop: Boolean) {
    val intent = Intent(activity, newActivity)
    if (args != null) {
        intent.putExtra(BUNDLE_ARGS, args)
    }
    if (clearTop) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
    activity.startActivity(intent)
}