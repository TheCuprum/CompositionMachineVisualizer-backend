package machine.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import machine.BaseConnectedQuiver;
import machine.BaseRuleSet;
import machine.Quiver;
import machine.QuiverInitializer;
import machine.callbacks.MachineCallback;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class CompositionMachine<CQ extends BaseConnectedQuiver<CQ>> {

    private final BaseRuleSet<CQ> rules;
    private final LinkedHashMap<Integer, Quiver<CQ>> quiverHistory;
    // private final Quiver<CQ> currentQuiver;
    private final ArrayList<MachineCallback> callbacks;

    public static <Q extends BaseConnectedQuiver<Q>> CompositionMachine<Q> createMachine(QuiverInitializer<Q> qInit, BaseRuleSet<Q> rules){
        return createMachine(qInit.generateQuiver(), rules);
    }
    public static <Q extends BaseConnectedQuiver<Q>> CompositionMachine<Q> createMachine(Quiver<Q> quiver, BaseRuleSet<Q> rules){
        // LogUtil.printQuiverContent(quiver);
        return new CompositionMachine<Q>(quiver, rules);
    }

    public CompositionMachine(Quiver<CQ> cq, BaseRuleSet<CQ> rules) {
        this.rules = rules;
        this.quiverHistory = new LinkedHashMap<>();
        this.quiverHistory.put(0, cq);
        // this.currentQuiver = cq;
        this.callbacks = new ArrayList<>();
    }

    public Map<Integer, Quiver<CQ>> getQuiverHistory(){
        return this.quiverHistory;
    }

    public void addCallback(MachineCallback callback){
        if (callback != null)
            this.callbacks.add(callback);
    }

    public void execute(int untilTime) {      
        for (MachineCallback cb : this.callbacks) 
                cb.onExecuteStart(untilTime, this.quiverHistory.get(0));

        for (int i = 1; i <= untilTime; i++) {
            for (MachineCallback cb : this.callbacks) 
                cb.onStepBegin(i, this.quiverHistory);

            Quiver<CQ> oldQuiver = this.quiverHistory.get(i - 1);        
            Quiver<CQ> newQuiver = updateGlobalState(oldQuiver);
            this.quiverHistory.put(i, newQuiver);
            
            if (halts(this.quiverHistory.get(0), newQuiver)) {
                System.out.println(newQuiver);
                System.out.println(this.quiverHistory.get(0));
                System.out.println("HALTS AT TIME " + i + "!");
                System.exit(0);
            }
            
            for (MachineCallback cb : this.callbacks) 
                cb.onStepEnd(i, this.quiverHistory.get(i), this.quiverHistory);
        }
    }

    public boolean halts(Quiver<?> q) {
        return false;

        // what is this.

        // int compositeCounter = 0;
        // int bitCounterCounter = 0;

        // for(ConnectedQuiver cq : q) {

        // if(cq.get(0) == 1) bitCounterCounter++;

        // for(int i = 1; i < cq.size(); i++) {

        // if(cq.get(i) == 1) bitCounterCounter++;

        // if(cq.get(i-1) == 1 && cq.get(i) == 1) {
        // compositeCounter ++;
        // if(compositeCounter > 1) return false;
        // }
        // }
        // }

        // return bitCounterCounter == 2 && compositeCounter==1;
    }

    public boolean halts(Quiver<?> current, Quiver<?> previous) {
        // return current.equals(previous);
        return false;
        // return current.equals(quiverHistory.get(0));
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
            // System.out.print("  ");
        }
        // System.out.println(currentQuiver);

        return newQuiver;
    }
}
