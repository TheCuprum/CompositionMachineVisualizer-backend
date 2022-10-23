package examples;

import machine.RuleSet;

/**
 * @author Damian Arellanes
 *         NOT, XOR and Rule 54
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class ExampleRules1 extends RuleSet {

    @Override
    public int delta1(int organism) {
        return (organism == 0) ? 1 : 0;
    }

    @Override
    public int delta2(int organism, int neighbourRight) {
        return ((organism == 1 && neighbourRight == 0) ||
                (organism == 0 && neighbourRight == 1)) ? 1 : 0;
    }

    @Override
    public int delta3(int neighbourLeft, int organism) {
        return ((organism == 1 && neighbourLeft == 0) ||
                (organism == 0 && neighbourLeft == 1)) ? 1 : 0;
    }

    @Override
    public int delta4(int neighbourLeft, int organism, int neighbourRight) {

        if (neighbourLeft == 1 && organism == 1 && neighbourRight == 1)
            return 0;
        if (neighbourLeft == 1 && organism == 1 && neighbourRight == 0)
            return 0;
        if (neighbourLeft == 1 && organism == 0 && neighbourRight == 1)
            return 1;
        if (neighbourLeft == 1 && organism == 0 && neighbourRight == 0)
            return 1;
        if (neighbourLeft == 0 && organism == 1 && neighbourRight == 1)
            return 0;
        if (neighbourLeft == 0 && organism == 1 && neighbourRight == 0)
            return 1;
        if (neighbourLeft == 0 && organism == 0 && neighbourRight == 1)
            return 1;
        if (neighbourLeft == 0 && organism == 0 && neighbourRight == 0)
            return 0;

        return -1;
    }

}
