package compositionmachine.machine;

// all quiver initializer should have a parameterless constructor
public interface QuiverInitializer<CQ extends BaseConnectedQuiver<CQ>> {
    public Quiver<CQ> generateQuiver();
}
