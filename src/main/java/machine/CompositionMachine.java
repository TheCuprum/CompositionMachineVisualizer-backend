package machine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Damian Arellanes
 * {@link https://github.com/damianarellanes/compositionmachine}
 */
public class CompositionMachine {
    
    private final RuleSet rules;    
    private final Map<Integer, Quiver> quiverHistory;
    
    public CompositionMachine(Quiver q, RuleSet rules) {      
      this.rules = rules;      
      this.quiverHistory = new HashMap();
      quiverHistory.put(0, q);
    }
    
    public void execute(int untilTime) {
      
      System.out.println(quiverHistory.get(0) + "  t=0");
      
      for(int i = 1; i <= untilTime; i++) {
        
        Quiver nextQuiver = nextGlobalState(quiverHistory.get(i-1));
        System.out.println(nextQuiver + "  t=" + i);
        quiverHistory.put(i, nextQuiver);
        
        if(halts(nextQuiver,quiverHistory.get(i-1))) {
          System.out.println(quiverHistory.get(i-1));
          System.out.println(nextQuiver);
          System.out.println("HALTS AT TIME " + i + "!");
          System.exit(0);
        }
      }
    }
    
    public boolean halts(Quiver q) {
      
      int compositeCounter = 0;
      int bitCounterCounter = 0;
      
      for(ConnectedQuiver cq : q) {
        
        if(cq.get(0) == 1) bitCounterCounter++;
        
        for(int i = 1; i < cq.size(); i++) {
          
          if(cq.get(i) == 1) bitCounterCounter++;
          
          if(cq.get(i-1) == 1 && cq.get(i) == 1) {            
            compositeCounter ++;            
            if(compositeCounter > 1) return false;
          }
        }
      }
      
      return bitCounterCounter == 2 && compositeCounter==1;
    }
    
    public boolean halts(Quiver current, Quiver previous) {
      //return current.equals(previous);
      return current.equals(quiverHistory.get(0));
    }

    private Quiver nextGlobalState(Quiver currentQuiver) {
      
      Quiver newQuiver = new Quiver();
      
      for(ConnectedQuiver cq : currentQuiver) {
        
        ConnectedQuiver ncq = new ConnectedQuiver();
        
        for(int i = 0; i < cq.size(); i++) {
         
          if(isInN1(cq)) ncq.add(rules.delta1(cq.get(i)));
          else if(isInN2(i, cq)) ncq.add(rules.delta2(cq.get(i), cq.get(i+1)));
          else if(isInN3(i, cq)) ncq.add(rules.delta3(cq.get(i-1), cq.get(i)));
          else if(isInN4(i, cq)) ncq.add(rules.delta4(cq.get(i-1), cq.get(i), cq.get(i+1)));
        }
        
        newQuiver.add(ncq);
      }
      
      return newQuiver;
    }
    
    private boolean isInN1(ConnectedQuiver cq) {      
      return cq.size() == 1;
    }
    
    private boolean isInN2(int index, ConnectedQuiver cq) { 
      return cq.size() > 1 && index == 0;
    }
    
    private boolean isInN3(int index, ConnectedQuiver cq) { 
      return cq.size() > 1 && index == cq.size()-1;
    }
    
    private boolean isInN4(int index, ConnectedQuiver cq) { 
      return cq.size() > 1 && index != 0 && index != cq.size()-1;
    }
}
