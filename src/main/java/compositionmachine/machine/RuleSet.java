package compositionmachine.machine;

import java.util.ArrayList;

import compositionmachine.machine.internal.Arrow;

public abstract class RuleSet implements BaseRuleSet<ConnectedQuiver> {

    @Override
    public int apply(ConnectedQuiver cq, Arrow arrow){
        int arrowState = cq.getArrowState(arrow);
        ArrayList<Arrow> rightArrowList = cq.getArrowsBySource(arrow.getTargetIndex());
        ArrayList<Arrow> leftArrowList = cq.getArrowsByTarget(arrow.getSourceIndex());

        if (cq.getMaxIndex() == 1) // N1
            return this.delta1(arrowState);
        else if (leftArrowList == null && rightArrowList != null){ // N2R
            Arrow rightArrow = rightArrowList.get(0);
            return this.delta2(arrowState, cq.getArrowState(rightArrow));
        }else if (leftArrowList != null && rightArrowList == null){ // N2L
            Arrow leftArrow = leftArrowList.get(0);
            return this.delta3(cq.getArrowState(leftArrow), arrowState);
        }else if (leftArrowList != null && rightArrowList != null){ // N3
            Arrow leftArrow = leftArrowList.get(0);
            Arrow rightArrow = rightArrowList.get(0);
            return this.delta4(cq.getArrowState(leftArrow), arrowState, cq.getArrowState(rightArrow));
        }else{
            return 0;
        }
    }

    public abstract int delta1(int organismState);

    public abstract int delta2(int organismState, int neighbourRight);

    public abstract int delta3(int neighbourLeft, int organismState);

    public abstract int delta4(int neighbourLeft, int organismState, int neighbourRight);
}
