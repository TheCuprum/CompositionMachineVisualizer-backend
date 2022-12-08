package compositionmachine.util;

import java.util.Set;

import compositionmachine.machine.Arrow;
import compositionmachine.machine.Quiver;
import compositionmachine.machine.interfaces.BaseConnectedQuiver;

public class LogUtil {
    public static <CQ extends BaseConnectedQuiver<CQ>> void printQuiverContent(Quiver<CQ> q){
        for (BaseConnectedQuiver<CQ> baseConnectedQuiver : q) {
            Set<Arrow> arrows = baseConnectedQuiver.getArrowStates().keySet();
            for (Arrow arr : arrows) {
                System.out.println(arr);
            }
            System.out.println();
        }
    }
}
