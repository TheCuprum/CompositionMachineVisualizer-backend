package compositionmachine.machine.predicates;

import java.util.LinkedHashMap;

import compositionmachine.machine.Quiver;
import compositionmachine.machine.interfaces.BaseConnectedQuiver;
import compositionmachine.machine.interfaces.HaltPredicate;

public class NullPredicate implements HaltPredicate{
    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> boolean testHalt(int step,
            LinkedHashMap<Integer, Quiver<CQ>> quiverHistory) {
        return false;
    }
}