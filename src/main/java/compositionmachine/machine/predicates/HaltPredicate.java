package compositionmachine.machine.predicates;

import java.util.LinkedHashMap;

import compositionmachine.machine.BaseConnectedQuiver;
import compositionmachine.machine.Quiver;

/**
 * HaltPredicate
 */
public interface HaltPredicate {
    public <CQ extends BaseConnectedQuiver<CQ>> boolean testHalt(int step,
            LinkedHashMap<Integer, Quiver<CQ>> quiverHistory);
}