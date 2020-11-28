package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import org.junit.Test;

import static org.junit.Assert.*;

public class EngineTest {

    @Test
    public void interactWithInputStringTest() {
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        Engine x = new Engine();
        TETile[][] finalWorldFrame =
                x.interactWithInputString("n5197880843569031643s");
        ter.renderFrame(finalWorldFrame);
    }
}
