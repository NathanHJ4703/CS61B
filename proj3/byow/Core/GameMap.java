package byow.Core;

import byow.TileEngine.TETile;

import java.util.HashMap;
import java.util.Map;

/*@source Itai Smith BYOW persistence videos */

public class GameMap {

    private static Map<String, Game> gameMap;

    public GameMap() {
        this.gameMap = new HashMap<>();
    }

    public static void addGame(String seed, Game game) {
        gameMap.put(seed, game);
    }


    public Game getGame(String seed) {
        return this.gameMap.get(seed);
    }
}
