package com.acme.twitteradeater

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import de.robv.android.xposed.XposedBridge

private const val TAG: String = "TwitterAdEater"

fun log(message: String, vararg va: Any?) {
    XposedBridge.log(String.format(message, *va))
}

fun logcat(message: String, vararg va: Any?) {
    Log.i(TAG, String.format(message, *va))
}

fun logerr(message: String, vararg va: Any?) {
    Log.e(TAG, String.format(message, *va))
}

fun logView(v: View, ind: String = "--") {
    var desc = v.toString()
    val cls = v.javaClass

    try {
        when (cls.name) {
            "androidx.appcompat.widget.AppCompatTextView" -> {
                desc = cls.getMethod("getText").invoke(v).toString()
            }
            "com.twitter.ui.widget.TypefacesTextView" -> {
                desc = cls.getMethod("getText").invoke(v).toString()
            }
            "com.twitter.ui.widget.TextLayoutView" -> {
                desc = cls.getMethod("getText").invoke(v).toString()
            }
            "com.twitter.ui.widget.UnpaddedTextLayoutView" -> {
                desc = cls.getMethod("getText").invoke(v).toString()
            }
            //"com.twitter.ui.user.UserLabelView" -> {
            //    desc = cls.getMethod("getText").invoke(v).toString()
            //}
        }
    } catch (e: NoSuchMethodException) {
        logerr("Failed to describe view %s", v)
    }

    logcat("%sView [%s]: %s", ind, v.javaClass.name, desc)
    if (v is ViewGroup) {
        v.children.forEach { logView(it, "$ind--") }
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
