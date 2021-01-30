/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.map;

import domain.support.LocDir;
import domain.support.Direction;
import domain.support.Location;
import java.util.Random;
import domain.support.MapGenerator2Parameters;
import java.util.ArrayList;

/**
 *
 * @author konstakallama
 */
public class MapGenerator2 {
    
    //Possible parameters to add:
    //Pre-occupied wall that can't be painted over

    Random r = new Random();
    int maxW;
    int maxH;
    MapGenerator2Parameters par;
    Map m;
    ArrayList<Room> rooms;
    
    public Map createMap(int maxW, int maxH) {
        MapGenerator2Parameters par = new MapGenerator2Parameters(maxW, maxH, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return this.createMap(par);
    }
    
    public Map createMap(int maxW, int maxH, long randomSeed) {
        r.setSeed(randomSeed);
        MapGenerator2Parameters par = new MapGenerator2Parameters(maxW, maxH, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return this.createMap(par);
    }
    
    public Map createMap(MapGenerator2Parameters par, long randomSeed) {
        r.setSeed(randomSeed);
        return this.createMap(par);
    }

    public Map createMap(MapGenerator2Parameters par) {
        this.par = par;
        this.maxW = par.maxW;
        this.maxH = par.maxH;
        Terrain[][] t = initT(maxW, maxH);
        //m = new Map(maxW, maxH);
        //m.setT(t);
        
        //Create first room of random size and location
        int w = par.minRoomW + r.nextInt(par.maxRoomW);
        int h = par.minRoomW + r.nextInt(par.maxRoomW);
        Room r0 = new Room(new Location(r.nextInt(maxW - par.maxRoomW) + 1, r.nextInt(maxH - par.maxRoomH + 1)), w, h);
        paintRoom(t, r0.getLocation(), r0.getW(), r0.getH());
        rooms = new ArrayList<>();
        rooms.add(r0);
              
        //m.recordHistory();
        
        //Attempt to add something steps amount of times
        for (int i = 0; i < par.steps; i++) {
            
            //Select random wall next to a room or corridor, also returns the direction in which to build
            LocDir ld = selectRandomWall(t);

            //Selection is random atm so it's possible that it fails
            if (ld == null) {
                continue;
            }

            double rn = r.nextDouble();
            Location behind = ld.getL().locInDir(ld.getD().getOpposite());
            //If behind is a room, try a corridor. Otherwise random besed on roomChance.
            if (rn < par.roomChance && t[behind.getX()][behind.getY()] != Terrain.FLOOR) {
                tryRoom(ld.getL(), ld.getD(), t);
            } else {
                tryCorridorWithConnect(ld.getL(), ld.getD(), t);
            }
        }

        removeDeadEnds(t);
        findCorridorStarts(t);
        m = new Map(maxW, maxH, t, 0, rooms);
        
        //this.printMap(t);

        return m;
    }

    /**
     * Initialize t to contain only wall.
     */
    private Terrain[][] initT(int w, int h) {
        Terrain[][] t = new Terrain[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                t[i][j] = Terrain.WALL;
            }
        }
        return t;
    }

    /**
     * Selects random tiles until it finds and returns one that is wall next to just one floor/corridor tile.
     */
    private LocDir selectRandomWall(Terrain[][] t) {
        for (int i = 0; i < 100; i++) {
            Location l = new Location(r.nextInt(maxW), r.nextInt(maxH));
            Direction d = wallNextToFloor(l, t);
            if (d != Direction.NONE) {
                return new LocDir(l, d);
            }
        }
        return null;
    }

    /**
     * Checks if tile is wall with exactly 1 adjacent floor/corridor.
     */
    private Direction wallNextToFloor(Location l, Terrain[][] t) {
        if (t[l.getX()][l.getY()] != Terrain.WALL) {
            return Direction.NONE;
        }

        int floors = 0;
        Direction d = Direction.NONE;
        for (Location a : l.getAdjacent()) {
            if (!this.outOfBounds(a)) {
                if (t[a.getX()][a.getY()] != Terrain.WALL) {
                    floors++;
                    d = a.getClosestDir(l);
                }
            }
        }
        if (floors == 1) {
            return d;
        }
        return Direction.NONE;
    }
    
    /**
     * Tries to add a room of random size to d from l. Does nothing if the terrain there is not wall.
     */
    private void tryRoom(Location l, Direction d, Terrain[][] t) {
        int w = par.minRoomW + r.nextInt(par.maxRoomW);
        int h = par.minRoomW + r.nextInt(par.maxRoomW);

        if (validRoomLocation(l, d, t, w, h)) {
            t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            paintRoom(t, getRoomNw(l, d, w, h), w, h);
            rooms.add(new Room(getRoomNw(l, d, w, h), w, h));
            //m.recordHistory();
        }
    }

    /**
     * Dumb corridor build which fails if terrain is not wall. Unused.
     */
    private void tryCorridorNoConnect(Location l, Direction d, Terrain[][] t) {
        int len = par.minCorridorLen + r.nextInt(par.maxCorridorLen - par.minCorridorLen);
        if (this.validRoomLocation(l, d, t, 3, len)) {
            paintCorridor(l, d, len, t);
        }
    }

    /**
     * Paint rectangular room (w, h) to l.
     */
    private void paintRoom(Terrain[][] t, Location l, int w, int h) {
        for (int i = l.getX(); i < l.getX() + w; i++) {
            for (int j = l.getY(); j < l.getY() + h; j++) {
                if (!this.outOfBounds(new Location(i, j))) {
                    t[i][j] = Terrain.FLOOR;
                }
            }
        }
    }

    /**
     * True if terrain in w x h space in direction d from location l is only wall.
     */
    private boolean validRoomLocation(Location l, Direction d, Terrain[][] t, int w, int h) {
        return this.checkRoomLocation(this.getRoomNw(l, d, w, h), this.getRoomSe(l, d, w, h), t);
    }

    private Location getRoomNw(Location l, Direction d, int w, int h) {
        if (d == Direction.DOWN) {
            return new Location(l.getX() - (w / 2), l.getY() + 1);
        } else if (d == Direction.LEFT) {
            return new Location(l.getX() - w, l.getY() - (h / 2));
        } else if (d == Direction.RIGHT) {
            return new Location(l.getX() + 1, l.getY() - (h / 2));
        } else if (d == Direction.UP) {
            return new Location(l.getX() - (w / 2), l.getY() - h);
        }
        return null;
    }

    private Location getRoomSe(Location l, Direction d, int w, int h) {
        if (d == Direction.DOWN) {
            return new Location(l.getX() + (w / 2), l.getY() + h);
        } else if (d == Direction.LEFT) {
            return new Location(l.getX() - 1, l.getY() + (h / 2));
        } else if (d == Direction.RIGHT) {
            return new Location(l.getX() + w, l.getY() + (h / 2));
        } else if (d == Direction.UP) {
            return new Location(l.getX() + (w / 2), l.getY() - 1);
        }
        return null;
    }

    private boolean checkRoomLocation(Location nw, Location se, Terrain[][] t) {
        for (int i = nw.getX() - 1; i < se.getX() + 2; i++) {
            for (int j = nw.getY() - 1; j < se.getY() + 2; j++) {
                if (this.outOfBounds(new Location(i, j))) {
                    return false;
                } else if (t[i][j] != Terrain.WALL) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean outOfBounds(Location a) {
        int x = a.getX();
        int y = a.getY();

        if (x < 0) {
            return true;
        } else if (x >= maxW) {
            return true;
        } else if (y < 0) {
            return true;
        } else if (y >= maxH) {
            return true;
        }

        return false;
    }

    /**
     * Paint a corridor of len tiles in direction d from location l. Stops if encounters something other than wall.
     */
    private void paintCorridor(Location l, Direction d, int len, Terrain[][] t) {
        for (int i = 0; i < len; i++) {
            if (this.outOfBounds(l)) {
                break;
            } else if (t[l.getX()][l.getY()] != Terrain.WALL) {
                break;
            }
            t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            l.move(d);
        }
    }

    /**
     * Paint corridor of random length from l to d. Extends by a maximum of connectDistance if there is a floor/corridor tile to connect to within that distance.
     */
    private void tryCorridorWithConnect(Location l, Direction d, Terrain[][] t) {
        int len = par.minCorridorLen + r.nextInt(par.maxCorridorLen - par.minCorridorLen);
        Location oldL = new Location(l.getX(), l.getY());
        if (scanCorridor(l, d, len, t)) {
            l.move(d, len);
            if (this.scanCorridor(l, d, par.connectDistance, t) && canConnect(l, d, par.connectDistance, t)) {
                len += par.connectDistance;
            }
            this.paintCorridor(oldL, d, len, t);
            //m.recordHistory();
        }
    }

    /**
     * True if terrain surrounding specified corridor is only wall. Does not check tiles the corridor will actually be painted on.
     */
    private boolean scanCorridor(Location l, Direction d, int len, Terrain[][] t) {
        for (int i = 0; i < len; i++) {
            Location l0 = l.locInDir(d.getClockwiseTurn());
            if (this.outOfBoundsOrNotWall(l0, t)) {
                return false;
            }
            l0 = l.locInDir(d.getCounterclockwiseTurn());
            if (this.outOfBoundsOrNotWall(l0, t)) {
                return false;
            }
            l = l.locInDir(d);
        }
        return true;
    }

    private boolean outOfBoundsOrNotWall(Location l, Terrain[][] t) {
        if (this.outOfBounds(l)) {
            return true;
        } else if (t[l.getX()][l.getY()] != Terrain.WALL) {
            return true;
        }
        return false;
    }

    private boolean notOutOfBoundsAndNotWall(Location l, Terrain[][] t) {
        if (!this.outOfBounds(l)) {
            if (t[l.getX()][l.getY()] != Terrain.WALL) {
                return true;
            }
        }
        return false;
    }

    /**
     * True if there is a floor/corridor tile in specified corridor's path.
     */
    private boolean canConnect(Location l, Direction d, int len, Terrain[][] t) {
        for (int i = 0; i < len; i++) {
            if (this.notOutOfBoundsAndNotWall(l, t)) {
                return true;
            }
            l = l.locInDir(d);
        }
        return false;
    }

    /**
     * Runs removeDeadEndCorridor on every tile on the map.
     */
    private void removeDeadEnds(Terrain[][] t) {
        for (int i = 0; i < this.maxW; i++) {
            for (int j = 0; j < this.maxH; j++) {
                boolean de = deadEnd(new Location(i, j), t) != Direction.NONE;
                this.removeDeadEndCorridor(new Location(i, j), t);
                if (de) {
                    //m.recordHistory();
                }
            }
        }
    }

    /**
     * Checks if tile is a dead end corridor and recursively removes that corridor until the head no longer is a dead end (ie room or intersection).
     */
    private void removeDeadEndCorridor(Location l, Terrain[][] t) {
        Direction d = deadEnd(l, t);
        if (d != Direction.NONE) {
            t[l.getX()][l.getY()] = Terrain.WALL;
            l = l.locInDir(d);
            this.removeDeadEndCorridor(l, t);
        }
    }

    /**
     * If tile is a dead end corridor (surrounded by 3 walls and 1 floor/corridor), returns the direction of the 1 floor/corridor. Otherwise returns DIrection.NONE.
     */
    private Direction deadEnd(Location l, Terrain[][] t) {
        if (t[l.getX()][l.getY()] != Terrain.CORRIDOR) {
            return Direction.NONE;
        }
        int count = 0;
        Direction d = Direction.NONE;

        Direction turn = Direction.DOWN;

        for (int k = 0; k < 4; k++) {
            if (this.notOutOfBoundsAndNotWall(l.locInDir(turn), t)) {
                count++;
                d = turn;
            }
            turn = turn.getClockwiseTurn();
        }

        if (count == 1) {
            return d;
        }
        return Direction.NONE;

    }

    private void findCorridorStarts(Terrain[][] t) {
        //printMap(t);
        for (Room rm : rooms) {
            //System.out.println(rm);
            ArrayList<Location> cs = new ArrayList<>();
            for (Location l : rm.getSurroundings()) {
                //System.out.println(l);
                if (!outOfBounds(l)) {
                    if (isCorridor(l, t)) {
                        cs.add(l);
                        //System.out.println(l);
                    }
                }
            }
            rm.setCorridorStarts(cs);
        }
    }
    
    private boolean isCorridor(Location l, Terrain[][] t) {
        if (!this.outOfBounds(l)) {
            return t[l.getX()][l.getY()] == Terrain.CORRIDOR;
        }
        return false;
    }
    
    private boolean isCorridor2(Location l, Terrain[][] t) {
        boolean ud = udWall(l, t);
        boolean lr = lrWall(l, t);
        if (ud) {
            return !lr;
        } else {
            return lr;
        }
    }

    private boolean udWall(Location l, Terrain[][] t) {
        Location u = l.locInDir(Direction.UP);
        Location d = l.locInDir(Direction.DOWN);
        if (this.outOfBounds(u) || this.outOfBounds(d)) {
            return false;
        }
        return t[u.getX()][u.getY()] == Terrain.WALL && t[d.getX()][d.getY()] == Terrain.WALL;
    }

    private boolean lrWall(Location l, Terrain[][] t) {
        Location f = l.locInDir(Direction.LEFT);
        Location g = l.locInDir(Direction.RIGHT);
        if (this.outOfBounds(f) || this.outOfBounds(g)) {
            return false;
        }
        return t[f.getX()][f.getY()] == Terrain.WALL && t[g.getX()][g.getY()] == Terrain.WALL;
    }
    
    private void printMap(Terrain[][] t) {
        for (int i = 0; i < t[0].length; i++) {
            for (int j = 0; j < t.length; j++) {
                String k = "#";
                if (t[j][i] == Terrain.FLOOR) {
                    k = ".";
                } else if (t[j][i] == Terrain.CORRIDOR) {
                    k = "o";
                }
                System.out.print(k + " ");
            }
            System.out.println("");
        }
    }


}
