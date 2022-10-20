package examples;

import machine.ConnectedQuiver;
import machine.CompositionMachine;
import machine.Quiver;

/**
 * @author Damian Arellanes
 * {@link https://github.com/damianarellanes/compositionmachine}
 */
public class Examples {
  
  public static Quiver getInitialConfiguration() {
    
    ConnectedQuiver q1 = new ConnectedQuiver();
    q1.add(1);q1.add(1);q1.add(0);q1.add(1);q1.add(1);q1.add(0);    
    
    ConnectedQuiver q2 = new ConnectedQuiver();
    q2.add(1);    
    
    ConnectedQuiver q3 = new ConnectedQuiver();
    q3.add(0);q3.add(0);q3.add(1);
    
    Quiver q = new Quiver();
    q.add(q1);
    q.add(q2);
    q.add(q3);
    
    return q;
  }
  
  public static void main(String[] args) {
    
    // Elementary cellular automata [rule (elements in the repeating pattern)]: 
    // 122 (6), 110 (5), 102, (8), 62 (6), 60 (5)
    
    CompositionMachine m = new CompositionMachine(
      getInitialConfiguration(), new ExampleRules1()
    );
//    Machine m = new CompositionMachine(
//      getInitialConfiguration(), new MyRules2()
//    ); 
    
    m.execute(9999);
  }
}
