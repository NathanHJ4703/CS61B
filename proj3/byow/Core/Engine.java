package byow.Core;

import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


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
    private File worldFolder;
    private String loaded;
    private boolean didLoad;

    public Engine() {
        this.worldFolder = new File("byow/Core/savedWorlds");
        if (!this.worldFolder.exists()) {
            this.worldFolder.mkdir();
        }
        loaded = "";
        didLoad = false;
    }

    public File getWorldFolder() {
        return worldFolder;
    }

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


        KeyboardInteract inputSource;
        inputSource = new KeyboardInteract();
        Game game = new Game(inputSource);
        TETile[][] worldGenerated;

        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();

            if (c == 'N') {
                String seed = game.generateNewWorld(game, null, ter);
                worldGenerated = interactWithInputString(seed);
                ter.initialize(WIDTH, HEIGHT, 10, 5);
                ter.renderFrame(worldGenerated);
                game.interact(worldGenerated, ter, seed, this);
            }

            if (c == 'L') {
                String loaded = loadWorld();
                if (loaded == null) {
                    break;
                }
                this.loaded = beforeQ(loaded);
                worldGenerated = interactWithInputString(this.loaded);
                ter.initialize(WIDTH, HEIGHT, 10, 5);
                ter.renderFrame(worldGenerated);
                game.interact(worldGenerated, ter, "l", this);
            }

            if (c == 'Q'|| game.isPressedQ()) {
                System.out.println("Quit worked");
                break;
            }

        }
    }

    public String beforeQ(String seed) {
        String newString = "";
        ArrayList<String> x = new ArrayList<>(Arrays.asList(seed.split("")));
        for (int i = 0; i < x.size(); i++) {
            if (x.get(i).equals(":") || x.get(i).equals("q")) {
                break;
            }
            newString += x.get(i);
        }
        return newString;
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
        String seed = input;
        String lSeed = input;
        if (input.charAt(0) == 'l') {
            //everything before :q
            seed = this.loaded;
            didLoad = true;
        }
        InputThing newWorld = new InputThing(seed);
        if (didLoad) {
            for (int i = 1; i < lSeed.length(); i++) {
                newWorld.commandsAtL.add(lSeed.charAt(i));
            }
        }

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
        Avatar avatar = new Avatar(OurWorld.addAvatar(finalWorldFrame, pTracker, newWorld.rand));
        boolean isKeyQ = false;


        if (didLoad) {
            while (newWorld.commandsAtL.size() > 0) {
                Character key = newWorld.commandsAtL.remove(0);
                if (key.equals('w')) {
                    if (finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY() + 1] == Tileset.FLOOR) {
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.FLOOR;
                        avatar.updatePosition(avatar.getPosition().getX(), avatar.getPosition().getY() + 1);
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.AVATAR;
                    }
                    seed += Character.toString('w');
                } else if (key.equals('a')) {
                    if (finalWorldFrame[avatar.getPosition().getX() - 1][avatar.getPosition().getY()] == Tileset.FLOOR) {
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.FLOOR;
                        avatar.updatePosition(avatar.getPosition().getX() - 1, avatar.getPosition().getY());
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.AVATAR;
                    }
                    seed += Character.toString('a');
                } else if (key.equals('s')) {
                    if (finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY() - 1] == Tileset.FLOOR) {
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.FLOOR;
                        avatar.updatePosition(avatar.getPosition().getX(), avatar.getPosition().getY() - 1);
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.AVATAR;
                    }
                    seed += Character.toString('s');
                } else if (key.equals('d')) {
                    if (finalWorldFrame[avatar.getPosition().getX() + 1][avatar.getPosition().getY()] == Tileset.FLOOR) {
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.FLOOR;
                        avatar.updatePosition(avatar.getPosition().getX() + 1, avatar.getPosition().getY());
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.AVATAR;
                    }
                    seed += Character.toString('d');
                } else if (key.equals(':')) {
                    Character q = newWorld.commandsAtL.remove(0);
                    if (q.equals('q')) {
                        seed += Character.toString(':');
                        seed += Character.toString('q');
                        saveWorld(seed);
                        isKeyQ = true;
                        break;
                    }
                }
            }
        } else {
            while (newWorld.commands.size() > 0) {
                Character key = newWorld.commands.remove(0);
                if (key.equals('w')) {
                    if (finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY() + 1] == Tileset.FLOOR) {
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.FLOOR;
                        avatar.updatePosition(avatar.getPosition().getX(), avatar.getPosition().getY() + 1);
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.AVATAR;
                    }
                    seed += Character.toString('w');
                } else if (key.equals('a')) {
                    if (finalWorldFrame[avatar.getPosition().getX() - 1][avatar.getPosition().getY()] == Tileset.FLOOR) {
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.FLOOR;
                        avatar.updatePosition(avatar.getPosition().getX() - 1, avatar.getPosition().getY());
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.AVATAR;
                    }
                    seed += Character.toString('a');
                } else if (key.equals('s')) {
                    if (finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY() - 1] == Tileset.FLOOR) {
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.FLOOR;
                        avatar.updatePosition(avatar.getPosition().getX(), avatar.getPosition().getY() - 1);
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.AVATAR;
                    }
                    seed += Character.toString('s');
                } else if (key.equals('d')) {
                    if (finalWorldFrame[avatar.getPosition().getX() + 1][avatar.getPosition().getY()] == Tileset.FLOOR) {
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.FLOOR;
                        avatar.updatePosition(avatar.getPosition().getX() + 1, avatar.getPosition().getY());
                        finalWorldFrame[avatar.getPosition().getX()][avatar.getPosition().getY()] = Tileset.AVATAR;
                    }
                    seed += Character.toString('d');
                } else if (key.equals(':')) {
                    Character q = newWorld.commands.remove(0);
                    if (q.equals('q')) {
                        seed += Character.toString(':');
                        seed += Character.toString('q');
                        saveWorld(seed);
                        isKeyQ = true;
                        break;
                    }
                }
            }
        }

        File worldFile = new File(this.worldFolder, "loadedWorld.txt");
        if (worldFile.exists() && !isKeyQ) {
            worldFile.delete();
        }

        return finalWorldFrame;
    }

    public String loadWorld() {
        File worldFile = new File(this.worldFolder, "loadedWorld.txt");
        FileReader fr;
        String output = "";
        if (!worldFile.exists()) {
            return null;
        }
        try {
            fr = new FileReader("byow/Core/savedWorlds/loadedWorld.txt");
            int i;
            while ((i = fr.read()) != -1) {
                char c = (char) i;
                output += Character.toString(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public void saveWorld(String input) {
        File worldFile = new File(this.worldFolder,"loadedWorld.txt");
        if (!worldFile.exists()) {
            try {
                worldFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter myWriter = new FileWriter("byow/Core/savedWorlds/loadedWorld.txt");
            myWriter.write(input);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Engine x = new Engine();
        x.interactWithKeyboard();
    }

}
