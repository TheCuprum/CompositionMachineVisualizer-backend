package compositionmachine.machine.callbacks;

import java.util.Map;

import compositionmachine.bootstrap.Config;
import compositionmachine.machine.Quiver;
import compositionmachine.machine.interfaces.BaseConnectedQuiver;
import compositionmachine.machine.interfaces.MachineCallback;

public class PrintBlockCallback implements MachineCallback {
    @Override
    public void initialize(Config config) {
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> void onExecuteStart(int totalSteps, Quiver<CQ> initialQuiver) {
        System.out.println(initialQuiver + "  t=0");
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> void onStepBegin(int step, Map<Integer, Quiver<CQ>> quiverHistory) {
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> void onStepEnd(int step, Quiver<CQ> newQuiver,
            Map<Integer, Quiver<CQ>> quiverHistory) {
        System.out.println(newQuiver + "  t=" + step);
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> Object onHalt(int step, Map<Integer, Quiver<CQ>> quiverHistory) {
        System.out.println("HALTS AT TIME " + step + "!");

        System.out.println(quiverHistory.get(0) + "  t=" + 0);
        System.out.println(quiverHistory.get(step) + "  t=" + step);
        
        return null;
    }

}
