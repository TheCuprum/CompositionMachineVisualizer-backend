package compositionmachine.bootstrap;

import com.google.common.base.Function;

public class Config implements Cloneable {
    public String initializerName = null;
    public String ruleName = null;
    public String customClassPath = null;
    public int iterationSteps = -1;
    public String haltPredicateName = null;
    public String[] callbackNames = null;
    public String dotOutputPath = null;
    public String machineName = null;

    private static final Config DEFAULT_CONFIG;
    static {
        DEFAULT_CONFIG = new Config();
        DEFAULT_CONFIG.customClassPath = "custom/";
        DEFAULT_CONFIG.iterationSteps = 100;
        DEFAULT_CONFIG.haltPredicateName = "compositionmachine.machine.predicates.UnchangePredicate";
        DEFAULT_CONFIG.callbackNames = new String[]{"machine.callbacks.PrintBlockCallback"};
        DEFAULT_CONFIG.dotOutputPath = "data/";
        DEFAULT_CONFIG.machineName = "default";
    }

    private static Object checkNull(Config config, Config defaultConfig, Function<Config, Object> accessFunction){
        Object o = accessFunction.apply(config);
        Object od = accessFunction.apply(defaultConfig);
        return o != null ? o : od;
    }
    
    private static int checkIntPositive(Config config, Config defaultConfig, Function<Config, Integer> accessFunction){
        int num = accessFunction.apply(config);
        int defaultNum = accessFunction.apply(defaultConfig);
        return num > 0 ? num : defaultNum;
    }

    public static Config getDefault() {// configure or default
        return DEFAULT_CONFIG;
    }

    public static Config complete(Config config){
        config.customClassPath = (String) checkNull(config, DEFAULT_CONFIG, (c) -> c.customClassPath);
        config.iterationSteps = checkIntPositive(config, DEFAULT_CONFIG, (c) -> c.iterationSteps);
        config.haltPredicateName = (String) checkNull(config, DEFAULT_CONFIG, (c) -> c.haltPredicateName);
        config.callbackNames = (String[]) checkNull(config, DEFAULT_CONFIG, (c) -> c.callbackNames);
        config.dotOutputPath = (String) checkNull(config, DEFAULT_CONFIG, (c) -> c.dotOutputPath);
        config.machineName = (String) checkNull(config, DEFAULT_CONFIG, (c) -> c.machineName);
        return config;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.initializerName).append('\n');
        sb.append(this.ruleName).append('\n');
        sb.append(this.customClassPath).append('\n');
        sb.append(this.iterationSteps).append('\n');
        sb.append(this.haltPredicateName).append('\n');
        for(String n : this.callbackNames)
            sb.append(n).append('\n');
        sb.append(this.machineName).append('\n');
        return sb.toString();
    }
}
