package compositionmachine.machine.interfaces;

import compositionmachine.machine.Quiver;

/**
 * Base interface to generate initial quiver.
 * It is suggeted that all implemented classes have a parameterless constructor.
 */
public interface QuiverInitializer<CQ extends BaseConnectedQuiver<CQ>> {
    public Quiver<CQ> generateQuiver();

    /**
     * Iterates the initializer one step.
     * @return Whether the iteration is successful.
     */
    public boolean iterate();
}
