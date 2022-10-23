package bootstrap;

public class Config {
    public String dotOutputPath;
    public String customClassPath;
    public int iterationSteps;
    public String initializerName;
    public String ruleName;

    private static final Config DEFAULT;
    static{
        DEFAULT = new Config();
        DEFAULT.dotOutputPath = "data/";
        DEFAULT.customClassPath = "custom/";
        DEFAULT.iterationSteps = 100;
        DEFAULT.initializerName = null;
        DEFAULT.ruleName = null;
    }

    public static Config getDefault(){// configure or default
        return DEFAULT;
    }
}
