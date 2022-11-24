package compositionmachine.bootstrap;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import compositionmachine.machine.BaseRuleSet;
import compositionmachine.machine.QuiverInitializer;
import compositionmachine.machine.callbacks.MachineCallback;
import compositionmachine.machine.internal.CompositionMachine;
import compositionmachine.machine.predicates.HaltPredicate;
import compositionmachine.util.FileUtil;

/**
 * Bootstrap class to create and boot Composition Machines.
 */
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
    private static final Class<?>[] CM_PARAMETER_TYPES = { QuiverInitializer.class, BaseRuleSet.class,
            HaltPredicate.class };
    private static final Class<?>[] CALLBACK_PARAMETER_TYPES = { Config.class };

    private URLClassLoader customClassLoader;
    private Config bootConfig = null;

    /**
     * Create bootstrap instance.
     * 
     * @param customClassPath Customized directory to load external classes from.
     * @param config          Bootstrap configuration.
     * @return Created bootstrap instance.
     */
    public static Bootstrap createBootstrap(String customClassPath, Config config) {
        Bootstrap b = new Bootstrap(customClassPath == null ? config.customClassPath : customClassPath, config);
        // dotwriter
        return b;
    }

    /**
     * Sacn for external class jars.
     * 
     * @param customClassPath Customized directory to load external classes from.
     * @return All found jars in specified path.
     */
    private static ArrayList<URI> sacnForJar(String customClassPath) {
        ArrayList<URI> uris = new ArrayList<>();
        File customFile = new File(customClassPath);
        if (customFile.isFile()) {
            uris.add(customFile.toURI());
            return uris;
        } else {
            File[] filtedFiles = customFile.listFiles(JAR_FILENAME_FILTER);
            ArrayList<URI> uriList = new ArrayList<>();
            if (filtedFiles != null) {
                uriList.add(customFile.toURI());
                for (File file : filtedFiles) {
                    uriList.add(file.toURI());
                }
            }else{
                System.err.println("Missing custom class path: ".concat(customClassPath));
            }
            return uriList;
        }
    }

    /**
     * Load external class by its name.
     * 
     * @param className Fully Qualified Name of target class.
     * @param baseClass Base class the class should inherited.
     * @return Loaded external class.
     */
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

    /**
     * Constructor.
     * 
     * @param customClassPath Customized directory to load external classes from.
     * @param bootConfig      Bootstrap configuration.
     */
    public Bootstrap(String customClassPath, Config bootConfig) {
        URL[] urls = FileUtil.uri2url(sacnForJar(customClassPath));
        this.customClassLoader = new URLClassLoader(urls);
        this.bootConfig = bootConfig;
    }

    /**
     * Sets bootstrap config after instance created
     * 
     * @param config Bootstrap configuration.
     */
    public void setConfig(Config config) {
        this.bootConfig = config;
    }

    /**
     * Getter for bootstrap config.
     * 
     * @return Bootstrap configuration instance.
     */
    public Config getConfig() {
        return this.bootConfig;
    }

    /**
     * Cteates Composition Machine.
     * 
     * @return Created Composition Machine instance.
     */
    public CompositionMachine<?> creatCompositionMachine() {
        Class<?> initializerClass = this.loadClass(this.bootConfig.initializerName, QuiverInitializer.class);
        Class<?> ruleClass = this.loadClass(this.bootConfig.ruleName, BaseRuleSet.class);
        Class<?> haltPredicateClass = this.loadClass(this.bootConfig.haltPredicateName, HaltPredicate.class);
        ArrayList<Class<?>> callbackClassList = new ArrayList<>();
        for (String callbackName : this.bootConfig.callbackNames) {
            Class<?> callbackClass = this.loadClass(callbackName, MachineCallback.class);
            if (callbackClass != null)
                callbackClassList.add(callbackClass);
        }

        try {
            Object initializerObject = initializerClass.getConstructor(new Class<?>[0]).newInstance(new Object[0]);
            Object ruleObject = ruleClass.getConstructor(new Class<?>[0]).newInstance(new Object[0]);
            Object haltPredicateObject = haltPredicateClass.getConstructor(new Class<?>[0]).newInstance(new Object[0]);

            Object[] callbackObjectList = new Object[callbackClassList.size()];
            for (int i = 0; i < callbackObjectList.length; i++) {
                callbackObjectList[i] = callbackClassList.get(i).getConstructor(new Class<?>[0])
                        .newInstance(new Object[0]);
                callbackClassList.get(i).getMethod("initialize", CALLBACK_PARAMETER_TYPES).invoke(callbackObjectList[i],
                        this.bootConfig);
            }

            Object[] args = { initializerObject, ruleObject, haltPredicateObject };
            CompositionMachine<?> machine = (CompositionMachine<?>) CompositionMachine.class
                    .getMethod("createMachine", CM_PARAMETER_TYPES).invoke(null, args);
            for (Object callbackObject : callbackObjectList) {
                machine.addCallback((MachineCallback) callbackObject);
            }
            return machine;
            // return CompositionMachine.createMachine(initializerObject, ruleObject);

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create and execute Composition Machine in one call.
     * 
     * @return Executed Composition Machine instance.
     */
    public CompositionMachine<?> boot() {
        CompositionMachine<?> machine = this.creatCompositionMachine();
        machine.execute(this.bootConfig.iterationSteps);
        return machine;
    }
}
