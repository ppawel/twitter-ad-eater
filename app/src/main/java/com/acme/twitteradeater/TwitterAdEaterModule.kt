package com.acme.twitteradeater

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam


class TwitterAdEaterModule : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam?) {
        log("NOM NOM NOM %s", lpparam!!.packageName)

        if (lpparam.packageName == "com.acme.twitteradeater") {
            Utils.readPrefs()
            logcat("prefs = %s", Utils.prefs.all)
        }

        if (lpparam.packageName == "com.twitter.android") {
            Utils.readPrefs()
            logcat("prefs = %s", Utils.prefs.all)
            log("Twitter app loaded, initializing hooks...")
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
            // Process the view immediately when "addView" is called - new row is being added
            processView(v)

            v.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                // Existing row is being reused
                processView(v)
            }
        }
    }

    private fun processView(v: View) {
        // Need to use the event loop, otherwise we don't have the correct visibility of the promoted parts
        v.postDelayed(
            {
                if (isPromoted(v)) {
                    v.visibility = View.GONE
                    if (debugLogsEnabled()) {
                        logcat("Removing ad: view = %s", v)
                        logView(v.parent as View)
                    }
                } else {
                    v.visibility = View.VISIBLE
                }
            },
            1
        )
    }

    private fun isPromoted(v: View): Boolean {
        if (v.toString().contains("promoted") && v.visibility == View.VISIBLE) {
            return true
        }
        if (v is ViewGroup) {
            return v.children.any { isPromoted(it) }
        }
        return false
    }
}
