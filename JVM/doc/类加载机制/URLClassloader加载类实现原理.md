首先来看下URLClassloader的example

![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/01cc4ad37022478f858652c7b9ad8b07~tplv-k3u1fbpfcp-watermark.image?)
从例子中可以看到只要创建URLClassloader并传入URL的集合，然后直接调用loadCLass传入类资源全限定名称, 就可以实现对类的URL中资源的class字节码加载到JVM成为Java。lang.Class对象。
### URLClassloader的类继承关系
URLClassloader继承自SecureClassloader以及Classloader。

![image.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8fa45245f1364389b4a553ed015b5edb~tplv-k3u1fbpfcp-watermark.image?)

URLClassloader的重要字段就是ucp，存放的是类和资源的路径
```
/* The search path for classes and resources */
private final URLClassPath ucp;
```
URLClassLoader的构造函数传资源的加载路径,
1. 初始化父类的构造函数。
2. 创建URLClassPath对象，并传入资源的urls以及访问上下文AccessContext。
```
public URLClassLoader(URL[] urls) {
    super();
    this.acc = AccessController.getContext();
    this.ucp = new URLClassPath(urls, acc);
}
```
### URLClassloader重写父类的findClass
由前面的文章可以知道URLClassloader继承Classloader只是重写了父类的findClass方法的实现径，这里实现AccessController#doPrivileged传入匿名内部类,是使用模板类在执行run函数的前后检查是否由访问权限,这里就不多说，下面直接看run方法的逻辑
1. 将传入类的路径名称字符串中所有的'.'替换成'/',结尾加'.class'。
2. 通过ucp(构造函数中创建的URLClassPath对象的变量)的getResource方法去查询
3. 如果查询Resouce不为空，则调用defineClass进行类资源的加载并转换成Class对象.
```
protected Class<?> findClass(final String name)throws ClassNotFoundException{
    final Class<?> result;
    try {
        result = AccessController.doPrivileged(
            new PrivilegedExceptionAction<Class<?>>() {
              String path = name.replace('.', '/').concat(".class");
                    Resource res = ucp.getResource(path, false);
                    if (res != null) {
                        try {
                            return defineClass(name, res);
                        } catch (IOException e) {
                            throw new ClassNotFoundException(name, e);
                        }
                    } else {
                        return null;
                    }
                }
            }, acc);
    } catch (java.security.PrivilegedActionException pae) {
        throw (ClassNotFoundException) pae.getException();
    }
    if (result == null) {
        throw new ClassNotFoundException(name);
    }
    return result;
}
```
#### URLClassPath的getResource实现
首先先来看下URLClassPath重要属性
```
/* The original search path of URLs. */
private final ArrayList<URL> path;

/* The deque of unopened URLs */
private final ArrayDeque<URL> unopenedUrls;

/* The resulting search path of Loaders */
private final ArrayList<Loader> loaders = new ArrayList<>();

/* Map of each URL opened to its corresponding Loader */
private final HashMap<String, Loader> lmap = new HashMap<>();
```
如果传入URLStreamHandlerFactory参数，则默认为null, 然后主要构造的是path和unopenedUrls这两个URL的集合, jarHandler为空.loaders默认是空的集合。
```
public URLClassPath(URL[] urls, AccessControlContext acc) {
    this(urls, null, acc);
}
public URLClassPath(URL[] urls,
                    URLStreamHandlerFactory factory,
                    @SuppressWarnings("removal") AccessControlContext acc) {
    ArrayList<URL> path = new ArrayList<>(urls.length);
    ArrayDeque<URL> unopenedUrls = new ArrayDeque<>(urls.length);
    for (URL url : urls) {
        path.add(url);
        unopenedUrls.add(url);
    }
    this.path = path;
    this.unopenedUrls = unopenedUrls;
    if (factory != null) {
        jarHandler = factory.createURLStreamHandler("jar");
    } else {
        jarHandler = null;
    }
    if (DISABLE_ACC_CHECKING)
        this.acc = null;
    else
        this.acc = acc;
}
```
URLClassPath#getResource逻辑
1. 遍历所有的Loader，然后调用getResource方法查询传入类全名的资源，如果找到则返回，没有则返回null。
```
public Resource getResource(String name, boolean check) {
    Loader loader;
    for (int i = 0; (loader = getLoader(i)) != null; i++) {
        Resource res = loader.getResource(name, check);
        if (res != null) {
            return res;
        }
    }
    return null;
}
```
URLClassPath#getLoader(int index)主要逻辑
1. while循环, 其条件是loader集合大小小于index + 1，由于index是从0开始的,所以开始判断的时候条件是 0 < 1,条件判断为true,
2. 循环体的逻辑如下:\
   2.1 对unopenedUrls加加锁，从unopenedUrls的数组的头部弹出出一个URL对象，如果没有了，则直接返回null.\
   2.2 调用URLUtil#urlNoFragString函数对url转成字符串,以方便存入HashMap的key。如果lmap(即Map<String,Loader>类型)对象是否包含url转换后的字符串,说明已加载，则执行continue，结束当前循环。\
   2.3 调用getLoader重载函数传入URL资源路径,将URL转换成Loader\
   2.4 获取loader的classpath集合，如果不为空,则调用push函数将URL加入unopenedUrls的双端数组的头部.\
   2.5 将新建的loader加入到loaders集合,同时加入lmap的key是ULR的字符换value
   是Loader的Map集合。
