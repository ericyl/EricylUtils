# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/liangyu/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontshrink

-dontwarn android.content.pm.**
-keep class android.content.pm.** { *; }

-keepclassmembernames class * extends android.app.Activity {
    public void *(android.view.View);
    public android.view.View *;
}

-keepclasseswithmembers class * {
    private native <methods>;
    public native <methods>;
    protected native <methods>;
    public static native <methods>;
    private static native <methods>;
    static native <methods>;
    native <methods>;
}

-keep class * extends android.view.** {
    public static <fields>;
    protected static <fields>;
    public <fields>;
    public static <methods>;
    protected static <methods>;
    public <methods>;
}

-keepclasseswithmembers class * extends android.view.** {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * extends android.support.v7.widget.** {
    public static <fields>;
    protected static <fields>;
    public <fields>;
    public static <methods>;
    protected static <methods>;
    public <methods>;
}

-keep class * extends android.support.v4.widget.** {
    public static <fields>;
    protected static <fields>;
    public <fields>;
    public static <methods>;
    protected static <methods>;
    public <methods>;
}

-keepclassmembernames class * extends android.preference.Preference {
    public static <fields>;
    protected static <fields>;
    public <fields>;
    public static <methods>;
    protected static <methods>;
    public <methods>;
}

-keepclasseswithmembers class * extends android.preference.Preference {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * extends com.android.volley.** {
    public static <fields>;
    protected static <fields>;
    public <fields>;
    public static <methods>;
    protected static <methods>;
    public <methods>;
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

-keeppackagenames com.ericyl.utils.**
-keep public interface com.ericyl.utils.** {
    public static <fields>;
    protected static <fields>;
    public <fields>;
    protected <fields>;
    public static <methods>;
    protected static <methods>;
    public <methods>;
    protected <methods>;
}
-keep class com.ericyl.utils.model.** {
    public static <fields>;
    protected static <fields>;
    public <fields>;
    public static <methods>;
    protected static <methods>;
    public <methods>;
}
-keep class com.ericyl.utils.util.** {
    public static <fields>;
    protected static <fields>;
    public <fields>;
    public static <methods>;
    protected static <methods>;
    public <methods>;
}

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, Annotation, EnclosingMethod, MethodParameters
