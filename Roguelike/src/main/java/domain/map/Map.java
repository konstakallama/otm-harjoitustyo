/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.map;

import domain.mapobject.Enemy;
import domain.mapobject.player.Player;
import domain.items.MapItem;
import domain.support.Direction;
import domain.gamemanager.CommandResult;
import domain.gamemanager.AttackResultType;
import domain.gamemanager.AttackResult;
import domain.support.Location;
import java.util.*;

/**
 *
 * @author konstakallama
 */
public class Map {

    private Enemy[][] enemies;
    private MapItem[][] items;
    private Terrain[][] terrain;
    private int floor;
    private Player player;
    private int mapW;
    private int mapH;
    private ArrayList<Room> rooms;
    private VisibilityStatus[][] visibility;

    public Map(int mapWidth, int mapHeight, Terrain[][] terrain, int floor) {
        this(mapWidth, mapHeight, terrain, floor, new ArrayList<>());
    }

    public Map(int mapWidth, int mapHeight, Terrain[][] terrain, int floor, ArrayList<Room> rooms) {
        this.enemies = new Enemy[mapWidth][mapHeight];
        this.terrain = terrain;
        this.floor = floor;
        this.items = new MapItem[mapWidth][mapHeight];

        this.mapH = mapHeight;
        this.mapW = mapWidth;
        this.rooms = rooms;
        initVisibility(mapWidth, mapHeight);
    }

    
    /**
     * Returns true if the tile specified by x and y is currently occupied by a wall, an enemy or the player or if the specified tile is out of bounds of the map.
     * @param x x-coordinate on the map.
     * @param y y-coordinate on the map.
     * @return true if the tile specified by x and y is currently occupied by a wall, an enemy or the player or if the specified tile is out of bounds of the map.
     */
    public boolean isOccupied(int x, int y) {
        if (this.isOutOfBounds(x, y)) {
            return true;
        }
        if (this.terrain[x][y].isOccupied()) {
            return true;
        }
        if (this.player != null) {
            if (this.player.getX() == x && this.player.getY() == y) {
                return true;
            }
        }

        if (this.enemies[x][y] == null) {
            return false;
        }
        return this.enemies[x][y].isOccupied();
    }
    /**
     * Adds an enemy to the specified tile on the map. Note that this will override any enemy already on the tile and does not check for terrain; it should always be ensured that map.isOccupied(x, y) is called first to make sure the tile is free.
     * @param x
     * @param y
     * @param o Enemy to add
     */
    public void addEnemy(int x, int y, Enemy o) {
        this.enemies[x][y] = o;
    }
    /**
     * Moves the enemy in x, y 1 tile to the specified direction. Returns true if the move is successful and false if the tile in direction d is occupied. The method assumes that x, y contains an enemy.
     * @param x
     * @param y
     * @param d Direction to move to
     * @return true if the move is successful and false if the tile in direction d is occupied.
     */
    public boolean moveEnemy(int x, int y, Direction d) {
        return moveEnemyHelper(x, y, d.xVal(), d.yVal());
    }

    private boolean moveEnemyHelper(int x, int y, int dx, int dy) {
        if (this.isOccupied(x + dx, y + dy)) {
            return false;
        }
        this.enemies[x + dx][y + dy] = this.enemies[x][y];
        this.enemies[x][y] = null;
        return true;
    }
    /**
     * Removes the enemy in x, y, if it contains one.
     * @param x
     * @param y 
     */
    public void removeEnemy(int x, int y) {
        this.enemies[x][y] = null;
    }

    public Enemy getEnemy(int x, int y) {
        return this.enemies[x][y];
    }

