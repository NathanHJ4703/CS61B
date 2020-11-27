package byow.Core;

import java.util.*;

public class RoomTracker {
    private Set<Room> distinctRooms;
    private LinkedList<Room> listOfRooms;
    private LinkedList<Room> isolatedRooms;
    private UnionFind roomsToConnect;
    private Map<Integer, Room> numberToRoom;

    public RoomTracker() {
        distinctRooms = new HashSet<>();
        listOfRooms = new LinkedList<>();
        isolatedRooms = new LinkedList<>();
        numberToRoom = new HashMap<>();
    }

    public void addRoom(Room room) {
        distinctRooms.add(room);
        listOfRooms.add(room);
        isolatedRooms.add(room);
    }

    public void giveID(int id, Room r) {
        numberToRoom.put(id, r);
    }

    public LinkedList<Room> getIsolatedRooms() {
        return isolatedRooms;
    }

    public Map<Integer, Room> getNumberToRoom() {
        return numberToRoom;
    }

    public void constructUnionFind(int k) {
        roomsToConnect = new UnionFind(k);
    }

    public LinkedList<Room> getListOfRooms() {
        return listOfRooms;
    }

    public Set<Room> getDistinctRooms() {
        return distinctRooms;
    }

    public void connectRooms(int v1, int v2) {
        roomsToConnect.connect(v1, v2);
    }

    public UnionFind getRoomsToConnect() {
        return roomsToConnect;
    }
}
