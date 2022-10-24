package bootstrap;

import machine.internal.CompositionMachine;

public class Main {
    public static Config parseArguments(String[] args){
        /*
         * loaderpath
         * steps
         * data_dir
         * custom initializer, rule class
         * httpserver
         */

         return Config.getDefault();
    }
    
    public static void main(String[] args) {
        /* TODO:
         * parse args
         * choose initializer
         * choose rules
         * 
         * 
         */
        String[] callbackNames = new String[2];
        // callbackNames[0] = "machine.callbacks.PrintBlockCallback";
        callbackNames[0] = "machine.callbacks.SaveDotCallback";
        callbackNames[1] = "machine.callbacks.PrintBlockCallback";
        Config config = new Config();
        config.customClassPath = "./";
        config.initializerName = "examples.ExampleQuiverInitializer";
        config.ruleName = "examples.ExampleRules2";
        config.iterationSteps = 5;
        config.callbackNames = callbackNames;
        config.dotOutputPath = "data/";
        config.machineName = null;
        Bootstrap b = Bootstrap.createBootstrap("./", config);
        CompositionMachine<?> machine = b.creatCompositionMachine();
        machine.execute(5);
        
        // for (Entry<Integer, ?> tuple : machine.getQuiverHistory().entrySet()) {
        //     System.out.println(tuple.getKey() + ", " + tuple.getValue());
        // }
    }
}
