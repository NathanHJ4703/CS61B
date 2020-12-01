package byow.InputDemo;

import byow.Core.Game;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

/**
 * Created by hug.
 */
public interface InputSource {
    public char getNextKey();
    public boolean possibleNextInput();
    public char getKeyWait(Game game, TETile[][] world, TERenderer ter);
}
