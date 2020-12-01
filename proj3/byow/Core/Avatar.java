package byow.Core;

public class Avatar {
    private Position position;

    public Avatar(Position position) {
        this.position = position;
    }


    public Position getPosition() {
        return position;
    }

    public void updatePosition(int x, int y) {
        position = new Position(x, y);
    }

}
