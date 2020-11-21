package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

    public class OurWorld {
        private static final int WIDTH = 60;
        private static final int HEIGHT = 30;
        private static final int max_width_room = WIDTH/2;
        private static final int max_height_room = HEIGHT/2;

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

            InputThing inputSeed = new InputThing(args[0]);

            Rooms room = new Rooms(new Position(inputSeed.rand.nextInt()),inputSeed.rand.nextInt(max_width_room),inputSeed.rand.nextInt(max_height_room))

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
