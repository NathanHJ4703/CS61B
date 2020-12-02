package byow.Core;

import byow.InputDemo.InputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.ArrayList;

public class Game {
    InputSource inputSource;
    private boolean pressedQ;
    private boolean onWall;
    private boolean onFloor;

    public Game(KeyboardInteract inputSource) {
        this.inputSource = inputSource;
        pressedQ = false;
        onWall = false;
        onFloor = false;
    }

    public boolean isPressedQ() {
        return pressedQ;
    }

    public void pressedQ() {
        pressedQ = true;
    }

    public String generateNewWorld(Game game, TETile[][] world, TERenderer ter) {
        String seed = "";
        StdDraw.clear();
        StdDraw.text(0.5, 0.6, "Enter a seed");
        while (true) {
            char digit = inputSource.getKeyWait(game, world, ter);
            if (digit == 'S') {
                break;
            }
            System.out.println(digit);
            String s = String.valueOf(digit);
            seed += s;
        }
        seed = "N" + seed + "S";

        return seed;
    }

    public void interactHUD(TETile[][] world, TERenderer ter) {
        while (true) {
            displayHUD(world);
            char c = inputSource.getNextKey();
            System.out.println(c);
            if (c == 'W') {

            }
            if (c == 'A') {

            }
            if (c == 'S') {

            }
            if (c == 'D') {

            }
            ter.renderFrame(world);


            if (c == 'Q') {
                pressedQ = true;
                break;
            }
        }
    }

    public void displayHUD(TETile[][] world) {
        Font hud = new Font("Arial", Font.PLAIN, 16);
        StdDraw.setFont(hud);
        StdDraw.setPenColor(Color.WHITE);
        if (isHoverWall(world)) {
            //System.out.println("Hovering over wall now!");
            //onWall = true;
            //onFloor = false;
            StdDraw.text(35, 35, "Wall");
            StdDraw.show();
        } else if (isHoverFloor(world)) {
            //System.out.println("Hovering over floor now!");
            //onFloor = true;
            //onWall = false;
            StdDraw.text( 35,  35, "Floor");
            StdDraw.show();
        }
    }

    private boolean isHoverWall(TETile[][] world) {

        double x = StdDraw.mouseX();
        int xPos = (int) x;
        double y = StdDraw.mouseY();
        int yPos = (int) y;
        if (xPos < 10 || xPos >= 60) {
            return false;
        }
        if (yPos < 5 || yPos >= 30) {
            return false;
        }
        return world[xPos - 10][yPos - 5] == Tileset.WALL;
    }

    private boolean isHoverFloor(TETile[][] world) {

        double x = StdDraw.mouseX();
        int xPos = (int) x;
        //System.out.println("X Value: " + x);
        double y = StdDraw.mouseY();
        int yPos = (int) y;
        //System.out.println("Y Value: " + y);
        if (xPos < 10 || xPos >= 60) {
            return false;
        }
        if (yPos < 5 || yPos >= 30) {
            return false;
        }
        return world[xPos - 10][yPos - 5] == Tileset.FLOOR;
    }

    public static void main(String[] args) {
        ArrayList<Integer> x = new ArrayList<>();
        x.add(3);
        x.add(4);
        x.add(5);
        System.out.println(x.size());
        System.out.println(x.remove(0));
        System.out.println(x.remove(0));
        System.out.println(x.remove(0));
        System.out.println(x.size());
    }
}