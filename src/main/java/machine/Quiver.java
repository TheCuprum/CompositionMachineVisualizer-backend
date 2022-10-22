package machine;

import java.util.ArrayList;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class Quiver extends ArrayList<BaseConnectedQuiver> {
    public Quiver snapShot() {
        Quiver newQuiver = new Quiver();
        for (BaseConnectedQuiver bcq : this) {
            newQuiver.add(bcq.snapshot());
        }
        return newQuiver;
    }
}
