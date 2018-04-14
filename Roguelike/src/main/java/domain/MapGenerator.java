/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author konstakallama
 */
public class MapGenerator {

    Random r = new Random();
    Formulas f = new Formulas();

    public Map createTestMap(int w, int h, int floor) {
        Terrain[][] t = createTestTerrain(w, h);
        Map m = new Map(w, h, t, floor);
        this.addItem(m, "potion");
        this.addItem(m, "atma weapon");
        this.addItem(m, "über armor");
        this.addStairs(m);

        return m;
    }

    public void addItem(Map m, String name) {
        Location l = f.createRandomFreeLocation(m);

        MapItem item = new MapItem(l.getX(), l.getY(), m, name, true);

        m.addItem(l.getX(), l.getY(), item);
    }

    public void addStairs(Map m) {
        Location l = f.createRandomFreeLocation(m);
        m.setTerrain(l.getX(), l.getY(), Terrain.STAIRS);
    }

    public Terrain[][] createSimpleTestTerrain(int w, int h) {
        Terrain[][] t = new Terrain[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (r.nextDouble() < 0.2) {
                    t[i][j] = Terrain.WALL;
                } else {
                    t[i][j] = Terrain.FLOOR;
                }

            }
        }
//        for (int i = 2; i < 50; i++) {
//            for (int j = 2; j < 30; j++) {
//                if (!(i ==20 && j == 20)) {
//                    t[i][j] = Terrain.FLOOR;
//                }
//
//            }
//        }
        return t;
    }

    public Terrain[][] createTestTerrain(int w, int h) {
        Terrain[][] t = new Terrain[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                t[i][j] = Terrain.WALL;
            }
        }

        ArrayList<Room> rooms = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Room r = this.createTestRoomIterator(t);
            if (r != null) {
                rooms.add(r);
            }
        }

        createCorridors(rooms);

        connectRooms(rooms);

        paintCorridors(t, rooms);

        return t;
    }

    private Room createTestRoomIterator(Terrain[][] t) {
        int i = 0;
        while (i < 10000) {
            Room r = this.createTestRoom(t);
            if (r != null) {
                return r;
            }
            i++;
        }
        return null;
    }

    private Room createTestRoom(Terrain[][] t) {
        int w = 3 + r.nextInt(t.length / 5);
        int h = 3 + r.nextInt(t[0].length / 5);
        Location l = this.findRoomLocation(t, w, h);

        if (this.isValidRoomLocation(t, l.getX(), l.getY(), w, h)) {
            this.paintRoom(t, l, w, h);
            return new Room(l, w, h);
        }

        return null;
    }

    private Location findRoomLocation(Terrain[][] t, int w, int h) {
        int k = 0;
        int x = 0;
        int y = 0;

        while (k < 10000) {
            x = r.nextInt(t.length - w);
            y = r.nextInt(t[0].length - h);

            if (this.isValidRoomLocation(t, x, y, w, h)) {
                break;
            }

            k++;
        }
        return new Location(x, y);
    }

    private boolean isValidRoomLocation(Terrain[][] t, int x, int y, int w, int h) {
        for (int i = Math.max(0, x - 2); i < Math.min(t.length, x + w + 2); i++) {
            for (int j = Math.max(0, y - 2); j < Math.min(t[0].length, y + h + 2); j++) {
                if (t[i][j] != Terrain.WALL) {
                    return false;
                }
            }
        }
        return true;
    }

    private void paintRoom(Terrain[][] t, Location l, int w, int h) {
        for (int i = l.getX(); i < l.getX() + w; i++) {
            for (int j = l.getY(); j < l.getY() + h; j++) {
                t[i][j] = Terrain.FLOOR;
            }
        }
    }

    private void createCorridors(ArrayList<Room> rooms) {
        for (Room room : rooms) {
            for (int i = 0; i < 1; i++) {
                int index = r.nextInt(rooms.size());
                if (!room.isDirectlyConnected(rooms.get(index))) {
                    room.addCoridor(new Corridor(room, rooms.get(index)));
                }

            }
        }
    }

    private void connectRooms(ArrayList<Room> rooms) {
        Room baseRoom = rooms.get(0);

        ArrayList<Room> roomsCopy = (ArrayList<Room>) rooms.clone();

        for (Room room : rooms) {
            if (!isConnected(baseRoom, room, rooms)) {
                Collections.shuffle(roomsCopy);
                for (Room targetRoom : roomsCopy) {
                    if (isConnected(baseRoom, targetRoom, rooms)) {
                        room.addCoridor(new Corridor(room, targetRoom));
                    }
                }
            }
        }
    }

    private boolean isConnected(Room r1, Room r2, ArrayList<Room> rooms) {
        if (r1.equals(r2)) {
            return true;
        }

        return bfs(r1, r2, rooms);
    }

    private boolean bfs(Room r1, Room r2, ArrayList<Room> rooms) {
        boolean[] visited = new boolean[rooms.size()];
        ArrayDeque<Room> q = new ArrayDeque<>();
        q.add(r1);

        while (!q.isEmpty()) {
            Room room = q.poll();

            if (room.equals(r2)) {
                return true;
            }

            int index = rooms.indexOf(room);

            if (!visited[index]) {
                visited[index] = true;
                for (Room r3 : room.getConnected()) {
                    if (!visited[rooms.indexOf(r3)]) {
                        q.add(r3);
                    }
                }
            }
        }

        return false;
    }

    private void paintCorridors(Terrain[][] t, ArrayList<Room> rooms) {
        for (Room room : rooms) {
            for (Corridor c : room.getCorridors()) {
                paintCorridor(t, c);
            }
        }
    }

    private void paintCorridor(Terrain[][] t, Corridor c) {
        Room from = c.getFrom();
        Room to = c.getTo();

        Direction d = getClosestDirForRooms(from, to);

        Location start = getCorridorStart(from, d, t);

        this.paintToDir(d, start, to, t);
    }

    private Direction getClosestDirForRooms(Room from, Room to) {
        int fromX = from.getMiddle().getX();
        int fromY = from.getMiddle().getY();

        int toX = to.getMiddle().getX();
        int toY = to.getMiddle().getY();

        return this.getClosestDir(fromX, toX, fromY, toY);
    }

    private Direction getClosestDir(int fromX, int toX, int fromY, int toY) {
        if (Math.abs(fromX - toX) >= Math.abs(fromY - toY)) {
            if (fromX - toX < 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (fromY - toY < 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }
    }

    private Location getCorridorStart(Room from, Direction d, Terrain[][] t) {
        Location l = from.getMiddle();
        while (t[l.getX()][l.getY()] == Terrain.FLOOR) {
            if (l.getX() + d.xVal() >= 0 && l.getX() + d.xVal() < 50 && l.getY() + d.yVal() >= 0 && l.getY() + d.yVal() < 50) {
                l.move(d);
            } else {
                break;
            }          
        }
        return getValidStart(l, d, t, from);
    }

    private Location getValidStart(Location l, Direction d, Terrain[][] t, Room from) {
        if (t[l.getX()][l.getY()] == Terrain.WALL) {
            return l;
        }
        Location startL = new Location(l.getX(), l.getY());

        if (d == Direction.DOWN || d == Direction.UP) {
            return this.getValidHorizontal(l, t, from, startL);
        } else {
            return this.getValidVertical(l, t, from, startL);
        }
    }

    private Location getValidHorizontal(Location l, Terrain[][] t, Room from, Location startL) {
        while (l.getX() < from.getLocation().getX() + from.getW()) {
            l.move(Direction.RIGHT);
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                return l;
            }
        }
        l = startL;
        while (l.getX() > from.getLocation().getX()) {
            l.move(Direction.LEFT);
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                return l;
            }
        }
        return startL;
    }

    private Location getValidVertical(Location l, Terrain[][] t, Room from, Location startL) {
        while (l.getY() < from.getLocation().getY() + from.getH()) {
            l.move(Direction.DOWN);
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                return l;
            }
        }
        l = startL;
        while (l.getX() > from.getLocation().getX()) {
            l.move(Direction.LEFT);
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                return l;
            }
        }
        return startL;
    }

    private void paintDown(Location l, Room to, Terrain[][] t) {
        while (to.getLocation().getY() > l.getY()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.DOWN);
        }
        if (to.isInside(l)) {
            return;
        }
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX(), l.getY(), to.getLocation().getY()), l, to, t);
    }

    private void paintToDir(Direction d, Location l, Room to, Terrain[][] t) {
        if (d == Direction.DOWN) {
            paintDown(l, to, t);
        } else if (d == Direction.UP) {
            paintUp(l, to, t);
        } else if (d == Direction.RIGHT) {
            paintRight(l, to, t);
        } else {
            paintLeft(l, to, t);
        }
    }

    private void paintUp(Location l, Room to, Terrain[][] t) {
        while (to.getLocation().getY() + to.getH() <= l.getY()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.UP);
        }
        if (to.isInside(l)) {
            return;
        }
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX(), l.getY(), to.getLocation().getY() + to.getH() - 1), l, to, t);
    }

    private void paintRight(Location l, Room to, Terrain[][] t) {
        while (to.getLocation().getX() > l.getX()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.RIGHT);
        }
        if (to.isInside(l)) {
            return;
        }
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX(), l.getY(), to.getLocation().getY()), l, to, t);
    }

    private void paintLeft(Location l, Room to, Terrain[][] t) {
        while (to.getLocation().getX() + to.getW() <= l.getX()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.LEFT);
        }
        if (to.isInside(l)) {
            return;
        }
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX() + to.getW() - 1, l.getY(), to.getLocation().getY()), l, to, t);
    }

}
