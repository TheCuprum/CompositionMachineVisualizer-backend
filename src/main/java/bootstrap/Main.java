package bootstrap;

import java.util.Map.Entry;

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
        Config config = new Config();
        config.customClassPath = "./";
        config.dotOutputPath = "";
        config.initializerName = "examples.ExampleQuiverInitializer";
        config.ruleName = "examples.ExampleRules2";
        config.iterationSteps = 10;
        Bootstrap b = Bootstrap.createBootstrap("./", config);
        CompositionMachine<?> machine = b.creatCompositionMachine();
        machine.execute(10);
        
        // for (Entry<Integer, ?> tuple : machine.getQuiverHistory().entrySet()) {
        //     System.out.println(tuple.getKey() + ", " + tuple.getValue());
        // }
    }
}
