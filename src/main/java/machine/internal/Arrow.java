package machine.internal;

import com.google.common.collect.HashBasedTable;

public class Arrow {
    private static final HashBasedTable<Integer, Integer, Arrow> arrowCache = HashBasedTable.create();

    private final int source;
    private final int target;
    private final int cachedHashCode;

    // usually target > source
    private Arrow(int sourceIndex, int targetIndex) {
        this.source = sourceIndex;
        this.target = targetIndex;
        StringBuilder builder = new StringBuilder();
        builder.append(this.source).append(',').append(this.target);
        this.cachedHashCode = builder.toString().hashCode();
    }

    public int getSourceIndex() {
        return this.source;
    }

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

    public static Arrow create(int sourceIndex, int targetIndex){
        Arrow a = arrowCache.get(sourceIndex, targetIndex);
        if (a == null){
            a = new Arrow(sourceIndex, targetIndex);
            arrowCache.put(sourceIndex, targetIndex, a);
            return a;
        }else{
            return a;
        }
    }
}
