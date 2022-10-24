package bootstrap;

public class Config implements Cloneable {
    public String customClassPath = null;
    public String initializerName = null;
    public String ruleName = null;
    public int iterationSteps = -1;
    public String[] callbackNames = null;
    public String dotOutputPath = null;
    public String machineName = null;

    private static final Config DEFAULT;
    static {
        DEFAULT = new Config();
        DEFAULT.customClassPath = "custom/";
        DEFAULT.iterationSteps = 100;
        DEFAULT.callbackNames = new String[0];
        DEFAULT.dotOutputPath = "data/";
    }

    public static Config getDefault() {// configure or default
        return DEFAULT;
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
}
