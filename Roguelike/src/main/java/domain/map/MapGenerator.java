package domain.map;

import domain.items.MapItem;
import domain.support.Formulas;
import domain.support.Direction;
import domain.support.Location;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Generates randomized maps.
 *
 * @author konstakallama
 */
public class MapGenerator {

    Random r = new Random();
    Formulas f = new Formulas();
    int fireTomesCreated = 0;
    String[] swords = {"bronze sword", "iron sword", "steel sword", "silver sword"};
    int swordIndex = 0;
    String[] lances = {"bronze lance", "iron lance", "steel lance", "silver lance"};
    int lanceIndex = 0;
    String[] axes = {"bronze axe", "iron axe", "steel axe", "silver axe"};
    int axeIndex = 0;
    String[] armor = {"leather armor", "iron armor", "elven armor", "heavy armor", "glass armor"};
    int armorIndex = 0;

    /**
     * Creates a new random map with the specified parameters. Also adds
     * possible items.
     *
     * @param w map width
     * @param h map height
     * @param floor the floor of the dungeon this map is of. Affects the formula
     * for the number of rooms on the map.
     * @return a new randomly generated map.
     */
    public Map createTestMap(int w, int h, int floor) {
        Map m = createTestTerrain(w, h, floor, 4 + r.nextInt(Math.min(floor / 3 + 1, 4)), 1);
        this.addStairs(m);
        this.addItems(m);

        return m;
    }

    /**
     * Adds the specified item to the given map.
     *
     * @param m map to add item to
     * @param name name of the item to be added (all item names in the game are
     * unique meaning this is sufficient information to determine the exact item
     * intended)
     */
    public void addItem(Map m, String name) {
        Location l = f.createRandomFreeLocation(m);

        MapItem item = new MapItem(l.getX(), l.getY(), m, name, true);

        m.addItem(l.getX(), l.getY(), item);
    }

    private void addStairs(Map m) {
        Location l = f.createRandomFreeLocation(m);
        m.setTerrain(l.getX(), l.getY(), Terrain.STAIRS);
    }

    private Map createTestTerrain(int w, int h, int floor, int roomAmount, int corridorAmount) {
        Terrain[][] t = new Terrain[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                t[i][j] = Terrain.WALL;
            }
        }

        ArrayList<Room> rooms = new ArrayList<>();

        for (int i = 0; i < roomAmount; i++) {
            Room room = this.createTestRoomIterator(t);
            if (room != null) {
                rooms.add(room);
            }
        }

        createCorridors(rooms, corridorAmount);

        connectRooms(rooms);

        paintCorridors(t, rooms);

        return new Map(w, h, t, floor, rooms);
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

    private void createCorridors(ArrayList<Room> rooms, int amount) {
        for (Room room : rooms) {
            for (int i = 0; i < amount; i++) {
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
                room.getConnected().stream().filter((r3) -> (!visited[rooms.indexOf(r3)])).forEachOrdered((r3) -> {
                    q.add(r3);
                });
            }
        }
        return false;
    }

    private void paintCorridors(Terrain[][] t, ArrayList<Room> rooms) {
        for (Room room : rooms) {
            ArrayList<Corridor> toRm = new ArrayList<>();
            for (Corridor c : room.getCorridors()) {
                if (!c.getFrom().equals(c.getTo())) {
                    paintCorridor(t, c);
                } else {
                    toRm.add(c);
                }
            }
            for (Corridor c : toRm) {
                room.removeCorridor(c);
            }
        }
    }

