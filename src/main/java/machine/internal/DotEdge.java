package machine.internal;

import org.jgrapht.graph.DefaultEdge;

public class DotEdge extends DefaultEdge {
    private int connection = 1;

    public static DotEdge create(int value){
        DotEdge edge = new DotEdge();
        edge.setConnection(value);
        return edge;
    }

    public void setConnection(int val){
        this.connection = val;
    }

    public int getConnection() {
        return this.connection;
    }
}
