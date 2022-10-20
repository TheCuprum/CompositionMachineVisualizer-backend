package machine;

/**
 * @author Damian Arellanes
 * {@link https://github.com/damianarellanes/compositionmachine}
 */
public interface RuleSet {

  public int delta1(int organism);
  public int delta2(int organism, int neighbourRight);
  public int delta3(int neighbourLeft, int organism);
  public int delta4(int neighbourLeft, int organism, int neighbourRight);
}
