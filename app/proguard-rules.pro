 # 在优化阶段移除相关方法的调用
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

#https://github.com/thegrizzlylabs/sardine-android/issues/70
-dontwarn org.xmlpull.v1.**
-keep class org.xmlpull.v1.** { *; }
-dontwarn android.content.res.XmlResourceParser

-keepnames class com.zxhhyj.music.** { *; }
