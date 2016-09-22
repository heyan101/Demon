package demon;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import demon.utils.XProperties;

/**
 * 插件管理器
 * 
 */
public class ModuleMgr {

    /**
     * 插件的配置文件名称
     */
    public static final String MODULE_PROPERTY_FILE_NAME = "module.properties";
    /**
     * 插件存放第三方依赖库的文件夹名称
     */
    public static final String MODULE_LIB_DIR_NAME = "lib";
    /**
     * 插件的存放.class文件所在的目录名称
     */
    public static final String MODULE_BIN_DIR_NAME = "bin";
    
    /**
     * demon基础插件所在目录
     */
    public static File baseModulePath;
    /**
     * 第三方开发插件所在目录<br>
     * 第三方插件也可以放到基础插件目录
     */
    private static File thirdModulePath;
    
    /**
     * 报表插件所在目录
     */
//    private static File reportsPath;
    
    /**
     * 插件加载操作的状态标识
     */
    private static boolean loaded = false;
    
    /**
     * 已经加载成功的插件
     */
    private static Set<String> loadedModules = new HashSet<String>();
    
    /**
     * 加载失败的插件
     */
    private static Set<String> failedModules = new HashSet<String>();
    
    /**
     * 忽略的目录，即不符合插件规范的，却又存在在加载目录的文件夹
     */
    private static Set<String> ignoredDirs = new HashSet<String>();
    
    /**
     * 在基础插件目录的插件
     */
    public static Set<String> baseModules = new HashSet<String>();
    
    /**
     * 在第三方插件目录的插件
     */
    public static Set<String> thirdModules = new HashSet<String>();
    
    /**
     * 在报表插件目录的插件
     */
//    public static Set<String> reportsModules = new HashSet<String>();
    
    /**
     * 加载插件
     * @throws Exception
     */
    public static void loadModules() throws Exception {
        if (loaded) {
            throw new Exception("Already loaded.");
        }

        loadModule(baseModulePath, baseModules);
        loadModule(thirdModulePath, thirdModules);
//        loadModule(reportsPath, reportsModules);
        
        System.out.println();
        printResult("Ignored Dirs", ignoredDirs);
        printResult("Failed Modules", failedModules);
        printResult("Loaded Modules", loadedModules);
        loaded = true;
    }
    
    /**
     * 加载指定插件目录下的插件
     * 
     * @param moduleRootPath 指定插件目录
     * @param modules 加载成功的插件集合
     * @return
     * @throws Exception
     */
    private static boolean loadModule(File moduleRootPath, Set<String> modules) throws Exception {
        
        if (null == moduleRootPath) {
            return false;
        }
        
        for (File modulePath : moduleRootPath.listFiles()) {
            if (modulePath.getName().startsWith("_") ||
                modulePath.getName().startsWith(".") ||
                !modulePath.isDirectory()) {
                ignoredDirs.add(modulePath.getName());
                continue;
            }

            String moduleName = modulePath.getName();
            try {
                loadModule(modulePath, moduleName, modules);
                
            } catch (Exception e) {
                failedModules.add(moduleName);
                System.err.println("Load " + moduleName + " failed, caused by exception:");
                e.printStackTrace();
            }
        }

        return true;
    }
    