    public int getFloor() {
        return floor;
    }
    /**
     * Adds an item to x, y. Overwrites any item previously in x, y.
     * @param x
     * @param y
     * @param item item to add
     */
    public void addItem(int x, int y, MapItem item) {
        this.items[x][y] = item;
    }
    /**
     * Returns the item in x, y or null if it doesn't contain one.
     * @param x
     * @param y
     * @return the item in x, y or null if it doesn't contain one.
     */
    public MapItem getItem(int x, int y) {
        return this.items[x][y];
    }
    /**
     * Removes the item in x, y, if it contains one.
     * @param x
     * @param y 
     */
    public void removeItem(int x, int y) {
        this.items[x][y] = null;
    }
    /**
     * Has all the enemies on the map take their turns. Returns a list of the AttackResults of the enemies' turns.
     * @return a list of the AttackResults of the enemies' turns.
     */
    public ArrayList<AttackResult> takeTurns() {
        ArrayList<AttackResult> l = new ArrayList<>();

        for (int i = 0; i < this.enemies.length; i++) {
            for (int j = 0; j < this.enemies[i].length; j++) {
                if (this.enemies[i][j] != null) {
                    l.add(this.enemies[i][j].takeTurn());
                }
            }
        }

        for (int i = 0; i < this.enemies.length; i++) {
            for (int j = 0; j < this.enemies[i].length; j++) {
                if (this.enemies[i][j] != null) {
                    this.enemies[i][j].reset();
                }
            }
        }

        return l;

    }
    /**
     * Returns the type of terrain in x, y.
     * @param x
     * @param y
     * @return the type of terrain in x, y.
     */
    public Terrain getTerrain(int x, int y) {
        return this.terrain[x][y];
    }
    /**
     * Moves the player in direction d. Returns a CommandResult which specifies whether the move was successful and if the player is now standing on stairs. Also updates the player's visibility.
     * @param d
     * @return a CommandResult which specifies whether the move was successful and if the player is now standing on stairs.
     */
    public CommandResult movePlayer(Direction d) {
        if (this.isOccupied(player.getX() + d.xVal(), player.getY() + d.yVal())) {
            return new CommandResult(false, false, "", null);
        }

        CommandResult cr = new CommandResult(true);

        int nx = player.getX() + d.xVal();
        int ny = player.getY() + d.yVal();

        if (this.items[nx][ny] != null) {
            cr = this.pickUp(nx, ny);
        }

        if (this.terrain[nx][ny] == Terrain.STAIRS) {
            cr = new CommandResult(true, true, this.nextFloorMessage(), new AttackResult(AttackResultType.FAIL, 0, player, null), true);
        }

        player.setX(nx);
        player.setY(ny);

        this.updateVisibility();

        return cr;
    }
    /**
     * Has the player pick up the item in nx, ny. Returns a CommandResult specifying whether the action is successful.
     * @param nx
     * @param ny
     * @return a CommandResult specifying whether the action is successful.
     */
    public CommandResult pickUp(int nx, int ny) {
        CommandResult cr;

        if (player.pickUp(items[nx][ny])) {
            cr = new CommandResult(true, true, "You picked up the " + this.items[nx][ny].getName() + ".", new AttackResult(AttackResultType.FAIL, 0, player, null));
            this.items[nx][ny] = null;
        } else {
            cr = new CommandResult(true, true, "Your inventory is full.", new AttackResult(AttackResultType.FAIL, 0, player, null));
        }

        return cr;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        updateVisibility();
    }

    public Enemy[][] getEnemies() {
        return enemies;
    }

    public MapItem[][] getItems() {
        return items;
    }

