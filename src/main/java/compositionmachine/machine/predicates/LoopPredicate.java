package compositionmachine.machine.predicates;

import java.util.LinkedHashMap;

import compositionmachine.machine.BaseConnectedQuiver;
import compositionmachine.machine.Quiver;

public class LoopPredicate implements HaltPredicate {
    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> boolean testHalt(int step,
            LinkedHashMap<Integer, Quiver<CQ>> quiverHistory) {
        if (step > 0)
            return quiverHistory.get(step).equals(quiverHistory.get(step / 2));
        else
            return false;
    }
}
