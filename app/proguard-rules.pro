# Add project specific ProGuard rules here.
# By image_default, the flags in this file are appended to flags specified
# in C:\Users\crystysal\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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


#-----------------------------混淆配置设定--------------------------------------

# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

#混淆时不会产生形形色色的类名(混合时不使用大小写混合，混合后的类名为小写)
-dontusemixedcaseclassnames

#指定不忽略非公共类库
-dontskipnonpubliclibraryclasses

#不预校验，如果需要预校验，是-dontoptimize
-dontpreverify

#屏蔽警告
-ignorewarnings

#混淆时记录日志
-verbose

#优化
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


#----------------------------导入第三方包，但是在当前版本中使用会报 input jar file is specified twice 错误，所以注释掉
#-libraryjars libs/android.support.v4.jar
#-libraryjars libs/BaiduLBS_Android.jar
#-libraryjars libs/commons-httpclient-3.1.jar
#-libraryjars libs/jackson-annotations-2.1.4.jar
#-libraryjars libs/jackson-core-2.1.4.jar
#-libraryjars libs/jackson-databind-.2.1.4.jar
#-libraryjars libs/xUtils-2.6.14.jar


#---------------------------不需要混淆第三方类库-----------------------------------------
#去掉警告
-dontwarn android.support.v4.**

#保留support下的所有类及其内部类
-keep class android.suppoer.v4.** {*;}


#---------------------------不需要混淆系统组件--------------------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

-keep class com.classtc.text.entity.**{*;} #过滤掉自己编写的实体类


#---------------------------保护指定的类和类的成员，但条件是所有指定的类和类成员是要存在的---------------
-keepclasseswithmembernames class*{
    public <init> (android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembernames class*{
    public <init> (android.content.Context,android.util.AttributeSet,int);
}

























