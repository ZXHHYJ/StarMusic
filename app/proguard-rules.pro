# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames

#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable

# 忽略警告
-ignorewarnings

-keep class com.zxhhyj.music.logic.bean**{*;}

-optimizationpasses 7

 # 在优化阶段移除相关方法的调用
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# mapping.txt文件列出混淆前后的映射
-printmapping mapping.txt