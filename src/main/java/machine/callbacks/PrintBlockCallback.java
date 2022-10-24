package machine.callbacks;

import java.util.Map;

import bootstrap.Config;
import machine.BaseConnectedQuiver;
import machine.Quiver;

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

}
