package compositionmachine.machine.internal;

import com.google.common.collect.HashBasedTable;

public class DotNode {
    private static final HashBasedTable<Integer, Integer, DotNode> nodeCache = HashBasedTable.create();
    
    private final int value;
    private final int group;

    private final int hash;
    private final String hashString;

    private DotNode(int val, int g){
        this.value = val;
        this.group = g;

        StringBuilder sb = new StringBuilder();
        sb.append(this.value).append(',').append(this.group);
        this.hashString = sb.toString();
        this.hash = this.hashString.hashCode();
    }

    public static DotNode create(int value, int group){
        DotNode node = nodeCache.get(value, group);
        if (node == null){
            node = new DotNode(value, group);
            nodeCache.put(value, group, node);
            return node;
        }else{
            return node;
        }
    }

    public int getValue(){
        return this.value;
    }

    public int getGroup(){
        return this.group;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DotNode) {
            DotNode node = (DotNode) obj;
            return node.value == this.value && node.group == this.group;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return hashString;
    }
    
    @Override
    public int hashCode() {
        return this.hash;
    }
}
