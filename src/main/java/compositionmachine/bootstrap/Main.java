package compositionmachine.bootstrap;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import compositionmachine.machine.CompositionMachine;

public class Main {
    private static Options OPTIONS = createCommandOptions();
    private static String HELP_MESSAGE = getHelpString();

    private static Options createCommandOptions() {
        Options opt = new Options();

        opt.addOption("help", "This is a help");
        opt.addOption(Option.builder("Q").longOpt("quiver-name").argName("quiver initializer").type(String.class)
                .required().hasArg().desc("Quiver initializer class name").build());

        opt.addOption(Option.builder("R").longOpt("rule-name").argName("rule set").type(String.class)
                .required().hasArg().desc("Rule set class name").build());

        opt.addOption(Option.builder("H").longOpt("halt-predicate").argName("halt predicate").type(String.class)
                .hasArg().desc("Halt predicate class name").build());

        opt.addOption(Option.builder("cp").longOpt("custom-classpath").argName("folder").type(String.class)
                .hasArg().desc("Custom path to load classes from").build());

        opt.addOption(Option.builder("t").longOpt("step-time").argName("step").type(Integer.class)
                .hasArg().desc("CM step time").build());

        opt.addOption(Option.builder("cb").longOpt("callbacks").argName("callback.A,callback.B").type(String.class)
                .hasArg().desc("Callbacks during execution").build());

        opt.addOption(Option.builder("o").longOpt("dot-output").argName("folder").type(String.class)
                .hasArg().desc("DOT file output directory").build());

        opt.addOption(Option.builder("n").longOpt("machine-name").argName("name").type(String.class)
                .hasArg().desc("Machine name").build());

        return opt;
    }

    private static String getHelpString() {
        if (HELP_MESSAGE == null) {
            HelpFormatter helpFormatter = new HelpFormatter();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
            helpFormatter.printHelp(printWriter, HelpFormatter.DEFAULT_WIDTH, " ", null,
                    OPTIONS, HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, null);
            printWriter.flush();
            HELP_MESSAGE = new String(byteArrayOutputStream.toByteArray());
            printWriter.close();
        }
        return HELP_MESSAGE;
    }

    public static Config parseArguments(String[] args) {
        if (args.length > 0 && (args[0].equals("help") || args[0].equals("-h"))) {
            System.out.println(getHelpString());
            System.exit(0);
        }

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine result = parser.parse(OPTIONS, args);
            Config config = new Config();

            ArrayList<String> callbacks = new ArrayList<>();
            String cbOptionValue = (String) result.getOptionValue("cb");
            if (cbOptionValue != null)
                for (String callbackName : cbOptionValue.split(",")) {
                    if (callbackName != null && callbackName.length() > 0)
                        callbacks.add(callbackName);
                }
            config.initializerName = (String) result.getParsedOptionValue("Q");
            config.ruleName = (String) result.getParsedOptionValue("R");
            config.haltPredicateName = (String) result.getParsedOptionValue("H");
            config.customClassPath = (String) result.getParsedOptionValue("cp");
            config.iterationSteps = Integer.valueOf(result.getOptionValue("t", "-1"));
            config.callbackNames = callbacks.size() > 0 ? callbacks.toArray(new String[0]) : new String[0];
            config.dotOutputPath = (String) result.getParsedOptionValue("o");
            config.machineName = (String) result.getParsedOptionValue("n");

            return Config.complete(config);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(getHelpString());
            System.exit(0);
        }
        return null;
    }

    public static void main(String[] args) {
        // args = "-Q compositionmachine.examples.ExampleQuiverInitializer -R compositionmachine.examples.ExampleRules2 -H compositionmachine.machine.predicates.LoopPredicate"
        // // "-cb compositionmachine.machine.callbacks.SaveDotCallback,compositionmachine.machine.callbacks.PrintBlockCallback"
        // .split(" ");
        // args = "help".split(" ");
        // args = new String[0];
        Config config = parseArguments(args);

        // String[] callbackNames = new String[1];
        // callbackNames[0] = "compositionmachine.machine.callbacks.PrintBlockCallback";
        // String[] callbackNames = new String[2];
        // callbackNames[0] = "compositionmachine.machine.callbacks.SaveDotCallback";
        // callbackNames[1] = "compositionmachine.machine.callbacks.PrintBlockCallback";
        // Config config = new Config();
        // config.customClassPath = "./";
        // config.initializerName = "examples.ExampleQuiverInitializer";
        // config.ruleName = "examples.ExampleRules2";
        // config.haltPredicateName = "compositionmachine.machine.predicates.UnchangePredicate";
        // config.iterationSteps = 5;
        // config.callbackNames = callbackNames;
        // config.dotOutputPath = "data/";
        // config.machineName = null;
        Bootstrap b = Bootstrap.createBootstrap(config.customClassPath, config);
        CompositionMachine<?> machine = b.boot();
        // CompositionMachine<?> machine = b.creatCompositionMachine();
        // machine.execute(config.iterationSteps);

        // for (Entry<Integer, ?> tuple : machine.getQuiverHistory().entrySet()) {
        // System.out.println(tuple.getKey() + ", " + tuple.getValue());
        // }
    }
}
