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
    /**
     * Judges whether the Composition Machine should be halt.
     * 
     * @param <CQ>          Type of quiver.
     * @param step          Current execution step.
     * @param quiverHistory Complete execution history.
     * @return True if the machine should halt, false if the machine should continue
     *         running.
     */
    public <CQ extends BaseConnectedQuiver<CQ>> boolean testHalt(int step,
            LinkedHashMap<Integer, Quiver<CQ>> quiverHistory);
}