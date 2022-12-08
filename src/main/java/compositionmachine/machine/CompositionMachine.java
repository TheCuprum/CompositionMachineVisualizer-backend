package compositionmachine.machine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import compositionmachine.machine.interfaces.BaseConnectedQuiver;
import compositionmachine.machine.interfaces.BaseRuleSet;
import compositionmachine.machine.interfaces.HaltPredicate;
import compositionmachine.machine.interfaces.MachineCallback;
import compositionmachine.machine.interfaces.QuiverInitializer;

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

    /**
     * Creates Composition Machine by QuiverInitializer, BaseRuleSet and
     * HaltPredicate.
     * 
     * @param <Q>       Target type of connected quiver the machine going to execute
     *                  on.
     * @param qInit     A QuiverInitializer instance to generate a new quiver.
     * @param rules     A set of rules that applicable on target type of connected
     *                  quiver.
     * @param predicate A HaltPredicate to check if the machine should halt.
     * @return A new CompositionMachine instance.
     */
    public static <Q extends BaseConnectedQuiver<Q>> CompositionMachine<Q> createMachine(QuiverInitializer<Q> qInit,
            BaseRuleSet<Q> rules, HaltPredicate predicate) {
        return createMachine(qInit.generateQuiver(), rules, predicate);
    }

    /**
     * Creates Composition Machine by Quiver, BaseRuleSet and HaltPredicate.
     * 
     * @param <Q>       Target type of connected quiver the machine going to execute
     *                  on.
     * @param quiver    A Quiver instance the machine will operate on.
     * @param rules     A set of rules that applicable on target type of connected
     *                  quiver.
     * @param predicate A HaltPredicate to check if the machine should halt.
     * @return A new CompositionMachine instance.
     */
    public static <Q extends BaseConnectedQuiver<Q>> CompositionMachine<Q> createMachine(Quiver<Q> quiver,
            BaseRuleSet<Q> rules, HaltPredicate predicate) {
        // LogUtil.printQuiverContent(quiver);
        return new CompositionMachine<Q>(quiver, rules, predicate);
    }

    /**
     * Creates Composition Machine by Quiver, BaseRuleSet and HaltPredicate.
     * 
     * @param cq        A Quiver instance the machine will operate on.
     * @param rules     A set of rules that applicable on target type of connected
     *                  quiver.
     * @param predicate A HaltPredicate to check if the machine should halt.
     */
    public CompositionMachine(Quiver<CQ> cq, BaseRuleSet<CQ> rules, HaltPredicate predicate) {
        this.rules = rules;
        this.quiverHistory = new LinkedHashMap<>();
        this.quiverHistory.put(0, cq);
        this.haltPredicate = predicate;
        // this.currentQuiver = cq;
        this.callbacks = new ArrayList<>();
    }

    /**
     * Gets complete history of the execution of the machine.
     * 
     * @return An instance of Map<Integer, Quiver<CQ>> which maps each step number
     *         to coresponding quiver.
     */
    public Map<Integer, Quiver<CQ>> getQuiverHistory() {
        return this.quiverHistory;
    }

    /**
     * Adds a callback to the machine.
     * 
     * @param callback A callback instance which implements MachineCallback
     *                 interface.
     * @see compositionmachine.machine.interfaces.MachineCallback
     */
    public void addCallback(MachineCallback callback) {
        if (callback != null)
            this.callbacks.add(callback);
    }

    /**
     * Execute the machine from start to {@code untilTime} steps.
     * 
     * @param untilTime Step count to be exected.
     * @return An array of length 0 if the machine dosen't halt, otherwise return an
     *         array of objects containing halt step and return values from
     *         callbacks.
     * @see compositionmachine.machine.interfaces.MachineCallback
     */
    public Object[] execute(int untilTime) {
        for (MachineCallback cb : this.callbacks)
            cb.onExecuteStart(untilTime, this.quiverHistory.get(0));

        for (int i = 1; i < untilTime; i++) {
            for (MachineCallback cb : this.callbacks)
                cb.onStepBegin(i, this.quiverHistory);

            Quiver<CQ> oldQuiver = this.quiverHistory.get(i - 1);
            Quiver<CQ> newQuiver = updateGlobalState(oldQuiver);
            this.quiverHistory.put(i, newQuiver);

            if (this.haltPredicate.testHalt(i, this.quiverHistory)) { // halt check
                ArrayList<Object> haltResult = new ArrayList<>(this.callbacks.size() + 1);
                haltResult.add(i); // halt step
                for (MachineCallback cb : this.callbacks)
                    haltResult.add(cb.onHalt(i, this.quiverHistory));
                return haltResult.toArray();
            }

            for (MachineCallback cb : this.callbacks)
                cb.onStepEnd(i, this.quiverHistory.get(i), this.quiverHistory);
        }
        return new Object[0];
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
