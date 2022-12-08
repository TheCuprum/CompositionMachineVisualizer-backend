package compositionmachine.machine.interfaces;

import java.util.LinkedHashMap;

import compositionmachine.machine.Quiver;

/**
 * Base interface to provide the ability of judging a halt during the
 * execution of the Composition Machine.
 * It is suggeted that all implemented classes have a parameterless constructor.
 * 
 * @see compositionmachine.machine.CompositionMachine#execute(int)
 */
public interface HaltPredicate {
    public <CQ extends BaseConnectedQuiver<CQ>> boolean testHalt(int step,
            LinkedHashMap<Integer, Quiver<CQ>> quiverHistory);
}