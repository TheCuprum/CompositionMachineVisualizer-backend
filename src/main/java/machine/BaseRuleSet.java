package machine;

import machine.internal.Arrow;

// all rule sets should have a parameterless constructor
public interface BaseRuleSet<CQ extends BaseConnectedQuiver<CQ>> {
    public int apply(CQ connectedQuiver, Arrow arrow);
}
