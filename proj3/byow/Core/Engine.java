package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


/** Runs the two methods interactWithKeyBoard and interactWithInputString.
 * @author nathanpak
 */
public class Engine {
    /** A renderer object for drawing the rooms and hallways. */
    private TERenderer ter = new TERenderer();
    /** Feel free to change the width and height. */
    public static final int WIDTH = 60;
    /** The height of the window. */
    public static final int HEIGHT = 30;

    /** Gets the TERenderer.
     * @return The TERenderer */
    public TERenderer getTer() {
        return ter;
    }

    /**
     * Method used for exploring a fresh world. This method should handle all
     * inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code.
     * The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas",
     * "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters
     * into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game
     * to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect
     * the game to run the first
     * 7 commands (n123sss) and then quit and save. If we
     * then do
     * interactWithInputString("l"), we should be back in the
     * exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state
     * of the world
     */
    public TETile[][] interactWithInputString(String input) {

        InputThing newWorld = new InputThing(input);


        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }

        RoomTracker rTracker = new RoomTracker();
        PositionTracker pTracker = new PositionTracker();
        OpenCoordTracker oTracker = new OpenCoordTracker();

        int i = newWorld.rand.nextInt(100);

        OurWorld.generateRooms(i + 10, newWorld.rand, finalWorldFrame,
                rTracker, pTracker);
        int k = rTracker.getIsolatedRooms().size();
        rTracker.constructUnionFind(k);

        OurWorld.addOpenings(rTracker.getListOfRooms(),
                finalWorldFrame, newWorld.rand, oTracker, pTracker);
        OurWorld.generateHallways(finalWorldFrame, newWorld.rand,
                oTracker, pTracker, rTracker);
        OurWorld.connectRooms(newWorld.rand, finalWorldFrame,
                rTracker, pTracker, oTracker);


        return finalWorldFrame;
    }

}
