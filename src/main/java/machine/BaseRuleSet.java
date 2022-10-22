package machine;

public interface BaseRuleSet<Q extends BaseConnectedQuiver> {
    public int apply(Q connectedQuiver, Arrow arrow);
}
