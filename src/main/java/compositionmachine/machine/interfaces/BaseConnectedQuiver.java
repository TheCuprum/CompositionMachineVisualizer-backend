package compositionmachine.machine.interfaces;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import compositionmachine.machine.Arrow;

public abstract class BaseConnectedQuiver<Sub extends BaseConnectedQuiver<Sub>> implements Cloneable{
    protected LinkedHashMap<Arrow, Integer> arrowState = new LinkedHashMap<>();
    private LinkedHashMap<Integer, ArrayList<Arrow>> cachedArrowBySource = new LinkedHashMap<>();
    private LinkedHashMap<Integer, ArrayList<Arrow>> cachedArrowByTarget = new LinkedHashMap<>();

    protected int maxIndex = 0;

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

    public void clearCache() {
        this.cachedArrowBySource.clear();
        this.cachedArrowByTarget.clear();
    }

    public void refreshCache() {
        this.clearCache();
        for (Arrow a : this.arrowState.keySet()) {
            this.cacheArrow(a, this.cachedArrowBySource, (arrow) -> arrow.getSourceIndex());
            this.cacheArrow(a, this.cachedArrowByTarget, (arrow) -> arrow.getTargetIndex());
        }
    }

    public int getMaxIndex() {
        return this.maxIndex;
    }

    public int getArrowState(Arrow a) {
        return this.arrowState.get(a);
    }

    public Map<Arrow, Integer> getArrowStates() {
        return this.arrowState;
    }

    public void updateArrowState(Arrow a, int state) {
        this.arrowState.put(a, state);
    }

    public ArrayList<Arrow> getArrowsBySource(int sourceIndex) {
        return (ArrayList<Arrow>) this.cachedArrowBySource.get(sourceIndex).clone();
    }

    public ArrayList<Arrow> getArrowsByTarget(int targetIndex) {
        return (ArrayList<Arrow>) this.cachedArrowByTarget.get(targetIndex).clone();
    }

    public Iterator<Arrow> getArrowIterator() {
        return this.arrowState.keySet().iterator();
    }

    public Sub typedClone() {
        Sub newObj = (Sub) this.clone();
        return newObj;
    }

    @Override
    public Object clone() {
        Sub newObj;
        try {
            newObj = (Sub)super.clone();
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
