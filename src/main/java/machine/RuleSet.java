package machine;

import java.util.ArrayList;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public interface RuleSet extends BaseRuleSet<ConnectedQuiver> {

    public static int apply1Deg(RuleSet rules, ConnectedQuiver cq, Arrow arrow){
        int arrowState = cq.getArrowState(arrow);
        ArrayList<Arrow> rightArrowList = cq.getArrowsBySource(arrow.getTargetIndex());
        ArrayList<Arrow> leftArrowList = cq.getArrowsByTarget(arrow.getSourceIndex());

        if (cq.getMaxIndex() == 1) // N1
            return rules.delta1(arrowState);
        else if (leftArrowList == null && rightArrowList != null){
            Arrow rightArrow = rightArrowList.get(0);
            return rules.delta2(arrowState, cq.getArrowState(rightArrow));
        }else if (leftArrowList != null && rightArrowList == null){
            Arrow leftArrow = leftArrowList.get(0);
            return rules.delta3(cq.getArrowState(leftArrow), arrowState);
        }else if (leftArrowList != null && rightArrowList != null){
            Arrow leftArrow = leftArrowList.get(0);
            Arrow rightArrow = rightArrowList.get(0);
            return rules.delta4(cq.getArrowState(leftArrow), arrowState, cq.getArrowState(rightArrow));
        }else{
            return 0;
        }
    }

    public int delta1(int organismState);

    public int delta2(int organismState, int neighbourRight);

    public int delta3(int neighbourLeft, int organismState);

    public int delta4(int neighbourLeft, int organismState, int neighbourRight);
}
