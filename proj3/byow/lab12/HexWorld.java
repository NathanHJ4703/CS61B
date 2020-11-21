package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        Tesselation tesselation = new Tesselation(4, 2);

        // fills in a block 14 tiles wide by 4 tiles tall

        for (Hexagon hexagon: tesselation.getHexagons()) {
            addHexagon(hexagon, world);
        }

        // draws the world to the screen
        ter.renderFrame(world);
    }

    private static void addHexagon(Hexagon hexagon, TETile[][] world) {

        for (Position p: hexagon.getCoordinates()) {
            world[p.getX()][p.getY()] = hexagon.getTile();
        }
    }
}