    private void paintCorridor(Terrain[][] t, Corridor c) {
        Room from = c.getFrom();
        Room to = c.getTo();

        Direction d = getClosestDirForRooms(from, to);

        Location start = getCorridorStart(from, d, t);

        c.setStart(new Location(start.getX(), start.getY()));

        this.paintToDir(d, start, to, t, c);
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
            } else if (fromY - toY > 0) {
                return Direction.UP;
            } else {
                return Direction.NONE;
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
            l = moveLRight(l, from);

            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                return l;
            }
        }
        l = startL;
        while (l.getX() > from.getLocation().getX()) {
            l.move(Direction.LEFT);

            if (from.getNW().getX() < l.getX()) {
                l.move(Direction.LEFT);
            }

            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                return l;
            }
        }
        return startL;
    }

    private Location getValidVertical(Location l, Terrain[][] t, Room from, Location startL) {
        while (l.getY() < from.getLocation().getY() + from.getH()) {
            l = moveLDown(l, from);

            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                return l;
            }
        }
        l = startL;
        while (l.getY() > from.getLocation().getY()) {
            l.move(Direction.UP);

            if (from.getNW().getY() < l.getY()) {
                l.move(Direction.UP);
            }

            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                return l;
            }
        }
        return startL;
    }

    private void paintDown(Location l, Room to, Terrain[][] t, Corridor c) {

        while (to.getLocation().getY() > l.getY()) {

            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.DOWN);
        }
        if (to.isInside(l)) {
            l.move(Direction.UP);
            c.setEnd(l);
            return;
        }
        c.setTurn(l);
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX(), l.getY(), to.getLocation().getY()), l, to, t, c);

    }

    private void paintToDir(Direction d, Location l, Room to, Terrain[][] t, Corridor c) {
        if (to.isNextTo(l)) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            c.setEnd(l);
            return;
        }
        if (d == Direction.NONE) {
            return;
        }
        if (d == Direction.DOWN) {
            paintDown(l, to, t, c);
        } else if (d == Direction.UP) {
            paintUp(l, to, t, c);
        } else if (d == Direction.RIGHT) {
            paintRight(l, to, t, c);
        } else {
            paintLeft(l, to, t, c);
        }
    }

    private void paintUp(Location l, Room to, Terrain[][] t, Corridor c) {
        while (to.getLocation().getY() + to.getH() <= l.getY()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.UP);
        }
        if (to.isInside(l)) {
            l.move(Direction.DOWN);
            c.setEnd(l);
            return;
        }
        c.setTurn(l);
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX(), l.getY(), to.getLocation().getY() + to.getH() - 1), l, to, t, c);

    }

    private void paintRight(Location l, Room to, Terrain[][] t, Corridor c) {
        while (to.getLocation().getX() > l.getX()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.RIGHT);
        }
        if (to.isInside(l)) {
            l.move(Direction.LEFT);
            c.setEnd(l);
            return;
        }
        c.setTurn(l);
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX(), l.getY(), to.getLocation().getY()), l, to, t, c);
    }

    private void paintLeft(Location l, Room to, Terrain[][] t, Corridor c) {
        while (to.getLocation().getX() + to.getW() <= l.getX()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.LEFT);
        }
        if (to.isInside(l)) {
            l.move(Direction.RIGHT);
            c.setEnd(l);
            return;
        }
        c.setTurn(l);
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX() + to.getW() - 1, l.getY(), to.getLocation().getY()), l, to, t, c);
    }

    private Location moveLDown(Location l, Room from) {
        l.move(Direction.DOWN);
        if (from.getSW().getY() > l.getY()) {
            l.move(Direction.DOWN);
        }
        return l;
    }

    private Location moveLRight(Location l, Room from) {
        l.move(Direction.RIGHT);
        if (from.getNE().getX() > l.getX()) {
            l.move(Direction.RIGHT);
        }
        return l;
    }

    private void addItems(Map m) {      
        if (r.nextDouble() < 0.4) {
            addRandomSpell(m);
        }      
        addWeapons(m);
        for (int i = 0; i < 2; i++) {
            if (r.nextDouble() < 0.8) {
                this.addItem(m, "apple");
            }
            if (r.nextDouble() < 0.5) {
                this.addItem(m, "potion");
            }
        }
    }

    private void addRandomSpell(Map m) {
        double d = r.nextDouble();      
        if (d < 0.4) {
            this.addItem(m, "fire tome");
        } else if (d < 0.7) {
            this.addItem(m, "freeze tome");
        } else if (d < 0.8) {
            this.addItem(m, "stun tome");
        } else if (d < 1) {
            this.addItem(m, "heal wound tome");
        }
    }

    private void addWeapons(Map m) {
        if (r.nextDouble() < 0.1 && this.swordIndex < this.swords.length) {
            this.addItem(m, this.swords[swordIndex]);
            this.swordIndex++;
        }       
        if (r.nextDouble() < 0.1 && this.lanceIndex < this.lances.length) {
            this.addItem(m, this.lances[lanceIndex]);
            this.lanceIndex++;
        }       
        if (r.nextDouble() < 0.1 && this.axeIndex < this.axes.length) {
            this.addItem(m, this.axes[axeIndex]);
            this.axeIndex++;
        }        
        if (r.nextDouble() < 0.1 && this.armorIndex < this.armor.length) {
            this.addItem(m, this.armor[armorIndex]);
            this.armorIndex++;
        }
    }
}
