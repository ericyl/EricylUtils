# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ericyl/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
-keepclassmembers class * {
    public <methods>;
}

-keepattributes Signature

-keep class * extends android.os.IInterface {* ;}
-keep interface * extends android.os.IInterface {* ;}

-keep public class * extends android.app.Service
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class * extends java.lang.annotation.Annotation {* ;}

-keepclasseswithmembernames class * {
   private native <methods>;
   public native <methods>;
   protected native <methods>;
   public static native <methods>;
   private static native <methods>;
   static native <methods>;
   native <methods>;
   private static <fields>;
   public static <fields>;
   static <fields>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

#----------------------------------------------------------------------------
# ButterKnife
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(...); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# Otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# Fresco
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# BottomBar
-dontwarn com.roughike.bottombar.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# RxJava
# compile 'com.artemzin.rxjava:proguard-rules:1.2.4.0'
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# BarcodeScanner
-dontwarn me.dm7.barcodescanner.**
-keep class me.dm7.barcodescanner.** {*;}
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# QRGen
-dontwarn com.github.kenglxn.qrgen.**
-keep class com.github.kenglxn.qrgen.** {*;}
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# uCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# SQLCipher
# Unverified (https://github.com/illarionov/sqlcipher-android-tests/blob/spatialite/SqlcipherAndroidTestsProject/SqlcipherAndroidTests/proguard-sqlcipher.txt)

#-libraryjars libs/jsr305.jar

-dontobfuscate

-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.collect.MinMaxPriorityQueue

#SQLCipher optimized
-keep class net.sqlcipher.database.* extends java.lang.Exception {
   *;
}
-keepclasseswithmembers class net.sqlcipher.** {
    native <methods>;
}
-keep class net.sqlcipher.database.SQLite* {
    int nHandle;
    int nStatement;
}
-keep class net.sqlcipher.CursorWindow {
    int nWindow;
}
-keep class net.sqlcipher.database.SQLiteDatabase {
    int mNativeHandle;
}
#-keepnames class net.sqlcipher.** {
# *;
#}
#----------------------------------------------------------------------------