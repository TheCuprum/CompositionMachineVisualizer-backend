package compositionmachine.machine;

import com.google.common.collect.HashBasedTable;

/**
 * Represents the organisms (or edges) in quivers.
 */
public class Arrow {
    private static final HashBasedTable<Integer, Integer, Arrow> arrowCache = HashBasedTable.create();

    private final int source;
    private final int target;
    private final int cachedHashCode;

    /**
     * Create new arrow instance by its source and target index.
     * 
     * @param sourceIndex Arrow's source node index.
     * @param targetIndex Arrow's target node index.
     */
    private Arrow(int sourceIndex, int targetIndex) {
        // usually target > source
        this.source = sourceIndex;
        this.target = targetIndex;
        StringBuilder builder = new StringBuilder();
        builder.append(this.source).append(',').append(this.target);
        this.cachedHashCode = builder.toString().hashCode();
    }

    /**
     * Gets arrow's source node index.
     * 
     * @return Arrow's source node index.
     */
    public int getSourceIndex() {
        return this.source;
    }

    /**
     * Gets arrow's target node index.
     * 
     * @return Arrow's target node index.
     */
    public int getTargetIndex() {
        return this.target;
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Arrow) {
            Arrow a = (Arrow) obj;
            return a.source == this.source && a.target == this.target;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.source).append(',').append(this.target);
        return builder.toString();
    }

    /**
     * Create arrow or reuse existing one in cache according to its source and
     * target index, to avoid duplicated small objects.
     * 
     * @param sourceIndex Arrow's source node index.
     * @param targetIndex Arrow's target node index.
     * @return Created arrow.
     */
    public static Arrow create(int sourceIndex, int targetIndex) {
        Arrow a = arrowCache.get(sourceIndex, targetIndex);
        if (a == null) {
            a = new Arrow(sourceIndex, targetIndex);
            arrowCache.put(sourceIndex, targetIndex, a);
            return a;
        } else {
            return a;
        }
    }
}
