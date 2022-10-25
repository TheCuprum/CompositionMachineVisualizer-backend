package compositionmachine.util;

public class Util {
    public static String leftPadString(String input, int length, char padChar){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - input.length(); i++){
            sb.append(padChar);
        }
        sb.append(input);
        return sb.toString();
    }
}
