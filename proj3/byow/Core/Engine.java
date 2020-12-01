package byow.Core;

import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;


/** Runs the two methods interactWithKeyBoard and interactWithInputString.
 * @author nathanpak
 */

public class Engine {
    /** A renderer object for drawing the rooms and hallways. */
    private TERenderer ter = new TERenderer();
    /** Feel free to change the width and height. */
    public static final int WIDTH = 70;
    /** The height of the window. */
    public static final int HEIGHT = 40;

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
        Font title = new Font("Arial", Font.BOLD, 50);
        StdDraw.setFont(title);
        StdDraw.text(0.5, 0.7, "Main Menu");
        Font option = new Font("Arial", Font.PLAIN, 20);
        StdDraw.setFont(option);
        StdDraw.text(0.5, 0.4, "New Game (Press N)");
        StdDraw.text(0.5, 0.3, "Load Game (Press L)");
        StdDraw.text(0.5, 0.2, "Quit (Press Q)");


        InputSource inputSource;
        inputSource = new KeyboardInteract();
        Game game = new Game(inputSource, ter);

        //while (inputSource.possibleNextInput()) {

        char c = inputSource.getNextKey();

        if (c == 'N') {
            String seed = game.generateNewWorld();
            TETile[][] worldGenerated = interactWithInputString(seed);
            ter.initialize(WIDTH, HEIGHT, 10, 5);
            ter.renderFrame(worldGenerated);
            game.interactHUD(worldGenerated);
        }
/**
            if (c == 'L') {

            }
*//**
        if (c == 'Q'|| game.isPressedQ()) {
            break;
        }*/

        //}

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


        TETile[][] finalWorldFrame = new TETile[50][25];
        for (int x = 0; x < 50; x += 1) {
            for (int y = 0; y < 25; y += 1) {
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


    public static void main(String[] args) {
        Engine x = new Engine();
        x.interactWithKeyboard();
    }

}
