package com.acme.twitteradeater

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.core.view.children
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class TwitterAdEaterModule : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam?) {
        log("NOM NOM NOM %s", lpparam!!.packageName)

        if (lpparam.packageName == "com.twitter.android") {
            log("Twitter app loaded... (package = %s)", lpparam.packageName)
            initHooks(lpparam)
        }
    }

    private fun initHooks(lpparam: LoadPackageParam) {
        val cls = XposedHelpers.findClass("android.view.ViewGroup", lpparam.classLoader)

        XposedBridge.hookAllMethods(cls, "addView", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                processAddView(param)
            }
        })
    }

    private fun processAddView(param: XC_MethodHook.MethodHookParam) {
        val v = param.args[0]

        if (v is ViewGroup && v.toString().contains("app:id/row")) {
            v.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->

                val handler = Handler(Looper.myLooper()!!)
                handler.postDelayed(
                    {
                        v.clearAnimation()
                        v.visibility = if (isPromoted(v)) View.GONE else View.VISIBLE
                    },
                    1
                )
            }
        }
    }

    private fun isPromoted(v: View): Boolean {
        if (v.toString().contains("tweet_promoted_badge_bottom") && v.visibility == View.VISIBLE) {
            return v !is ViewStub
        }
        if (v is ViewGroup) {
            return v.children.any { isPromoted(it) }
        }
        return false
    }
}
