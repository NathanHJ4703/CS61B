package byow.lab12;

import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;

public class Tesselation {
    private int tesselationSide;
    private int hexSide;

    public Tesselation(int tesselationSide, int hexSide) {
        this.tesselationSide = tesselationSide;
        this.hexSide = hexSide;
    }

    public List<Hexagon> getHexagons() {
        LinkedList<Hexagon> hexagons = new LinkedList<>();

        int xSeparation = hexSide * 2 - 1;

        int xOffset = xSeparation * (tesselationSide * 2 - 2);

        for (int i = 0; i < tesselationSide; i++) {

            int x1 = i * xSeparation + hexSide;
            int x2 = x1 + xOffset;

            int y = (tesselationSide - i - 1) * hexSide + 2 * hexSide;
            int numHexagons = tesselationSide + i;

            addColumn(hexagons, new Position(x1, y), numHexagons);
            addColumn(hexagons, new Position(x2, y), numHexagons);

            xOffset -= 2 * xSeparation;
        }

        return hexagons;
    }

    private void addColumn(LinkedList<Hexagon> hexagons, Position position, int numHexagons) {
        for (int i = 0; i < numHexagons; i++) {
            int x = position.getX();
            int y = position.getY();
            hexagons.add(new Hexagon(hexSide, new Position(x,y), Tileset.AVATAR));
        }
    }
}
