package com.arctouch.codechallenge.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arctouch.codechallenge.util.Constants.Companion.BUNDLE_ARGS


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