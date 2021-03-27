package com.acme.twitteradeater

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import de.robv.android.xposed.XposedBridge

private val TAG: String = "TwitterAdEater"

fun log(message: String, vararg va: Any?) {
    XposedBridge.log(String.format(message, *va))
}

fun logcat(message: String, vararg va: Any?) {
    Log.i(TAG, String.format(message, *va))
}

fun logView(v: View, ind: String = "") {
    logcat("%sView %s", ind, v.toString())
    if (v is ViewGroup) {
        //logcat(v.children.joinToString("\n") { it.toString() })
        v.children.forEach { logView(it, "$ind ") }
    }
}

fun getParentRow(v: View): View {
    var result: View = v
    while (!result.toString().contains("GroupedRowView")) {
        if (result.parent == null) {
            break;
        }
        result = result.parent as View
    }
    return result
}
