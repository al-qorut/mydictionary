# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
##---------------Begin: proguard configuration for SQLCipher  ----------
-keep,includedescriptorclasses class net.sqlcipher.** { *; }
-keep,includedescriptorclasses interface net.sqlcipher.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
<init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
**[] $VALUES;
public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
*** rewind();
}

-keep class dagger.** { *; }
-keep interface dagger.** { *; }

-keep class **$$ModuleAdapter { *; }
-keepnames class **$$InjectAdapter { *; }

-keepclassmembers class * {
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <init>(...);
}

#rxJava
# Keep the necessary RxJava classes
-keep class io.reactivex.** { *; }
-keep interface io.reactivex.** { *; }
-keep class io.reactivex.rxjava3.** { *; }
-keep interface io.reactivex.rxjava3.** { *; }
-keep class io.reactivex.internal.** { *; }
-keep class io.reactivex.plugins.** { *; }

# Keep all annotation types
-keep @interface io.reactivex.rxjava3.annotations.** { *; }

# Keep lambda classes used in RxJava
-keep class io.reactivex.rxjava3.internal.util.** { *; }
-keep class io.reactivex.rxjava3.internal.operators.** { *; }

# Keep generic classes and methods that are used in RxJava3
-keepclassmembers class * {
    ** (io.reactivex.rxjava3.core.Observable);
    ** (io.reactivex.rxjava3.core.Single);
    ** (io.reactivex.rxjava3.core.Completable);
    ** (io.reactivex.rxjava3.core.Maybe);
    ** (io.reactivex.rxjava3.core.Flowable);
}
