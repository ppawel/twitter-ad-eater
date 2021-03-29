# Twitter Ad Eater

Twitter Ad Eater is a module for the Xposed/[LSPosed](https://github.com/LSPosed/LSPosed) framework that attempts to remove ads from the Twitter Android application.

# Installation

1. Download the module from the [Releases](https://github.com/ppawel/twitter-ad-eater/releases) page.
2. Install the APK.
3. Open the LSPosed app.
4. Enable the module and whitelist the Twitter app on the module page.
5. Reboot the device.

# Background

I created this module after switching to Android 11 where [the original module](https://github.com/gusarov/TwitterAdKiller) [does not seem to work anymore](https://github.com/gusarov/TwitterAdKiller/issues/2).

The module is tested so far only on Android 11 and with the LSPosed framework. Feel free to [report any issues](https://github.com/ppawel/twitter-ad-eater/issues) and I will take a look.

Implementation of this module is inspired by the Twitter AdKiller module but I decided to create my own module because... why not? :P

## TODO

* Remove promoted trending topics/hashtags
* Remove the "moments" or "stories" section (or whatever it is called...) from the timeline to save space
* Try to find a more efficient way of filtering out promoted content, instead of hooking into the general `ViewGroup.addView` method
