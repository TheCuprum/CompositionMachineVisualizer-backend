package compositionmachine.machine.interfaces;

import java.util.Map;

import compositionmachine.bootstrap.Config;
import compositionmachine.machine.Quiver;

public interface MachineCallback {   
    public void initialize(Config config);

    public <CQ extends BaseConnectedQuiver<CQ>> void onExecuteStart(int totalSteps, Quiver<CQ> initialQuiver);

    public <CQ extends BaseConnectedQuiver<CQ>> void onStepBegin(int step, Map<Integer, Quiver<CQ>> quiverHistory);
    
    public <CQ extends BaseConnectedQuiver<CQ>> void onStepEnd(int step, Quiver<CQ> newQuiver, Map<Integer, Quiver<CQ>> quiverHistory);

    public <CQ extends BaseConnectedQuiver<CQ>> Object onHalt(int step, Map<Integer, Quiver<CQ>> quiverHistory);
}
