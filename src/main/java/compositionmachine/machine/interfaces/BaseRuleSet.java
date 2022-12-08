package compositionmachine.machine.interfaces;

import compositionmachine.machine.Arrow;

/**
 * Base rule set interface for every rule set.
 * It is suggeted that all implemented classes have a parameterless constructor.
 */
public interface BaseRuleSet<CQ extends BaseConnectedQuiver<CQ>> {
    /**
     * Applies this set of rules to an arrow in a connected quiver.
     * 
     * @param connectedQuiver Connected quiver.
     * @param arrow           Target Arrow instance.
     * @return New state of the arrow.
     */
    public int apply(CQ connectedQuiver, Arrow arrow);
}
