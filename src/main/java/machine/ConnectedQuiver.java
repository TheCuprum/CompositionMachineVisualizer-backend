package machine;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Damian Arellanes
 * {@link https://github.com/damianarellanes/compositionmachine}
 */
public class ConnectedQuiver extends ArrayList<Integer>{

  @Override
  public String toString() {
    
    StringBuilder sb = new StringBuilder();
    Iterator i = this.iterator();
    while(i.hasNext()) {      
      if((int)i.next() == 1) sb.append("\u25A0");
      else sb.append("\u25A1");
    }
    
    return sb.toString();
  }
}
