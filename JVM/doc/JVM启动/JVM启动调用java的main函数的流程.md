JVM虚拟机的执行入口是main.c中main的函数入口,其中window和Liunx平台函数头是不一样，JVM中是通过宏定义去区分走不通分支，下面是我去掉window，只保留了Liunx的部分，该函数的主逻辑是处理参数，最终调用JLI_Launch函数
```
JNIEXPORT int JNICALL
main(int argc, char **argv)
{
    int margc;
    char** margv;
    int jargc;
    char** jargv;
    const jboolean const_javaw = JNI_FALSE;
    {
        int i, main_jargc, extra_jargc;
        JLI_List list;
        //省略
      return JLI_Launch(margc, margv,
                   jargc, (const char**) jargv,
                   0, NULL,
                   VERSION_STRING,
                   DOT_VERSION,
                   (const_progname != NULL) ? const_progname : *margv,
                   (const_launcher != NULL) ? const_launcher : *margv,
                   jargc > 0,
                   const_cpwildcard, const_javaw, 0);
  }
}
```
##### java.c中JLI_Launch函数
这个是启动的函数入口,其主要处理的逻辑如下
1. 调用InitLauncher方法进行初始化.
2. DumpState打印启动状态.
3. SelectVersion选择合适jre版本.
4. CreateExecutionEnvironment创建JVM执行环境.
5. SetJvmEnvironment设置JVM环境。
6. LoadJavaVM加载JAVA VM的地址(位于java_md.c中，因为具体JAVAVM是跟依赖的平台相关).
7. SetClassPath设置JVM启动加载路径。
8. SetJavaCommandLineProp启动的-D的参数.
9. SetJavaLauncherPlatformProps设置启动的平台参数.
10. JVMInit初始化JVM.
```
/*
 * Entry point.
 */
JNIEXPORT int JNICALL
JLI_Launch(int argc, char ** argv,              /* main argc, argc */
        int jargc, const char** jargv,          /* java args */
        int appclassc, const char** appclassv,  /* app classpath */
        const char* fullversion,               /* full version defined */
        const char* dotversion,                 /* UNUSED dot version defined */
        const char* pname,                      /* program name */
        const char* lname,                      /* launcher name */
        jboolean javaargs,                      /* JAVA_ARGS */
        jboolean cpwildcard,                    /* classpath wildcard*/
        jboolean javaw,                         /* windows-only javaw */
        jint ergo                               /* unused */
)
{
    int mode = LM_UNKNOWN;
    char *what = NULL;
    char *main_class = NULL;
    int ret;
    InvocationFunctions ifn;
    jlong start, end;
    char jvmpath[MAXPATHLEN];
    char jrepath[MAXPATHLEN];
    char jvmcfg[MAXPATHLEN];

    _fVersion = fullversion;
    _launcher_name = lname;
    _program_name = pname;
    _is_java_args = javaargs;
    _wc_enabled = cpwildcard;

    InitLauncher(javaw);
    DumpState();
    
    SelectVersion(argc, argv, &main_class);

    CreateExecutionEnvironment(&argc, &argv,
                               jrepath, sizeof(jrepath),
                               jvmpath, sizeof(jvmpath),
                               jvmcfg,  sizeof(jvmcfg));

    if (!IsJavaArgs()) {
        SetJvmEnvironment(argc,argv);
    }

    ifn.CreateJavaVM = 0;
    ifn.GetDefaultJavaVMInitArgs = 0;

    if (!LoadJavaVM(jvmpath, &ifn)) {
        return(6);
    }

    ++argv;
    --argc;

    if (IsJavaArgs()) {
        /* Preprocess wrapper arguments */
        TranslateApplicationArgs(jargc, jargv, &argc, &argv);
        if (!AddApplicationOptions(appclassc, appclassv)) {
            return(1);
        }
    } else {
        /* Set default CLASSPATH */
        char* cpath = getenv("CLASSPATH");
        if (cpath != NULL) {
            SetClassPath(cpath);
        }
    }

    /* Parse command line options; if the return value of
     * ParseArguments is false, the program should exit.
     */
    if (!ParseArguments(&argc, &argv, &mode, &what, &ret, jrepath))
    {
        return(ret);
    }

    /* Override class path if -jar flag was specified */
    if (mode == LM_JAR) {
        SetClassPath(what);     /* Override class path */
    }

    /* set the -Dsun.java.command pseudo property */
    SetJavaCommandLineProp(what, argc, argv);

    /* Set the -Dsun.java.launcher pseudo property */
    SetJavaLauncherProp();

    /* set the -Dsun.java.launcher.* platform properties */
    SetJavaLauncherPlatformProps();

    return JVMInit(&ifn, threadStackSize, argc, argv, mode, what, ret);
}
```
##### java_md.c中LoadJavaVM函数
1. 加载JVM path中动态链接库 例如Liunx下是jre下的/lib/amd64/server/libjvm.so动态链接文件.
2. 赋值InvocationFunctions的是哪个函数指针，用于后续的虚拟机创建.
```
typedef struct {
    CreateJavaVM_t CreateJavaVM;
    GetDefaultJavaVMInitArgs_t GetDefaultJavaVMInitArgs;
    GetCreatedJavaVMs_t GetCreatedJavaVMs;
} InvocationFunctions;

jboolean
LoadJavaVM(const char *jvmpath, InvocationFunctions *ifn)
{
    HINSTANCE handle;

    JLI_TraceLauncher("JVM path is %s\n", jvmpath);

    /* Load the Java VM DLL */
    if ((handle = LoadLibrary(jvmpath)) == 0) {
        JLI_ReportErrorMessage(DLL_ERROR4, (char *)jvmpath);
        return JNI_FALSE;
    }

    /* Now get the function addresses */
    ifn->CreateJavaVM =
        (void *)GetProcAddress(handle, "JNI_CreateJavaVM");
    ifn->GetDefaultJavaVMInitArgs =
        (void *)GetProcAddress(handle, "JNI_GetDefaultJavaVMInitArgs");
    if (ifn->CreateJavaVM == 0 || ifn->GetDefaultJavaVMInitArgs == 0) {
        JLI_ReportErrorMessage(JNI_ERROR1, (char *)jvmpath);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}
```
#### java_md.c中JVMInit函数
JVMInit主要是调用ContinueInNewThread 创建mainThread执行JVM
```
int  JVMInit(InvocationFunctions* ifn, jlong threadStackSize,
        int argc, char **argv,
        int mode, char *what, int ret)
{
    ShowSplashScreen();
    return ContinueInNewThread(ifn, threadStackSize, argc, argv, mode, what, ret);
}
```
java.c中ContinueInNewThread\
主要逻辑初始化threadStackSize线程栈大小，然后继续调用ContinueInNewThread0进行创建线程去执行JVM的初始化，这里传入java.c中JavaMain这个函数指针,用户调用java中代码中main函数。
```
int ContinueInNewThread(InvocationFunctions* ifn, jlong threadStackSize,
                    int argc, char **argv,
                    int mode, char *what, int ret)
{
    if (threadStackSize == 0) {
      struct JDK1_1InitArgs args1_1;
      memset((void*)&args1_1, 0, sizeof(args1_1));
      args1_1.version = JNI_VERSION_1_1;
      ifn->GetDefaultJavaVMInitArgs(&args1_1);  /* ignore return value */
      if (args1_1.javaStackSize > 0) {
         threadStackSize = args1_1.javaStackSize;
      }
    }
    { /* Create a new thread to create JVM and invoke main method */
      JavaMainArgs args;
      int rslt;

      args.argc = argc;
      args.argv = argv;
      args.mode = mode;
      args.what = what;
      args.ifn = *ifn;

      rslt = ContinueInNewThread0(JavaMain, threadStackSize, (void*)&args);
    
      return (ret != 0) ? ret : rslt;
    }
}
```
java_md.c中ContinueInNewThread0
1. 设置线程栈的大小和属性值.
2. pthread_create是Linux平台创建新的线程方法。传入第三个参数中需要执行入口的continuation函数指针，它是指向java.c中JavaMain函数，这样程序又回到了java.c中执行JavaMaini函数。
3. pthread_attr_destroy销毁线程属性信息.
```
int
ContinueInNewThread0(int (JNICALL *continuation)(void *), jlong stack_size, void * args) {
    int rslt;
    pthread_t tid;
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);

    if (stack_size > 0) {
      pthread_attr_setstacksize(&attr, stack_size);
    }
    pthread_attr_setguardsize(&attr, 0); // no pthread guard page on java threads

    if (pthread_create(&tid, &attr, (void *(*)(void*))continuation, (void*)args) == 0) {
      void * tmp;
      pthread_join(tid, &tmp);
      rslt = (int)(intptr_t)tmp;
    } else {
      rslt = continuation(args);
    }
    pthread_attr_destroy(&attr);
    return rslt;
}
```
##### java.c中JavaMain函数
1. InitializeJVM初始化JVM，主要是调用动态链接库libjvm.so中CreateJavaVM函数。
2. showSettings显示设置。
3. ShowResolvedModules显示解析的模块.
4. ListModules列出模块
5. DescribeModule描述模块。
6. ValidateModules验证模块路径。
7. PrintJavaVersion打印java的版本.
8. 如果虚拟机启动报错, PrintUsage打印启动的classname.
9. LoadMainClass加载java类中main函数所在的类。
10. GetApplicationClass获取应用类。(该应用是指的JavaFX的桌面应用程序).
11. CreateApplicationArgs创建应用程序参数.
12. PostJVMInit JVM启动后置处理。
13. 调用JNIEnv对象的GetStaticMethodID方法获取mainclass中main函数的方法id.
14. 调用JNIEnv的CallStaticVoidMethod方法执行java的mainclass中main方法.
15. 调用ExceptionOccurred,如果出现异常则打印错误日志，并销毁虚拟机.
```
int JNICALL JavaMain(void * _args)
{
    JavaMainArgs *args = (JavaMainArgs *)_args;
    int argc = args->argc;
    char **argv = args->argv;
    int mode = args->mode;
    char *what = args->what;
    InvocationFunctions ifn = args->ifn;

    JavaVM *vm = 0;
    JNIEnv *env = 0;
    jclass mainClass = NULL;
    jclass appClass = NULL; // actual application class being launched
    jmethodID mainID;
    jobjectArray mainArgs;
    int ret = 0;
    jlong start, end;

    /* Initialize the virtual machine */
    start = CounterGet();
    if (!InitializeJVM(&vm, &env, &ifn)) {
        JLI_ReportErrorMessage(JVM_ERROR1);
        exit(1);
    }

    if (showSettings != NULL) {
        ShowSettings(env, showSettings);
        CHECK_EXCEPTION_LEAVE(1);
    }

    // show resolved modules and continue
    if (showResolvedModules) {
        ShowResolvedModules(env);
        CHECK_EXCEPTION_LEAVE(1);
    }

    // list observable modules, then exit
    if (listModules) {
        ListModules(env);
        CHECK_EXCEPTION_LEAVE(1);
        LEAVE();
    }

    // describe a module, then exit
    if (describeModule != NULL) {
        DescribeModule(env, describeModule);
        CHECK_EXCEPTION_LEAVE(1);
        LEAVE();
    }

    // validate modules on the module path, then exit
    if (validateModules) {
        jboolean okay = ValidateModules(env);
        CHECK_EXCEPTION_LEAVE(1);
        if (!okay) ret = 1;
        LEAVE();
    }

    if (printVersion || showVersion) {
        PrintJavaVersion(env, showVersion);
        CHECK_EXCEPTION_LEAVE(0);
        if (printVersion) {
            LEAVE();
        }
    }

    /* If the user specified neither a class name nor a JAR file */
    if (printXUsage || printUsage || what == 0 || mode == LM_UNKNOWN) {
        PrintUsage(env, printXUsage);
        CHECK_EXCEPTION_LEAVE(1);
        LEAVE();
    }
    ret = 1;
 
    mainClass = LoadMainClass(env, mode, what);
    CHECK_EXCEPTION_NULL_LEAVE(mainClass);
 
    appClass = GetApplicationClass(env);
    NULL_CHECK_RETURN_VALUE(appClass, -1);

    /* Build platform specific argument array */
    mainArgs = CreateApplicationArgs(env, argv, argc);
    CHECK_EXCEPTION_NULL_LEAVE(mainArgs);

    PostJVMInit(env, appClass, vm);
    CHECK_EXCEPTION_LEAVE(1);

    mainID = (*env)->GetStaticMethodID(env, mainClass, "main",
                                       "([Ljava/lang/String;)V");

    /* Invoke main method. */
    (*env)->CallStaticVoidMethod(env, mainClass, mainID, mainArgs);

    /*
     * The launcher's exit code (in the absence of calls to
     * System.exit) will be non-zero if main threw an exception.
     */
    ret = (*env)->ExceptionOccurred(env) == NULL ? 0 : 1;

    LEAVE();
}
```
##### java类main函数的加载
1. 调用GetLauncherHelperClass获取jdk中sun/launcher/LauncherHelper类
2. 调用GetStaticMethodID传入函数签名,获取checkAndLoadMain函数id
3. 调用CallStaticObjectMethod调用jdk中函数并传入参数,返回启动jar中mainclass类
```
static 
LoadMainClass(JNIEnv *env, int mode, char *name)
{
    jmethodID mid;
    jstring str;
    jobject result;
    jlong start, end;
    jclass cls = GetLauncherHelperClass(env);
    NULL_CHECK0(cls);
    NULL_CHECK0(mid = (*env)->GetStaticMethodID(env, cls,
                "checkAndLoadMain",
                "(ZILjava/lang/String;)Ljava/lang/Class;"));

    NULL_CHECK0(str = NewPlatformString(env, name));
    NULL_CHECK0(result = (*env)->CallStaticObjectMethod(env, cls, mid,
                                                        USE_STDERR, mode, str));
    return (jclass)result;
}
```
JVM中C++中是通通过调用JNIEnv的CallStaticVoidMethod函数去执行java类中main方法的。这个涉及后JNI相关，后续再详细分析

总结\
本文主要分了JVM启动入口，创建虚拟机,并解析参数,初始化的过程，并调用java类中main函数,其虚拟机的初始化是调用动态链接库实现的，然后通过JNI调用jdk中
LauncherHelper获取java类中main函数，最后通过调用jni的CallStaticObjectMethod将执行java执行权交给java类中的函数.至此java的main函数就开始执行了。

