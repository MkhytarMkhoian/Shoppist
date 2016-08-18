# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android_and_Java\android_studio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes *Annotation*
-keepattributes Signature
-dontwarn com.squareup.**
-keep class com.squareup.** { *; }

-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-dontwarn com.parse.ParseOkHttpClient**
-keep class com.parse.ParseOkHttpClient** { *; }
-dontwarn com.parse.**

-keep class com.android.vending.billing.**

-dontwarn javax.annotation.**

#-assumenosideeffects class org.solovyev.android.checkout.Billing {
#    public static void debug(...);
#    public static void warning(...);
#    public static void error(...);
#}
#
#-assumenosideeffects class org.solovyev.android.checkout.Check {
#    static *;
#}

