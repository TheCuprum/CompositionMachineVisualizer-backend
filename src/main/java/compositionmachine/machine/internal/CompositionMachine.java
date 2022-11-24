package compositionmachine.machine.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import compositionmachine.machine.BaseConnectedQuiver;
import compositionmachine.machine.BaseRuleSet;
import compositionmachine.machine.Quiver;
import compositionmachine.machine.QuiverInitializer;
import compositionmachine.machine.callbacks.MachineCallback;
import compositionmachine.machine.predicates.HaltPredicate;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class CompositionMachine<CQ extends BaseConnectedQuiver<CQ>> {

    private final BaseRuleSet<CQ> rules;
    private final LinkedHashMap<Integer, Quiver<CQ>> quiverHistory;
    private HaltPredicate haltPredicate;
    // private final Quiver<CQ> currentQuiver;
    private final ArrayList<MachineCallback> callbacks;

    public static <Q extends BaseConnectedQuiver<Q>> CompositionMachine<Q> createMachine(QuiverInitializer<Q> qInit,
            BaseRuleSet<Q> rules, HaltPredicate predicate) {
        return createMachine(qInit.generateQuiver(), rules, predicate);
    }

    public static <Q extends BaseConnectedQuiver<Q>> CompositionMachine<Q> createMachine(Quiver<Q> quiver,
            BaseRuleSet<Q> rules, HaltPredicate predicate) {
        // LogUtil.printQuiverContent(quiver);
        return new CompositionMachine<Q>(quiver, rules, predicate);
    }

    public CompositionMachine(Quiver<CQ> cq, BaseRuleSet<CQ> rules, HaltPredicate predicate) {
        this.rules = rules;
        this.quiverHistory = new LinkedHashMap<>();
        this.quiverHistory.put(0, cq);
        this.haltPredicate = predicate;
        // this.currentQuiver = cq;
        this.callbacks = new ArrayList<>();
    }

    public Map<Integer, Quiver<CQ>> getQuiverHistory() {
        return this.quiverHistory;
    }

    public void addCallback(MachineCallback callback) {
        if (callback != null)
            this.callbacks.add(callback);
    }

    public void execute(int untilTime) {
        for (MachineCallback cb : this.callbacks)
            cb.onExecuteStart(untilTime, this.quiverHistory.get(0));

        for (int i = 1; i < untilTime; i++) {
            for (MachineCallback cb : this.callbacks)
                cb.onStepBegin(i, this.quiverHistory);

            Quiver<CQ> oldQuiver = this.quiverHistory.get(i - 1);
            Quiver<CQ> newQuiver = updateGlobalState(oldQuiver);
            this.quiverHistory.put(i, newQuiver);

            if (this.haltPredicate.testHalt(i, this.quiverHistory)) { // halt check
                System.out.println("HALTS AT TIME " + i + "!");
                for (MachineCallback cb : this.callbacks)
                    cb.onHalt(i, this.quiverHistory);
                return;
            }

            for (MachineCallback cb : this.callbacks)
                cb.onStepEnd(i, this.quiverHistory.get(i), this.quiverHistory);
        }
    }

    // return value is next state
    private Quiver<CQ> updateGlobalState(Quiver<CQ> currentQuiver) {
        Quiver<CQ> newQuiver = currentQuiver.snapShot();

        for (int i = 0; i < currentQuiver.size(); i++) {
            Iterator<Arrow> arrowIterator = currentQuiver.get(i).getArrowIterator();
            while (arrowIterator.hasNext()) {
                Arrow arrow = arrowIterator.next();
                Integer state = this.rules.apply(currentQuiver.get(i), arrow);
                newQuiver.get(i).updateArrowState(arrow, state);
                // System.out.print(state);
            }
            // System.out.print(" ");
        }
        // System.out.println(currentQuiver);

        return newQuiver;
    }
}
