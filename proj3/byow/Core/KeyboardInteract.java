package byow.Core;

import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInteract implements InputSource {

    @Override
    public char getNextKey() {

        if (StdDraw.hasNextKeyTyped()) {
            return Character.toUpperCase(StdDraw.nextKeyTyped());
        }
        return 0;
    }

    public char getKeyWait(Game game, TETile[][] worldGenerated, TERenderer ter) {
        while (true) {
            if (worldGenerated != null) {
                game.displayHUD(worldGenerated);
                ter.renderFrame(worldGenerated);
            }
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
