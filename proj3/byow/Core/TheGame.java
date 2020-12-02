package byow.Core;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;

/*@source Itai Smith BYOW persistence videos */

public class TheGame implements Serializable {
    private static GameMap gameMap;
    private File SavedGames;
    private File mapFile;

    public TheGame() {
        this.mapFile = Paths.get("byow", "SavedGames", "mapFile.txt").toFile();
        if (this.mapFile.exists()) {
            this.gameMap = IOUtils.readObject(mapFile, GameMap.class);
        } else {
            this.gameMap = new GameMap();
            try {
                this.mapFile.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //load game map
        TheGame game = new TheGame();
        game.runGame();
        //want to write serializable object ( game.gameMap) into game.mapfile
        IOUtils.writeObject(game.mapFile, (Serializable) game.gameMap);
    }

    public static void saveGame(String seed, Game game) {
        gameMap.addGame(seed, game);

    }
    private Game loadGame(String name) {
        return gameMap.getGame(name);
    }

    private Game runGame() {
        return null;
    }
}
