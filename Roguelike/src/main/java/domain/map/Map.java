/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.map;

import domain.mapobject.Enemy;
import domain.mapobject.Player;
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

    public Map(int mapWidth, int mapHeight, Terrain[][] terrain, int floor) {
        this.enemies = new Enemy[mapWidth][mapHeight];
        this.terrain = terrain;
        this.floor = floor;
        this.items = new MapItem[mapWidth][mapHeight];

        this.mapH = mapHeight;
        this.mapW = mapWidth;
        this.rooms = new ArrayList<>();
    }

    public Map(int mapWidth, int mapHeight, Terrain[][] terrain, int floor, ArrayList<Room> rooms) {
        this.enemies = new Enemy[mapWidth][mapHeight];
        this.terrain = terrain;
        this.floor = floor;
        this.items = new MapItem[mapWidth][mapHeight];

        this.mapH = mapHeight;
        this.mapW = mapWidth;
        this.rooms = rooms;
    }

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

    public void addEnemy(int x, int y, Enemy o) {
        this.enemies[x][y] = o;
    }

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

    public void removeEnemy(int x, int y) {
        this.enemies[x][y] = null;
    }

    public Enemy getEnemy(int x, int y) {
        return this.enemies[x][y];
    }

    public int getFloor() {
        return floor;
    }

    public void addItem(int x, int y, MapItem item) {
        this.items[x][y] = item;
    }

    public MapItem getItem(int x, int y) {
        return this.items[x][y];
    }

    public void removeItem(int x, int y) {
        this.items[x][y] = null;
    }

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

    public Terrain getTerrain(int x, int y) {
        return this.terrain[x][y];
    }

    public CommandResult movePlayer(Direction d) {
        if (this.isOccupied(player.getX() + d.xVal(), player.getY() + d.yVal())) {
            return new CommandResult(false, false, "", null);
        }
        
        CommandResult cr = new CommandResult(true, false, "", null);
        
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
        
        return cr;
    }

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

    public boolean hasEnemy(int x, int y) {
        if (this.isOutOfBounds(x, y)) {
            return false;
        }
        return this.enemies[x][y] != null;
    }

    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= this.mapW || y < 0 || y >= this.mapH;
    }

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

    public boolean hasPlayer(int x, int y) {
        return x == this.player.getX() && y == this.player.getY();
    }

    public void setTerrain(int x, int y, Terrain t) {
        this.terrain[x][y] = t;
    }

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
    
    public boolean isInsideRoom(Location l) {
        for (Room r : this.rooms) {
            if (r.isInside(l)) {
                return true;
            }
        }
        return false;
    }
    
    public Room insideWhichRoom(Location l) {
        for (Room r : this.rooms) {
            if (r.isInside(l)) {
                return r;
            }
        }
        return null;
    }

}
