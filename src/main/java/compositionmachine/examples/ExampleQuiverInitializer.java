package compositionmachine.examples;

import compositionmachine.machine.ConnectedQuiver;
import compositionmachine.machine.Quiver;
import compositionmachine.machine.QuiverInitializer;

public class ExampleQuiverInitializer implements QuiverInitializer<ConnectedQuiver>{
    @Override
    public Quiver<ConnectedQuiver> generateQuiver() {
        ConnectedQuiver q1 = new ConnectedQuiver();
        q1.addArrow(1);
        q1.addArrow(1);
        q1.addArrow(0);
        q1.addArrow(1);
        q1.addArrow(1);
        q1.addArrow(0);

        ConnectedQuiver q2 = new ConnectedQuiver();
        q2.addArrow(1);

        ConnectedQuiver q3 = new ConnectedQuiver();
        q3.addArrow(0);
        q3.addArrow(0);
        q3.addArrow(1);

        Quiver<ConnectedQuiver> q = new Quiver<>();
        q.add(q1);
        q.add(q2);
        q.add(q3);

        return q;
    };
}
