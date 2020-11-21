package byow.lab12;

import byow.TileEngine.TETile;

import java.util.LinkedList;
import java.util.List;

public class Hexagon {
    private int side;
    private Position upperLeft;
    private TETile tile;

    public Hexagon(int side, Position upperLeft, TETile tile) {
        this.side = side;
        this.upperLeft = upperLeft;
        this.tile = tile;
    }

    public TETile getTile() {
        return tile;
    }

    /**
     * Return a list of the (x,y) positions that this hexagon covers
     * @return
     */
    public List<Position> getCoordinates() {
        List<Position> list = new LinkedList<>();

        for (int i = 0; i < side; i++) {
            int x = upperLeft.getX() - i;
            int y = upperLeft.getY() - i;
            addRow(list, new Position(x, y), side + i * 2);
        }

        for (int i = 0; i < side; i++) {
            int x = upperLeft.getX() - (side - 1 - i);
            int y = upperLeft.getY() - side - i;
            addRow(list, new Position(x, y), side + (side - 1 - i) * 2);
        }

        return list;
    }

    private void addRow(List<Position> list, Position startPosition, int length) {

        for (int offset = 0; offset < length; offset++) {
            int y = startPosition.getY();
            int x = startPosition.getX() + offset;
            list.add(new Position(x, y));
        }
    }
}
