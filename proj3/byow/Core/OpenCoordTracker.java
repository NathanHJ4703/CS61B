package byow.Core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OpenCoordTracker {
    private List<Position> initialOpenCoordinates;
    private Map<Position, Room> openToRoom;
    private List<Position> openCoordinates;

    public OpenCoordTracker() {
        initialOpenCoordinates = new LinkedList<>();
        openToRoom = new HashMap<>();
        openCoordinates = new LinkedList<>();
    }

    public void addInitialOpenCoordinates(Position p) {
        initialOpenCoordinates.add(p);
    }

    public void addOpenCoordinates(Position p) {
        openCoordinates.add(p);
    }

    public void addOpenToRoom(Position p, Room r) {
        openToRoom.put(p, r);
    }

    public List<Position> getInitialOpenCoordinates() {
        return initialOpenCoordinates;
    }

    public Map<Position, Room> getOpenToRoom() {
        return openToRoom;
    }

    public List<Position> getOpenCoordinates() {
        return openCoordinates;
    }

    public Position removeInitialOpenCoordinates(int position) {
        return initialOpenCoordinates.remove(position);
    }

    public void removeOpenCoordinates(Position p) {
        openCoordinates.remove(p);
    }

    public void removeOpenToRoom(Position p) {

    }

}
