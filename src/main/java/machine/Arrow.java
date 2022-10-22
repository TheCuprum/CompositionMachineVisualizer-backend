package machine;

public class Arrow {
    private final int source;
    private final int target;
    private final int cachedHashCode;

    // usually target > source
    public Arrow(int sourceIndex, int targetIndex) {
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
}
