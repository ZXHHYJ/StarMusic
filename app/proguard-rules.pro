# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames

 # 在优化阶段移除相关方法的调用
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}