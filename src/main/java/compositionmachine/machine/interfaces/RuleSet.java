package compositionmachine.machine.interfaces;

import java.util.ArrayList;

import compositionmachine.machine.Arrow;
import compositionmachine.machine.ConnectedQuiver;

/**
 * Base calss for 1-dimensional Composition Machine rules.
 */
public abstract class RuleSet implements BaseRuleSet<ConnectedQuiver> {

    @Override
    public int apply(ConnectedQuiver cq, Arrow arrow) {
        int arrowState = cq.getArrowState(arrow);
        ArrayList<Arrow> rightArrowList = cq.getArrowsBySource(arrow.getTargetIndex());
        ArrayList<Arrow> leftArrowList = cq.getArrowsByTarget(arrow.getSourceIndex());

        if (cq.getMaxIndex() == 1) // N1
            return this.delta1(arrowState);
        else if (leftArrowList == null && rightArrowList != null) { // N2R
            Arrow rightArrow = rightArrowList.get(0);
            return this.delta2(arrowState, cq.getArrowState(rightArrow));
        } else if (leftArrowList != null && rightArrowList == null) { // N2L
            Arrow leftArrow = leftArrowList.get(0);
            return this.delta3(cq.getArrowState(leftArrow), arrowState);
        } else if (leftArrowList != null && rightArrowList != null) { // N3
            Arrow leftArrow = leftArrowList.get(0);
            Arrow rightArrow = rightArrowList.get(0);
            return this.delta4(cq.getArrowState(leftArrow), arrowState, cq.getArrowState(rightArrow));
        } else {
            return 0;
        }
    }

    /**
     * Rule for an isolated organism.
     * 
     * @param organism The organism's state.
     * @return New state of the organism.
     */
    public abstract int delta1(int organism);

    /**
     * Rule for an organism with only right neighbour.
     * 
     * @param organism       The organism's state.
     * @param neighbourRight Right neighbour's state.
     * @return New state of the organism.
     */
    public abstract int delta2(int organism, int neighbourRight);

    /**
     * Rule for an organism with only left neighbour.
     * 
     * @param neighbourLeft Left neighbour's state.
     * @param organism      The organism's state.
     * @return New state of the organism.
     */
    public abstract int delta3(int neighbourLeft, int organism);

    /**
     * Rule for an organism with both left and right neighbour.
     * 
     * @param neighbourLeft  Left neighbour's state.
     * @param organism       The organism's state.
     * @param neighbourRight Right neighbour's state.
     * @return New state of the organism.
     */
    public abstract int delta4(int neighbourLeft, int organism, int neighbourRight);
}
