package byow.Core;

import byow.InputDemo.InputSource;
import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInteract implements InputSource {

    @Override
    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                return Character.toUpperCase(StdDraw.nextKeyTyped());
            }
        }
    }

    @Override
    public boolean possibleNextInput() {
        return true;
    }
}
