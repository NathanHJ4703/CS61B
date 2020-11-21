package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Hallways {
    private int length;
    private Position p;
    private static final int WIDTH = 2;
    private static final Random RANDOM = new Random();

    public Hallways(Position p, int length){
        this.p = p;
        this.length = length;
    }
    public void drawVerticalHallway() {
        //static x
        int length = RANDOM.nextInt();
        TETile[][] vertHall = new TETile[WIDTH][length];
        for (int i = 0; i < length; i++) {
            vertHall[WIDTH - 2][i] = Tileset.WALL;
            vertHall[WIDTH - 1][i] = Tileset.FLOOR;
            vertHall[WIDTH][i] = Tileset.WALL;
        }

    }



    public void drawHorizontalHallway() {
        //static y

    }
    public void drawCorner() {

    }

}
