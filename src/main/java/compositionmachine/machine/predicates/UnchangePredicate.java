package compositionmachine.machine.predicates;

import java.util.LinkedHashMap;

import compositionmachine.machine.Quiver;
import compositionmachine.machine.interfaces.BaseConnectedQuiver;
import compositionmachine.machine.interfaces.HaltPredicate;

/**
 * Halts the machine if the machine's state doesn't change.
 */
public class UnchangePredicate implements HaltPredicate {

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> boolean testHalt(int step,
            LinkedHashMap<Integer, Quiver<CQ>> quiverHistory) {
        if (step > 0)
            return quiverHistory.get(step).equals(quiverHistory.get(step - 1));
        else
            return false;
    }

}
