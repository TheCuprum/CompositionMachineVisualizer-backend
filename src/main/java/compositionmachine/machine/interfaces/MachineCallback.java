package compositionmachine.machine.interfaces;

import java.util.Map;

import compositionmachine.bootstrap.Config;
import compositionmachine.machine.Quiver;

/**
 * Base interface to perform custom operations during the execution of
 * Composition Machine.
 * It is suggeted that all implemented classes have a parameterless constructor.
 * 
 * @see compositionmachine.bootstrap.Bootstrap#creatCompositionMachine()
 * @see compositionmachine.machine.CompositionMachine#execute(int)
 */
public interface MachineCallback {
    /**
     * Initialize the callback with input config.
     * 
     * @param config Config to construct the machine.
     */
    public void initialize(Config config);

    /**
     * Called when execution start.
     * 
     * @param <CQ>          Type of connected quiver.
     * @param totalSteps    Total execution steps.
     * @param initialQuiver Initial quiver state.
     */
    public <CQ extends BaseConnectedQuiver<CQ>> void onExecuteStart(int totalSteps, Quiver<CQ> initialQuiver);

    /**
     * Called when each step begins.
     * 
     * @param <CQ>          Type of connected quiver.
     * @param step          Current execution steps.
     * @param quiverHistory Complete history of the machine.
     */
    public <CQ extends BaseConnectedQuiver<CQ>> void onStepBegin(int step, Map<Integer, Quiver<CQ>> quiverHistory);

    /**
     * Called when each step ends.
     * 
     * @param <CQ>          Type of connected quiver.
     * @param step          Current execution steps.
     * @param newQuiver     Next quiver state.
     * @param quiverHistory Complete history of the machine.
     */
    public <CQ extends BaseConnectedQuiver<CQ>> void onStepEnd(int step, Quiver<CQ> newQuiver, Map<Integer, Quiver<CQ>> quiverHistory);

    /**
     * Called when the machine is halted by halt predicates.
     * @param <CQ>          Type of connected quiver.
     * @param step          Current execution steps.
     * @param quiverHistory Complete history of the machine.
     * @return Necessary objects to be returned whem the machine halts.
     */
    public <CQ extends BaseConnectedQuiver<CQ>> Object onHalt(int step, Map<Integer, Quiver<CQ>> quiverHistory);
}
