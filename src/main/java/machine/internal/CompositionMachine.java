package machine.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import machine.BaseConnectedQuiver;
import machine.BaseRuleSet;
import machine.Quiver;
import machine.QuiverInitializer;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class CompositionMachine<CQ extends BaseConnectedQuiver<CQ>> {

    private final BaseRuleSet<CQ> rules;
    private final Map<Integer, Quiver<CQ>> quiverHistory;
    private final Quiver<CQ> currentQuiver;

    public static <Q extends BaseConnectedQuiver<Q>> CompositionMachine<Q> createMachine(QuiverInitializer<Q> qInit, BaseRuleSet<Q> rules){
        return createMachine(qInit.generateQuiver(), rules);
    }
    public static <Q extends BaseConnectedQuiver<Q>> CompositionMachine<Q> createMachine(Quiver<Q> quiver, BaseRuleSet<Q> rules){
        // LogUtil.printQuiverContent(quiver);
        return new CompositionMachine<Q>(quiver, rules);
    }

    public CompositionMachine(Quiver<CQ> cq, BaseRuleSet<CQ> rules) {
        this.rules = rules;
        this.quiverHistory = new HashMap<>();
        this.currentQuiver = cq;
    }

    public Map<Integer, Quiver<CQ>> getQuiverHistory(){
        return this.quiverHistory;
    }

    public void execute(int untilTime) {      
        for (int i = 0; i < untilTime; i++) {           
            Quiver<CQ> quiverSnapshot = updateGlobalState(this.currentQuiver);
            System.out.println(quiverSnapshot + "  t=" + i);
            this.quiverHistory.put(i, quiverSnapshot);
            
            if (halts(this.currentQuiver, quiverSnapshot)) {
                System.out.println(quiverSnapshot);
                System.out.println(this.currentQuiver);
                System.out.println("HALTS AT TIME " + (i + 1) + "!");
                System.exit(0);
            }
        }
        System.out.println(currentQuiver + "  t=" + untilTime);
        this.quiverHistory.put(untilTime, currentQuiver);
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

    // return value is snapshot of curentQuiver
    private Quiver<CQ> updateGlobalState(Quiver<CQ> currentQuiver) {
        Quiver<CQ> oldQuiver = currentQuiver.snapShot();

        for (int i = 0; i < currentQuiver.size(); i++) {
            Iterator<Arrow> arrowIterator = oldQuiver.get(i).getArrowIterator();
            while (arrowIterator.hasNext()) {
                Arrow arrow = arrowIterator.next();
                Integer state = this.rules.apply(oldQuiver.get(i), arrow);
                currentQuiver.get(i).updateArrowState(arrow, state);
                // System.out.print(state);
            }
            // System.out.print("  ");
        }
        // System.out.println(currentQuiver);

        return oldQuiver;
    }
}
