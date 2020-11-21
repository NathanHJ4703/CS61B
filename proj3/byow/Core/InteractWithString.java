package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class InteractWithString {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;


    public static void main(String[] args) {
        // Initialize the tile rendering engine with window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // Initializes tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        InputThing inputSeed = new InputThing(args[0]);


        //draws the world to the screen
        ter.renderFrame(world);
    }

    private static void addRoom(Room room, TETile[][] world) {

    }

}