    public Terrain[][] getTerrain() {
        return terrain;
    }
    /**
     * Returns true if x, y contains an Enemy, false if not or x, y is out of bounds.
     * @param x
     * @param y
     * @return true if x, y contains an Enemy, false if not or x, y is out of bounds.
     */
    public boolean hasEnemy(int x, int y) {
        if (this.isOutOfBounds(x, y)) {
            return false;
        }
        return this.enemies[x][y] != null;
    }
    /**
     * Returns true if x, y is not on the map.
     * @param x
     * @param y
     * @return true if x, y is not on the map.
     */
    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= this.mapW || y < 0 || y >= this.mapH;
    }
    /**
     * Returns the direction in which the distance from x, y to the player is the shortest.
     * @param x
     * @param y
     * @return the direction in which the distance from x, y to the player is the shortest.
     */
    public Direction getPlayerDirection(int x, int y) {
        if (Math.abs(x - this.player.getX()) >= Math.abs(y - this.player.getY())) {
            if (x - this.player.getX() < 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (y - this.player.getY() < 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }

    }
    /**
     * Returns the direction in which the distance from x, y to the player is the second shortest. Uses getPlayerDirection to determine this and the two should never return the same value.
     * @param x
     * @param y
     * @return the direction in which the distance from x, y to the player is the second shortest.
     */
    public Direction getSecondaryPlayerDirection(int x, int y) {
        Direction pd = this.getPlayerDirection(x, y);
        if (pd == Direction.DOWN || pd == Direction.UP) {
            if (x - this.player.getX() < 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (y - this.player.getY() < 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }
    }
    /**
     * Returns true if the player is on x, y.
     * @param x
     * @param y
     * @return true if the player is on x, y.
     */
    public boolean hasPlayer(int x, int y) {
        return x == this.player.getX() && y == this.player.getY();
    }
    /**
     * Sets the terrain on x, y to t.
     * @param x
     * @param y
     * @param t 
     */
    public void setTerrain(int x, int y, Terrain t) {
        this.terrain[x][y] = t;
    }
    /**
     * Returns true if the player is standing on stairs.
     * @return true if the player is standing on stairs.
     */
    public boolean playerIsOnStairs() {
        return this.terrain[player.getX()][player.getY()] == Terrain.STAIRS;
    }

    public String nextFloorMessage() {
        return "Would you like to go to the next floor? (press enter to advance)";
    }

    public int getMapW() {
        return mapW;
    }

    public int getMapH() {
        return mapH;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
    /**
     * Returns true if l is inside any room on the map.
     * @param l
     * @return true if l is inside any room on the map.
     */
    public boolean isInsideRoom(Location l) {
        for (Room r : this.rooms) {
            if (r.isInside(l)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Returns the room l is inside or null if it is not inside any; isInsideRoom should always be called first to ensure l is inside a room.
     * @param l
     * @return the room l is inside or null if it is not inside any.
     */
    public Room insideWhichRoom(Location l) {
        for (Room r : this.rooms) {
            if (r.isInside(l)) {
                return r;
            }
        }
        return null;
    }

    private void initVisibility(int mapWidth, int mapHeight) {
        this.visibility = new VisibilityStatus[mapWidth][mapHeight];

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                this.visibility[i][j] = VisibilityStatus.UNKNOWN;
            }
        }
    }

    private void updateVisibility() {
        if (this.isInsideRoom(this.player.getLocation())) {
            updateVisibilityInRoom();
        } else {
            updateVisibilityInCorridor();
        }
        for (Location l : player.getLocation().getAdjacent()) {
            if (!this.isOutOfBounds(l)) {
                this.visibility[l.getX()][l.getY()] = VisibilityStatus.IN_RANGE;
            }
        }
    }

    private void updateVisibilityInRoom() {
        Room r = this.getPlayerRoom();

        for (int i = 0; i < mapW; i++) {
            for (int j = 0; j < mapH; j++) {
                if (r.isInside(new Location(i, j))) {
                    this.visibility[i][j] = VisibilityStatus.IN_RANGE;
                } else if (r.isNextTo(new Location(i, j))) {
                    this.visibility[i][j] = VisibilityStatus.KNOWN;
                } else {
                    if (this.visibility[i][j] == VisibilityStatus.IN_RANGE) {
                        this.visibility[i][j] = VisibilityStatus.KNOWN;
                    }
                }
            }
        }
    }

    private void updateVisibilityInCorridor() {
        for (int i = 0; i < mapW; i++) {
            for (int j = 0; j < mapH; j++) {
                if (this.visibility[i][j] == VisibilityStatus.IN_RANGE) {
                    this.visibility[i][j] = VisibilityStatus.KNOWN;
                }
            }
        }

        corridorVisibilityBfs();
    }

    public VisibilityStatus[][] getVisibility() {
        return visibility;
    }
    /**
     * Returns the VisibilityStatus of x, y.
     * @param x
     * @param y
     * @return the VisibilityStatus of x, y.
     */
    public VisibilityStatus getVisibility(int x, int y) {
        return visibility[x][y];
    }

    private void corridorVisibilityBfs() {
        ArrayDeque<Location> q = new ArrayDeque<>();
        q.add(this.player.getLocation());

        while (!q.isEmpty()) {
            Location l = q.poll();

            this.visibility[l.getX()][l.getY()] = VisibilityStatus.IN_RANGE;

            for (Location a : l.getAdjacent()) {
                if (!this.isOutOfBounds(a)) {
                    if (this.terrain[a.getX()][a.getY()] != Terrain.WALL) {
                        if (a.manhattanDistance(player.getLocation()) > l.manhattanDistance(player.getLocation()) && a.manhattanDistance(player.getLocation()) <= player.getVisionRange()) {
                            q.add(a);
                        }
                    }

                }
            }
        }
    }

    private boolean isOutOfBounds(Location l) {
        return this.isOutOfBounds(l.getX(), l.getY());
    }
    /**
     * Returns the room the player is in or next to, or a zero-room with w and h of 0 if they are not inside any.
     * @return the room the player is in or next to, or a zero-room with w and h of 0 if they are not inside any.
     */
    public Room getPlayerRoom() {
        if (this.player == null) {
            return new Room(new Location(0, 0), 0, 0);
        } else if (!this.isInsideRoom(this.player.getLocation())) {
            for (Location l : this.player.getLocation().getAdjacent()) {
                if (this.isInsideRoom(l)) {
                    return this.insideWhichRoom(l);
                }
            }
            return new Room(new Location(0, 0), 0, 0);
        }
        return this.insideWhichRoom(this.player.getLocation());
    }

}
