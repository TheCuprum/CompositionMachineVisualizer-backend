package compositionmachine.machine;

import java.util.ArrayList;

import compositionmachine.machine.interfaces.BaseConnectedQuiver;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class Quiver<CQ extends BaseConnectedQuiver<CQ>> extends ArrayList<CQ> {
    /**
     * Clones the quiver without refreshing every connected quiver's cache.
     * 
     * @return A not fully-cloned quiver, should only be used in read-only
     *         operations.
     */
    public Quiver<CQ> snapShot() {
        Quiver<CQ> newQuiver = new Quiver<>();
        for (CQ cq : this) {
            newQuiver.add(cq.typedClone());
        }
        return newQuiver;
    }
}
