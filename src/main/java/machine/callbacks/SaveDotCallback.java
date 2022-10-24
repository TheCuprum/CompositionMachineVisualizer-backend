package machine.callbacks;

import java.util.Map;

import bootstrap.Config;
import machine.BaseConnectedQuiver;
import machine.Quiver;
import util.DotWriter;
import util.Util;

public class SaveDotCallback implements MachineCallback {

    DotWriter writer;
    int suffixLength = 0;

    @Override
    public void initialize(Config config) {
        this.writer = new DotWriter(config.dotOutputPath, config.machineName, true);
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> void onExecuteStart(int totalSteps, Quiver<CQ> initialQuiver) {
        while (totalSteps / 10 > 0) {
            this.suffixLength++;
            totalSteps /= 10;
        }
        this.writer.writeDotFile(initialQuiver, Util.leftPadString("0.dot", this.suffixLength, '0'));
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> void onStepBegin(int step, Map<Integer, Quiver<CQ>> quiverHistory) {
    }

    @Override
    public <CQ extends BaseConnectedQuiver<CQ>> void onStepEnd(int step, Quiver<CQ> newQuiver,
            Map<Integer, Quiver<CQ>> quiverHistory) {
        this.writer.writeDotFile(newQuiver, Util.leftPadString(step + ".dot", this.suffixLength, '0'));
    }
}
