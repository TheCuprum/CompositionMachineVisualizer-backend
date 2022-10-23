package machine;

import java.util.ArrayList;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class Quiver<CQ extends BaseConnectedQuiver<CQ>> extends ArrayList<CQ> {
    public Quiver<CQ> snapShot() {
        Quiver<CQ> newQuiver = new Quiver<>();
        for (CQ cq : this) {
            newQuiver.add(cq.typedClone());
        }
        return newQuiver;
    }
    
}
