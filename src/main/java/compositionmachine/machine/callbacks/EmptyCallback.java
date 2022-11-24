package compositionmachine.machine.callbacks;

import java.util.Map;

import compositionmachine.bootstrap.Config;
import compositionmachine.machine.BaseConnectedQuiver;
import compositionmachine.machine.Quiver;

public class EmptyCallback implements MachineCallback {

    @Override
    public void initialize(Config config) {
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> void onExecuteStart(int totalSteps, Quiver<CQ> initialQuiver) {
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> void onStepBegin(int step, Map<Integer, Quiver<CQ>> quiverHistory) {
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> void onStepEnd(int step, Quiver<CQ> newQuiver,
            Map<Integer, Quiver<CQ>> quiverHistory) {
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> void onHalt(int step, Map<Integer, Quiver<CQ>> quiverHistory) {
    }
}
