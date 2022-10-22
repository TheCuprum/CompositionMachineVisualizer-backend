package machine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class CompositionMachine {

    private final BaseRuleSet<BaseConnectedQuiver> rules;
    private final Map<Integer, Quiver> quiverHistory;
    private final Quiver currentQuiver;

    public CompositionMachine(Quiver q, BaseRuleSet<BaseConnectedQuiver> rules) {
        this.rules = rules;
        this.quiverHistory = new HashMap<>();
        this.currentQuiver = q;
    }

    public void execute(int untilTime) {

        
        for (int i = 0; i < untilTime; i++) {
            
            Quiver quiverSnapshot = updateGlobalState(this.currentQuiver);
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
    }

    public boolean halts(Quiver q) {
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

    public boolean halts(Quiver current, Quiver previous) {
        // return current.equals(previous);
        return current.equals(quiverHistory.get(0));
    }

    // return value is snapshot of curentQuiver
    private Quiver updateGlobalState(Quiver currentQuiver) {
        Quiver newQuiver = currentQuiver.snapShot();

        for (int i = 0; i < currentQuiver.size(); i++) {
            Iterator<Arrow> arrowIterator = newQuiver.get(i).getArrowIterator();
            while (arrowIterator.hasNext()) {
                Arrow arrow = arrowIterator.next();
                currentQuiver.get(i).updateArrowState(arrow, rules.apply(newQuiver.get(i), arrow));
            }
        }

        return newQuiver;
    }
}
