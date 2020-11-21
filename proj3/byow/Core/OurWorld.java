package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

    public class OurWorld {
        private static final int WIDTH = 60;
        private static final int HEIGHT = 30;

        public OurWorld() {

        }

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

            Rooms room = new Rooms()

            InputThing inputSeed = new InputThing(args[0]);


            //draws the world to the screen
            ter.renderFrame(world);

        }

        private void addRoom(Rooms room, TETile[][] world) {
            for (Position p : room.getCoordinates()) {
                world[p.getX()][p.getY()] = p.getTile(); //???? what does that do??
            }
        }
    }

//tasks: create rooms, create hallways,
// learn how to join then, add randomness
// deal with overlapping
//interact w input string
