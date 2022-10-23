package machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import machine.internal.Arrow;
import machine.internal.NotImplementedError;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class ConnectedQuiver extends BaseConnectedQuiver<ConnectedQuiver> /* extends ArrayList<Integer> */ {

    private ArrayList<Integer> arrowStateOverride = new ArrayList<>();
    private ArrayList<ArrayList<Arrow>> arrowCache = new ArrayList<>();

    public void addArrow(int state) {
        this.arrowStateOverride.add(state);
        ArrayList<Arrow> cache = new ArrayList<>();
        cache.add(Arrow.create(this.maxIndex, this.maxIndex + 1));
        this.arrowCache.add(cache);
        this.maxIndex++; // == arraylist.size()
    }

    @Override
    public void addArrow(Arrow a, int state) {
        throw new NotImplementedError();
    }

    @Override
    public void clearCache() {
    }

    @Override
    public void refreshCache() {
    }

    @Override
    public int getArrowState(Arrow a) {
        return this.arrowStateOverride.get(a.getSourceIndex());
    }

    @Override
    public void updateArrowState(Arrow a, int state) {
        this.arrowStateOverride.set(a.getSourceIndex(), state);
    }

    @Override
    public HashMap<Arrow, Integer> getArrowStates() {
        HashMap<Arrow, Integer> map = new HashMap<>();
        for (int index = 0; index < this.arrowStateOverride.size(); index++) {
            map.put(Arrow.create(index, index + 1), this.arrowStateOverride.get(index));
        }
        return map;
    }

    @Override
    public ArrayList<Arrow> getArrowsBySource(int sourceIndex) {
        if (sourceIndex >= 0 && sourceIndex < this.maxIndex)
            return this.arrowCache.get(sourceIndex);
        else
            return null;
    }

    @Override
    public ArrayList<Arrow> getArrowsByTarget(int targetIndex) {
        if (targetIndex > 0 && targetIndex <= this.maxIndex)
            return this.arrowCache.get(targetIndex - 1);
        else
            return null;
    }

    @Override
    public Iterator<Arrow> getArrowIterator() {
        final Iterator<ArrayList<Arrow>> cacheIterator = this.arrowCache.iterator();
        return new Iterator<Arrow>() {
            @Override
            public boolean hasNext() {
                return cacheIterator.hasNext();
            }

            @Override
            public Arrow next() {
                ArrayList<Arrow> nextCache = cacheIterator.next();
                if (nextCache != null)
                    return nextCache.get(0);
                else
                    return null;
            }
        };
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        Iterator<Integer> i = this.arrowStateOverride.iterator();
        while (i.hasNext()) {
            if (i.next().intValue() == 1)
                sb.append("\u25A0");
            else
                sb.append("\u25A1");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConnectedQuiver) {
            ConnectedQuiver cq = (ConnectedQuiver) obj;
            return this.arrowStateOverride.equals(cq.arrowStateOverride);
        } else {
            return false;
        }
    }

    @Override
    public Object clone() {
        ConnectedQuiver newObj = (ConnectedQuiver) super.clone();
        newObj.arrowStateOverride = (ArrayList<Integer>) this.arrowStateOverride.clone();
        newObj.arrowCache = this.arrowCache;
        return newObj;
    }
}
