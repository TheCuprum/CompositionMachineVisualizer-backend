package compositionmachine.machine.interfaces;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import compositionmachine.machine.Arrow;

/**
 * Base class for connected quivers.
 */
public abstract class BaseConnectedQuiver<Sub extends BaseConnectedQuiver<Sub>> implements Cloneable {
    protected LinkedHashMap<Arrow, Integer> arrowState = new LinkedHashMap<>();
    private LinkedHashMap<Integer, ArrayList<Arrow>> cachedArrowBySource = new LinkedHashMap<>();
    private LinkedHashMap<Integer, ArrayList<Arrow>> cachedArrowByTarget = new LinkedHashMap<>();

    protected int maxIndex = 0;

    /**
     * Adds an arrow with initial state.
     * 
     * @param a     Arrow instance.
     * @param state Arrow's inital state.
     */
    public void addArrow(Arrow a, int state) {
        this.maxIndex = Math.max(this.maxIndex, Math.max(a.getSourceIndex(), a.getTargetIndex()));
        this.arrowState.put(a, state);
        this.cacheArrow(a, this.cachedArrowBySource, (arrow) -> arrow.getSourceIndex());
        this.cacheArrow(a, this.cachedArrowByTarget, (arrow) -> arrow.getTargetIndex());
    }

    private void cacheArrow(Arrow a, LinkedHashMap<Integer, ArrayList<Arrow>> targetCache,
            Function<Arrow, Integer> getterFunction) {
        int value = getterFunction.apply(a);
        ArrayList<Arrow> cachedArray = targetCache.get(value);
        if (cachedArray != null) {
            for (int i = 0; i < cachedArray.size(); i++) { // increase
                if (getterFunction.apply(cachedArray.get(i)) > value)
                    cachedArray.add(i, a);
            }
        } else {
            ArrayList<Arrow> newArrayList = new ArrayList<>();
            newArrayList.add(a);
            this.cachedArrowBySource.put(value, newArrayList);
        }
    }

    /**
     * Clears instance's arrow cache.
     */
    public void clearCache() {
        this.cachedArrowBySource.clear();
        this.cachedArrowByTarget.clear();
    }

    /**
     * Refreshes instance's arrow cache.
     */
    public void refreshCache() {
        this.clearCache();
        for (Arrow a : this.arrowState.keySet()) {
            this.cacheArrow(a, this.cachedArrowBySource, (arrow) -> arrow.getSourceIndex());
            this.cacheArrow(a, this.cachedArrowByTarget, (arrow) -> arrow.getTargetIndex());
        }
    }

    /**
     * Get max node index in this quiver.
     * 
     * @return Max node index。
     */
    public int getMaxIndex() {
        return this.maxIndex;
    }

    /**
     * Get the state of arrow.
     * 
     * @param a Arrow instance.
     * @return The state of arrow.
     */
    public int getArrowState(Arrow a) {
        return this.arrowState.get(a);
    }

    /**
     * Get the states of every arrows.
     * 
     * @return A map which maps each arrow to its state.
     */
    public Map<Arrow, Integer> getArrowStates() {
        return this.arrowState;
    }

    /**
     * Update an arrow's state.
     * 
     * @param a     Arrow instance.
     * @param state New state.
     */
    public void updateArrowState(Arrow a, int state) {
        this.arrowState.put(a, state);
    }

    /**
     * Gets a list of arrows which have common souce {@code sourceIndex}.
     * 
     * @param sourceIndex Arrows' source node.
     * @return A list of arrows that from this node.
     */
    public ArrayList<Arrow> getArrowsBySource(int sourceIndex) {
        return (ArrayList<Arrow>) this.cachedArrowBySource.get(sourceIndex).clone();
    }

    /**
     * Gets a list of arrows which have common target {@code targetIndex}.
     * 
     * @param targetIndex Arrows' target node.
     * @return A list of arrows that target to this node.
     */
    public ArrayList<Arrow> getArrowsByTarget(int targetIndex) {
        return (ArrayList<Arrow>) this.cachedArrowByTarget.get(targetIndex).clone();
    }

    /**
     * Gets the iterator to iterate every arrow in this connected quiver.
     * @return The iterator instance.
     */
    public Iterator<Arrow> getArrowIterator() {
        return this.arrowState.keySet().iterator();
    }

    /**
     * Clone the connected quiver with correct return type and without rebuilding the cache.
     * @return A clone of connected quiver instance.
     */
    public Sub typedClone() {
        Sub newObj = (Sub) this.clone();
        return newObj;
    }

    @Override
    public Object clone() {
        Sub newObj;
        try {
            newObj = (Sub) super.clone();
            LinkedHashMap<Arrow, Integer> clonedArrowState = new LinkedHashMap<>();
            for (Entry<Arrow, Integer> pair : this.arrowState.entrySet()) {
                clonedArrowState.put(pair.getKey(), pair.getValue().intValue());
            }
            newObj.arrowState = clonedArrowState;
            newObj.maxIndex = this.maxIndex;
            return newObj;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseConnectedQuiver) {
            BaseConnectedQuiver<?> bcq = (BaseConnectedQuiver<?>) obj;
            return this.arrowState.equals(bcq.arrowState);
        } else {
            return false;
        }
    }
}
