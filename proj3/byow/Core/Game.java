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

public class Game {
    InputSource inputSource;
    private boolean pressedQ;
    private boolean onWall;
    private boolean onFloor;
    private File worldFolder;

    public Game(KeyboardInteract inputSource) {
        this.inputSource = inputSource;
        pressedQ = false;
        onWall = false;
        onFloor = false;
        this.worldFolder = new File("byow/Core/savedWorlds");
        if (!this.worldFolder.exists()) {
            this.worldFolder.mkdir();
        }
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

    public void interact(TETile[][] world, TERenderer ter, String seed, Engine engine) {
        while (true) {
            displayHUD(world);
            char m = inputSource.getNextKey();
            if (m == 'W') {
                seed += "w";
                world = engine.interactWithInputString(seed);
            }
            if (m == 'A') {
                seed += "a";
                world = engine.interactWithInputString(seed);
            }
            if (m == 'S') {
                seed += "s";
                world = engine.interactWithInputString(seed);
            }
            if (m == 'D') {
                seed += "d";
                world = engine.interactWithInputString(seed);
            }
            if (m == ':') {
                char q = inputSource.getKeyWait(this, world, ter);
                if (q == 'Q') {
                    seed += ":q";
                    world = engine.interactWithInputString(seed);
                    pressedQ();
                    break;
                }
            }
            ter.renderFrame(world);
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

    public void saveWorld(String input) {
        File worldFile = new File(this.worldFolder, input + ".txt");
        if (!worldFile.exists()) {
            try {
                worldFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter myWriter = new FileWriter("byow/Core/savedWorlds/" + input + ".txt");
            myWriter.write(input);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String loadWorld(String input) {
        File worldFile = new File(this.worldFolder, input + ".txt");
        FileReader fr;
        String output = "";
        if (!worldFile.exists()) {
            return null;
        }
        try {
            fr = new FileReader("byow/Core/savedWorlds/" + input + ".txt");
            int i;
            while ((i = fr.read()) != -1) {
                output += String.valueOf(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public String filterQ(String seed) {
        String newString = "";
        ArrayList<String> x = new ArrayList<>(Arrays.asList(seed.split("")));
        for (int i = 0; i < x.size(); i++) {
            if (x.get(i).equals(":") || x.get(i).equals("q")) {
                continue;
            }
            newString += x.get(i);
        }
        return newString;
    }



    public static void main(String[] args) {

    }
}