    /**
     * 加载插件
     * 
     * @param modulePath 插件路径
     * @param moduleName 插件名称
     * @param modules 成功加载的插件集合
     * @return
     * @throws Exception
     */
    private static boolean loadModule(File modulePath, String moduleName, Set<String> modules) throws Exception {
        if (loadedModules.contains(moduleName)) {
            return true;
        } else if (failedModules.contains(moduleName)) {
            return false;
        }
        
        String[] dependedModuleNames = getDependedModules(modulePath, moduleName);
        if (dependedModuleNames == null) {
            failedModules.add(moduleName);
            System.err.println(String.format("Load %s failed, module.properties file not found", moduleName));
            return false;
        }

        for (String name : dependedModuleNames) {
        	if (!loadedModules.contains(name) && !loadModule(new File(baseModulePath, name), name, modules)) {
        	    System.err.println(String.format("Load %s failed, caused by the failure of depended module %s", 
        	            moduleName, name));
        	    failedModules.add(moduleName);
        	    return false;
        	}
        }
        
        modules.add(moduleName);

        List<URL> urls = new LinkedList<URL>();
        File moduleBinPath = new File(modulePath, MODULE_BIN_DIR_NAME);
        if (moduleBinPath.isDirectory()) {
            urls.add(moduleBinPath.toURI().toURL());
        }

        File moduleLibPath = new File(modulePath, MODULE_LIB_DIR_NAME);
        loadJar(urls, moduleLibPath);

        URLClassLoader classLoader = (URLClassLoader) ClassLoader
                .getSystemClassLoader();
        Method add = URLClassLoader.class.getDeclaredMethod("addURL",
                new Class[] {URL.class});
        add.setAccessible(true);
        for (URL url : urls.toArray(new URL[0])) {
            add.invoke(classLoader, url);
        }

        try {
            Class<?> cls = classLoader.loadClass(String.format("demon.%s.Init",
                    moduleName));
            Method method = cls.getMethod("init", String.class);
            method.invoke(null, modulePath.getAbsolutePath());
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            System.err.println(String.format("Invalid module '%s', demon.%s.Init.init not found.", moduleName, moduleName));
            failedModules.add(moduleName);
            return false;
        }
        
        loadedModules.add(moduleName);
        
        return true;
    }
    
    /**
     * 加载插件依赖的第三方jar包
     * 
     * @param urls
     * @param moduleLibPath
     * @throws MalformedURLException
     */
    private static void loadJar(List<URL> urls, File moduleLibPath) throws MalformedURLException {
        if (moduleLibPath.isDirectory()) {
            for (File jarPath : moduleLibPath.listFiles()) {
                if (jarPath.getAbsolutePath().toLowerCase().endsWith(".jar")) {
                    urls.add(jarPath.toURI().toURL());
                } else if (jarPath.isDirectory()) {
                    loadJar(urls, jarPath);
                }
            }
        }
    }
    
    /**
     * 解析插件依赖的其他插件
     * 
     * @param modulePath 插件目录
     * @param moduleName 插件名称
     * @return
     * @throws Exception
     */
    private static String[] getDependedModules(File modulePath, String moduleName) throws Exception {
    	File propFilePath = new File(modulePath.getAbsolutePath(), MODULE_PROPERTY_FILE_NAME);
    	if (!propFilePath.isFile()) {
            ignoredDirs.add(moduleName);
    	    return null;
    	}
    	
    	XProperties properties = new XProperties(moduleName, modulePath.getAbsolutePath());
    	String dependedModuleNames = properties.getProperty("dependence");
    	
    	List<String> names = new ArrayList<String>();
    	if (dependedModuleNames != null) {
    		for (String name : dependedModuleNames.split(",")) {
        		if (name.trim().length() > 0) {
        			names.add(name);
        		}
        	}
    	}
    	return names.toArray(new String[0]);
    }
    
    /**
     * 打印加载结果
     * 
     * @param title
     * @param items
     */
    private static void printResult(String title, Set<String> items) {
        System.out.print(title + ": ");
        for (String item : items) {
            System.out.print(item + " ");
        }
        System.out.println();
    }
    
    /**
     * 初始化插件加载器
     * 
     * @param baseModuleDir 基础插件目录
     * @param thirdModuleDir 第三方插件目录
     */
    public static void init(String baseModuleDir, String thirdModuleDir) {
        ModuleMgr.baseModulePath = new File(baseModuleDir);
        if (null != thirdModuleDir) {
            ModuleMgr.thirdModulePath = new File(thirdModuleDir);
        }
    }

    /**
     * 获取插件的所在目录
     * @param module
     * @return
     */
    public static String getModulePath(String module) {
        
        if (baseModulePath != null && baseModules.contains(module)) {
            return baseModulePath.getAbsolutePath();
        } else if (thirdModulePath != null && thirdModules.contains(module)) {
            return thirdModulePath.getAbsolutePath();
        }
        return null;
    }
}
