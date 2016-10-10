# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\SDK\android-sdk-windows/tools/proguard/proguard-android.txt
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

 -optimizationpasses 5          # 指定代码的压缩级别
 -dontusemixedcaseclassnames   # 是否使用大小写混合
 -dontskipnonpubliclibraryclasses #不去忽略非公共的库类
 -dontpreverify           # 混淆时是否做预校验
 -verbose                # 混淆时是否记录日志
 -ignorewarning          #忽略警告

 -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

 -keep public class * extends android.app.Activity      # 保持哪些类不被混淆
 -keep public class * extends android.app.Application   # 保持哪些类不被混淆
 -keep public class * extends android.app.Service       # 保持哪些类不被混淆
 -keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
 -keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
 -keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
 -keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
 -keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
 -keep public class * extends android.support.v4.app.Fragment
 -keep public class * extends android.app.Fragment
 -keep public class * extends android.support.v7.app.AppCompatActivity


 -keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
     native <methods>;
 }
 -keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
     public <init>(android.content.Context, android.util.AttributeSet);
 }
 -keepclasseswithmembers class * {# 保持自定义控件类不被混淆
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }
 -keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
     public void *(android.view.View);
 }
 -keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }
 -keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
     public static final android.os.Parcelable$Creator *;
 }


#----------------    友盟相关的排除混淆    -------------------------
 -dontshrink
 -dontoptimize
 -dontwarn com.google.android.maps.**
 -dontwarn android.webkit.WebView
 -dontwarn com.umeng.**
 -keep public class javax.**
 -keep public class android.webkit.**
 -keepattributes Exceptions,InnerClasses,Signature
 -keepattributes *Annotation*
 -keepattributes SourceFile,LineNumberTable


 -keep public interface com.tencent.**
 -keep public interface com.umeng.socialize.**
 -keep public interface com.umeng.socialize.sensor.**
 -keep public interface com.umeng.scrshot.**

 -keep public class com.umeng.socialize.* {*;}

 -keep class com.umeng.scrshot.**
 -keep public class com.tencent.** {*;}
 -keep class com.umeng.socialize.sensor.**
 -keep class com.umeng.socialize.handler.**
 -keep class com.umeng.socialize.handler.*
 -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
 -keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

 -keep class im.yixin.sdk.api.YXMessage {*;}
 -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

 -dontwarn twitter4j.**
 -keep class twitter4j.** { *; }

 -keep class com.tencent.** {*;}
 -dontwarn com.tencent.**
 -keep public class com.umeng.soexample.R$*{
     public static final int *;
 }
 -keep public class com.umeng.soexample.R$*{
     public static final int *;
 }
 -keep class com.tencent.open.TDialog$*
 -keep class com.tencent.open.TDialog$* {*;}
 -keep class com.tencent.open.PKDialog
 -keep class com.tencent.open.PKDialog {*;}
 -keep class com.tencent.open.PKDialog$*
 -keep class com.tencent.open.PKDialog$* {*;}

 -keep class com.sina.** {*;}
 -dontwarn com.sina.**

 -keepnames class * implements android.os.Parcelable {
     public static final ** CREATOR;
 }



#----------fresco混淆排除----------------------
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
-dontwarn okio.**
-dontwarn javax.annotation.**
-keep class com.facebook.imagepipeline.gif.** { *; }
-keep class com.facebook.imagepipeline.webp.** { *; }


#--------------ButterKnife混淆排除-------------------------

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
  }


#--------------Retrofit混淆排除-----------------------
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Exceptions


#----------------不混淆资源类--------------------
-keepclassmembers class **.R$* {
    public static <fields>;
}

#----------------如果引用了v4或者v7包--------------
-dontwarn android.support.**
#
# design support library
#
-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }


# --------------Gson混淆--------------------
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
-keep class cn.zn.com.zn_android.model.** { *; }


#---------------联网的类不被混淆---------------
 -keep class android.net.http.**{*;}
 -keep class org.apache.http.auth.**{*;}
 -keep class org.apache.http.client.**{*;}
 -keep class org.apache.http.**{*;}
 -keep class java.net.**{*;}
 -keep public class com.tencent.smtt.webkit.**
#---------------奥点云----------------------
-keep class com.aodianyun.dms.android.DMS
-keep class org.eclipse.paho.android.service.**{*;}
-keep class org.eclipse.paho.client.mqttv3.**{*;}


 #------------------  下方是共性的排除项目         ----------------
 # 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
 # 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除

 -keepclasseswithmembers class * {
     ... *JNI*(...);
 }
 -keepclasseswithmembernames class * {
 	... *JRI*(...);
 }
 -keep class **JNI* {*;}


#---------------EventBus-------------------
-keepclassmembers class ** {
    public void onEvent*(**);
}


#----------------ijk播放器-----------------
-keep class tv.danmaku.ijk.media.player.** {*;}
-keep class com.pili.pldroid.player.** {*;}
-dontwarn com.pili.**

-keep class com.yintong.** {*;}


#----------------------保护注解----------------
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

#混淆后生成mapping文件
-printmapping build/outputs/mapping/release/mapping.txt


#支付宝相关的混淆

#-libraryjars libs/alipaySdk-20160825.jar
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

# ----------Addidional for x5.sdk classes for apps-------


-keep class com.tencent.smtt.export.external.**{
    *;
}

-keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
	*;
}

-keep class com.tencent.smtt.sdk.CacheManager {
	public *;
}

-keep class com.tencent.smtt.sdk.CookieManager {
	public *;
}

-keep class com.tencent.smtt.sdk.WebHistoryItem {
	public *;
}

-keep class com.tencent.smtt.sdk.WebViewDatabase {
	public *;
}

-keep class com.tencent.smtt.sdk.WebBackForwardList {
	public *;
}

-keep public class com.tencent.smtt.sdk.WebView {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
	public static final <fields>;
	public java.lang.String getExtra();
	public int getType();
}

-keep public class com.tencent.smtt.sdk.WebView$PictureListener {
	public <fields>;
	public <methods>;
}


-keepattributes InnerClasses

-keep public enum com.tencent.smtt.sdk.WebSettings$** {
    *;
}

-keep public class com.tencent.smtt.sdk.WebSettings {
    public *;
}


-keep public class com.tencent.smtt.sdk.ValueCallback {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebViewClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient {
	public <fields>;
	public <methods>;
}


-keep class com.tencent.smtt.sdk.SystemWebChromeClient{
	public *;
}
# 1. extension interfaces should be apparent
-keep public class com.tencent.smtt.export.external.extension.interfaces.* {
	public protected *;
}

# 2. interfaces should be apparent
-keep public class com.tencent.smtt.export.external.interfaces.* {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.CookieSyncManager {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.Tbs* {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.LogFileUtils {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLog {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLogClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.CookieSyncManager {
	public <fields>;
	public <methods>;
}


-keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {
	public <fields>;
	public <methods>;
}

-keep class MTT.ThirdAppInfoNew {
	*;
}

# 微信支付
-keep class com.tencent.mm.sdk.** {
   *;
}