package bootstrap;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import machine.BaseRuleSet;
import machine.QuiverInitializer;
import machine.callbacks.MachineCallback;
import machine.internal.CompositionMachine;
import util.FileUtil;

public class Bootstrap {
    private static final FilenameFilter JAR_FILENAME_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            if (name.endsWith(".jar"))
                return true;
            else
                return false;
        }
    };
    private static final Class<?>[] CM_PARAMETER_TYPES = {QuiverInitializer.class, BaseRuleSet.class};
    private static final Class<?>[] CALLBACK_PARAMETER_TYPES = {Config.class};

    private URLClassLoader customClassLoader;
    private Config bootConfig = null;

    public static Bootstrap createBootstrap(String customClassPath, Config config) {
        Bootstrap b = new Bootstrap(customClassPath == null ? config.customClassPath : customClassPath, config);
        // dotwriter
        return b;
    }

    private static URI[] sacnForJar(String customClassPath) {
        File customFile = new File(customClassPath);
        if (customFile.isFile()) {
            URI[] uris = new URI[1];
            uris[0] = customFile.toURI();
            return uris;
        } else {
            File[] filtedFiles = customFile.listFiles(JAR_FILENAME_FILTER);
            ArrayList<URI> uriList = new ArrayList<>();
            uriList.add(customFile.toURI());
            for (File file : filtedFiles) {
                uriList.add(file.toURI());
            }
            URI[] u = new URI[uriList.size()];
            return uriList.toArray(u);
        }
    }

    private Class<?> loadClass(String className, Class<?> baseClass) {
        try {
            Class<?> cls = this.customClassLoader.loadClass(className);
            if (baseClass.isAssignableFrom(cls))
                return cls;
            else
                return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bootstrap(String customClassPath, Config bootConfig) {
        URL[] urls = FileUtil.uri2url(sacnForJar(customClassPath));
        this.customClassLoader = new URLClassLoader(urls);
        this.bootConfig = bootConfig;
    }

    public void setConfig(Config config) {
        this.bootConfig = config;
    }

    public Config getConfig() {
        return this.bootConfig;
    }

    public CompositionMachine<?> creatCompositionMachine() {
        Class<?> initializerClass = this.loadClass(this.bootConfig.initializerName, QuiverInitializer.class);
        Class<?> ruleClass = this.loadClass(this.bootConfig.ruleName, BaseRuleSet.class);
        ArrayList<Class<?>> callbackClassList = new ArrayList<>();
        for (String callbackName : this.bootConfig.callbackNames) {
            Class<?> callbackClass = this.loadClass(callbackName, MachineCallback.class);
            if (callbackClass != null)
                callbackClassList.add(callbackClass);
        }

        try {
            Object initializerObject = 
                initializerClass.getConstructor(new Class<?>[0]).newInstance(new Object[0]);
            Object ruleObject = 
                ruleClass.getConstructor(new Class<?>[0]).newInstance(new Object[0]);

            Object[] callbackObjectList = new Object[callbackClassList.size()];
            for (int i = 0; i < callbackObjectList.length; i++) {
                callbackObjectList[i] = callbackClassList.get(i).getConstructor(new Class<?>[0]).newInstance(new Object[0]);
                callbackClassList.get(i).getMethod("initialize", CALLBACK_PARAMETER_TYPES).invoke(callbackObjectList[i], this.bootConfig);
            }

            Object[] args = {initializerObject, ruleObject};
            CompositionMachine<?> machine = (CompositionMachine<?>) CompositionMachine.class.getMethod("createMachine", CM_PARAMETER_TYPES).invoke(null, args);
            for (Object callbackObject : callbackObjectList) {
                machine.addCallback((MachineCallback)callbackObject);
            }
            return machine;
            // return CompositionMachine.createMachine(initializerObject, ruleObject);

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CompositionMachine<?> boot() {
        CompositionMachine<?> machine = this.creatCompositionMachine();
        machine.execute(this.bootConfig.iterationSteps);
        return machine;
    }
}