3. 循环体结束后,则取loader集合中index位置的Loader。
```
private synchronized Loader getLoader(int index) {
    if (closed) {
        return null;
    }
    while (loaders.size() < index + 1) {
        final URL url;
        synchronized (unopenedUrls) {
            url = unopenedUrls.pollFirst();
            if (url == null)
                return null;
        }
        String urlNoFragString = URLUtil.urlNoFragString(url);
        if (lmap.containsKey(urlNoFragString)) {
           continue;
        }
        Loader loader;
        try {
            loader = getLoader(url);
            URL[] urls = loader.getClassPath();
            if (urls != null) {
                push(urls);
            }
        } catch (IOException e) {
            // Silently ignore for now...
            continue;
        } catch (SecurityException se) {
          if (DEBUG) {
             System.err.println("Failed to access " + url + ", " + se );
          }
         continue;
        }
        // Finally, add the Loader to the search path.
        loaders.add(loader);
        lmap.put(urlNoFragString, loader);
    }
    return loaders.get(index);
}
```
URLClassPath#getLoader(URL url)主要逻辑如下\
主要是将URL资源路径转换成Loader对象.直接getLoader中
PrivilegedExceptionAction的run函数的逻辑。
1. 通过获取ULR的protocol协议以及资源文件名称.
2. 判断文件名称不为空并且资源文件名称是'/'结尾的，如果为true，则继续判断URL的协议是'file'或则'jar'还是其他.\
   2.1 协议为'file'，则创建FileLoader.\
   2.2 协议为'jar'并且默认url默认URLStreamHandler.则创建JarLoader.\
   2.3 协议为其他，则创建Loader。
3. 如果2中判断false，则创建JarLoader.
```
private Loader getLoader(final URL url) throws IOException {
    try {
        return AccessController.doPrivileged(
                new PrivilegedExceptionAction<>() {
                    public Loader run() throws IOException {
                        String protocol = url.getProtocol();
                        String file = url.getFile();
                        if (file != null && file.endsWith("/")) {
                            if ("file".equals(protocol)) {
                                return new FileLoader(url);
                            } else if ("jar".equals(protocol) &&
                                    isDefaultJarHandler(url) &&
                                    file.endsWith("!/")) {
                                // extract the nested URL
                                URL nestedUrl = new URL(file.substring(0, file.length() - 2));
                                return new JarLoader(nestedUrl, jarHandler, lmap, acc);
                            } else {
                                return new Loader(url);
                            }
                        } else {
                            return new JarLoader(url, jarHandler, lmap, acc);
                        }
                    }
                }, acc);
    } catch (PrivilegedActionException pae) {
        throw (IOException)pae.getException();
    }
}
```
那么接下我们主要以JarLoader为例分下、
#### JarLoader的getResource查询资源的实现
首先看下JarLoader的主要的字段，主要就是JarFile,
```
private static class JarLoader extends Loader {
    private JarFile jar;
    private final URL csu;
    private JarIndex index;
    private URLStreamHandler handler;
    private final HashMap<String, Loader> lmap;
    //省略
 }
```
1. 首先调用ensureOpen确保jar文件已经解析完成。
2. 通过调用JarFile的getJarEntry传入类全名，获取到JarEntry对象。
3. 判断JarEntry不为空，则调用checkResource返回对应的资源。
   4。判断JarIndex为空，则直接返回null。
5. 如果jarentry为空，但是JarIndex不为空，则调用getResource查询类资源。
```
Resource getResource(final String name, boolean check) {
    try {
        ensureOpen();
    } catch (IOException e) {
        throw new InternalError(e);
    }
    final JarEntry entry = jar.getJarEntry(name);
    if (entry != null)
        return checkResource(name, check, entry);
    if (index == null)
        return null;
    HashSet<String> visited = new HashSet<>();
    return getResource(name, check, visited);
}
```
JarFile类是继承ZipFile，它本质上classes和资源压缩文件
JarFile的Specification参考
https://docs.oracle.com/javase/7/docs/technotes/guides/jar/jar.html#Signed_JAR_File


