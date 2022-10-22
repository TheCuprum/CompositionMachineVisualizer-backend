package machine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Damian Arellanes
 *         {@link https://github.com/damianarellanes/compositionmachine}
 */
public class ConnectedQuiver extends BaseConnectedQuiver /* extends ArrayList<Integer> */ {

    private ArrayList<Integer> arrowStateOverride = new ArrayList<>();
    private ArrayList<ArrayList<Arrow>> arrowCache = new ArrayList<>();

    public void addArrow(int state) {
        this.arrowStateOverride.add(state);
        ArrayList<Arrow> cache = new ArrayList<>();
        cache.add(new Arrow(this.maxIndex, this.maxIndex + 1));
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
    public ArrayList<Arrow> getArrowsBySource(int sourceIndex) {
        return this.arrowCache.get(sourceIndex);
    }

    @Override
    public ArrayList<Arrow> getArrowsByTarget(int targetIndex) {
        return this.arrowCache.get(targetIndex - 1);
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
                    return cacheIterator.next().get(0);
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
}
