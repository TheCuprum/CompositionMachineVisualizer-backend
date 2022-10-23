package examples;

import machine.ConnectedQuiver;
import machine.Quiver;
import machine.QuiverInitializer;
import machine.internal.CompositionMachine;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class Examples {

    private static QuiverInitializer<ConnectedQuiver> initializer = new ExampleQuiverInitializer();

    public static Quiver<ConnectedQuiver> getInitialConfiguration() {
        return initializer.generateQuiver();
    }

    public static void main(String[] args) {

        // Elementary cellular automata [rule (elements in the repeating pattern)]:
        // 122 (6), 110 (5), 102, (8), 62 (6), 60 (5)

        CompositionMachine<ConnectedQuiver> m = new CompositionMachine<ConnectedQuiver>(
                getInitialConfiguration(), new ExampleRules2());
        // Machine m = new CompositionMachine(
        // getInitialConfiguration(), new MyRules2()
        // );

        m.execute(9);

    }
}
