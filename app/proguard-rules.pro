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

-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep class com.automa.datastore.user_data** { *; }
-keep class com.automa.domain.auth.model** { *; }
-keep class com.automa.domain.check_sheet.model** { *; }
-keep class com.automa.domain.driver_task.model** { *; }
-keep class com.automa.domain.account.model** { *; }
-keep class com.automa.domain.breakdown_report.model** { *; }
-keep class com.automa.domain.mechanic_task.model** { *; }

-keep class com.automa.data.mechanic_task** { *; }
-keep class com.automa.data.driver_task** { *; }
-keep class com.automa.data.check_sheet** { *; }
-keep class com.automa.data.auth** { *; }
-keep class com.automa.data.account** { *; }
-keep class com.automa.data.breakdown_report** { *; }

-printmapping