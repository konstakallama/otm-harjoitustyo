/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.mapobject;

import domain.map.Map;
import domain.support.Direction;
import domain.gamemanager.AttackResultType;
import domain.gamemanager.AttackResult;
import domain.support.Location;
import java.util.*;

/**
 *
 * @author konstakallama
 */
public class Enemy extends Moves {

    boolean visible;
    EnemyStats stats;
    boolean hasMoved;
    Direction lastMoved;
    Location movingTowards;

    public Enemy(int x, int y, Map map, EnemyStats stats, boolean visible) {
        super(map, x, y, stats.getType().getName());
        this.stats = stats;
        this.visible = visible;
        this.hasMoved = false;
        this.lastMoved = randomDirection();
        this.movingTowards = null;
    }

    public EnemyType getType() {
        EnemyStats s = (EnemyStats) this.stats;
        return s.getType();
    }

    public EnemyStats getStats() {
        return this.stats;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public boolean isEnemy() {
        return true;
    }

    public void die() {
        map.removeEnemy(x, y);
    }

    public AttackResult takeTurn() {
        AttackResult result;

        if (!this.hasMoved) {
            Direction pd = map.getPlayerDirection(x, y);
            if (map.hasPlayer(x + pd.xVal(), y + pd.yVal())) {
                result = this.attack(pd);
            } else {
                result = new AttackResult(AttackResultType.FAIL, 0, this, null);

                moveAi(pd);

            }
        } else {
            result = new AttackResult(AttackResultType.FAIL, 0, this, null);
        }

        this.hasMoved = true;
        return result;
    }

    private void randomMove() {
        this.move(this.randomDirection());
    }

    public void reset() {
        this.hasMoved = false;
    }

    public AttackResult attack(Direction d) {
        if (map.hasPlayer(x + d.xVal(), y + d.yVal())) {
            if (f.attackHits(this.stats, map.getPlayer().getStats())) {
                return f.enemyDamageCalculation(this, map.getPlayer());
            } else {
                return new AttackResult(AttackResultType.MISS, 0, this, map.getPlayer(), f.hitProb(stats, map.getPlayer().getStats()));
            }
        }
        return new AttackResult(AttackResultType.FAIL, 0, this, null);
    }

    public boolean move(Direction d) {
        if (this.map.moveEnemy(x, y, d)) {
            this.x += d.xVal();
            this.y += d.yVal();
            this.lastMoved = d;
            return true;
        }
        return false;
    }

    private void moveAi(Direction pd) {
        boolean inRoom = false;
        if (seesPlayer()) {
            moveTowardsPlayer(pd);
        } else if (inRoom()) {
            inRoom = true;
            if (this.movingTowards == null) {
                pickTarget();
            } else if (this.movingTowards.equals(new Location(0, 0))) {
                pickTarget();
            }
            this.moveTowards(movingTowards);
        } else if (map.isOccupied(x + this.lastMoved.xVal(), y + this.lastMoved.yVal())) {
            if (inCorner()) {
                moveInCorner();
            } else {
                randomTurn();
            }
        } else if (inCorridor(x + lastMoved.getOpposite().xVal(), y + lastMoved.getOpposite().yVal())) {
            this.move(this.lastMoved);
        } else if (nextToCorridor()) {
//            System.out.println("*");
            this.move(this.corridorDir());
        } else {
            this.move(this.lastMoved);
        }
        
        if (!inRoom) {
            this.movingTowards = null;
        }

    }

    private boolean inCorridor(int x, int y) {
        Location l = new Location(x, y);
        int k = 0;
        Direction d = Direction.DOWN;
        for (int i = 0; i < 4; i++) {
            Location l2 = l.locInDir(d);
            if (map.isOccupied(l2.getX(), l2.getY())) {
                k++;
            }
            d = d.getClockwiseTurn();
        }
        return k == 3;
    }

    private boolean seesPlayer() {
        if (map.isInsideRoom(new Location(x, y))) {
            if (map.insideWhichRoom(new Location(x, y)).isInside(new Location(map.getPlayer().getX(), map.getPlayer().getY()))) {
                return true;
            }
        }
        return playerBfs(new Location(x, y));
    }

    private boolean inCorner() {
        return this.checkOccupied(lastMoved)
                && (this.checkOccupied(lastMoved.getClockwiseTurn())
                || this.checkOccupied(lastMoved.getCounterclockwiseTurn()));
    }

    private void moveInCorner() {
        if (!this.checkOccupied(lastMoved.getClockwiseTurn())) {
            this.move(lastMoved.getClockwiseTurn());
        } else if (!this.checkOccupied(lastMoved.getCounterclockwiseTurn())) {
            this.move(lastMoved.getCounterclockwiseTurn());
        } else {
            this.move(lastMoved.getOpposite());
        }
    }

    private boolean checkOccupied(Direction d) {
        return map.isOccupied(x + d.xVal(), y + d.yVal());
    }

    private void randomTurn() {
        Random r = new Random();
        int coin = r.nextInt(2);
        if (coin > 0) {
            this.move(lastMoved.getClockwiseTurn());
        } else {
            this.move(lastMoved.getCounterclockwiseTurn());
        }
    }

    private Direction randomDirection() {
        Random r = new Random();
        double d = r.nextDouble();
        if (d < 0.25) {
            return (Direction.UP);
        } else if (d < 0.5) {
            return (Direction.DOWN);
        } else if (d < 0.75) {
            return (Direction.RIGHT);
        } else {
            return (Direction.LEFT);
        }
    }

    private boolean nextToCorridor() {
        Direction d = this.lastMoved.getOpposite();
        for (int i = 0; i < 3; i++) {
            d = d.getClockwiseTurn();
            if (this.inCorridor(x + d.xVal(), y + d.yVal())) {
                return true;
            }
        }
        return false;
    }

    private Direction corridorDir() {
        Direction d = this.lastMoved.getOpposite();
        for (int i = 0; i < 4; i++) {
            d = d.getClockwiseTurn();
            if (this.inCorridor(x + d.xVal(), y + d.yVal()) && !map.isOccupied(x + d.xVal(), y + d.yVal())) {
                return d;
            }
        }
        return this.randomDirection();
    }

    private boolean playerBfs(Location location) {
        boolean[][] visited = new boolean[50][50];
        ArrayDeque<Location> q = new ArrayDeque<>();
        q.add(location);
        int visionRange = 9;

        while (!q.isEmpty()) {
            Location l = q.poll();

            if (map.hasPlayer(l.getX(), l.getY())) {
                return true;
            }

            Direction d = Direction.UP;

            for (int i = 0; i < 4; i++) {
                Location ld = l.locInDir(d);
                if (ld.getX() >= 0 && ld.getY() >= 0 && ld.getX() < map.getMapW() && ld.getY() < map.getMapH()) {
                    if (!visited[ld.getX()][ld.getY()]) {
                        visited[ld.getX()][ld.getY()] = true;
                        if (map.hasPlayer(ld.getX(), ld.getY())) {
                            return true;
                        }
                        if (!map.isOccupied(ld.getX(), ld.getY()) && ld.manhattanDistance(x, y) <= visionRange) {
                            q.add(ld);
                        }
                    }
                }

                d = d.getClockwiseTurn();
            }
        }

        return false;
    }

    private void moveTowardsPlayer(Direction pd) {
        if (map.isOccupied(x + pd.xVal(), y + pd.yVal())) {
            Direction spd = (map.getSecondaryPlayerDirection(x, y));
            if (map.isOccupied(x + spd.xVal(), y + spd.yVal())) {
                Direction tpd = spd.getOpposite();
                if (map.isOccupied(x + tpd.xVal(), y + tpd.yVal())) {
                    this.move(pd.getOpposite());
                } else {
                    this.move(tpd);
                }
            } else {
                this.move(spd);
            }
        } else {
            this.move(pd);
        }
    }
    
    private void moveTowards(Location l) {
        Direction pd = this.getDirectionTowards(l);
        
        if (map.isOccupied(x + pd.xVal(), y + pd.yVal())) {
            Direction spd = (getSecondaryDirectionTowards(l, pd));
            if (map.isOccupied(x + spd.xVal(), y + spd.yVal())) {
                Direction tpd = spd.getOpposite();
                if (map.isOccupied(x + tpd.xVal(), y + tpd.yVal())) {
                    this.move(pd.getOpposite());
                } else {
                    this.move(tpd);
                }
            } else {
                this.move(spd);
            }
        } else {
            this.move(pd);
        }
    }
    
    public Direction getDirectionTowards(Location l) {
        if (Math.abs(x - l.getX()) >= Math.abs(y - l.getY())) {
            if (x - l.getX() < 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (y - l.getY() < 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }

    }

    private Direction getSecondaryDirectionTowards(Location l, Direction pd) {
        if (pd == Direction.DOWN || pd == Direction.UP) {
            if (x - l.getX() < 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (y - l.getY() < 0) {
                return Direction.DOWN;
            } else {
                return Direction.UP;
            }
        }
    }

    private boolean inRoom() {
        return map.isInsideRoom(new Location(x, y));
    }

    private void pickTarget() {
        ArrayList<Location> ls = map.insideWhichRoom(new Location(x, y)).getCorridorStarts();
        if (ls.size() == 1) {
            this.movingTowards = ls.get(0);
            return;
        }
        Random r = new Random();
        int i = 0;
        while (i < 1000000) {
            i++;
            Location l = ls.get(r.nextInt(ls.size()));
            if (!l.equals(new Location(x, y).locInDir(lastMoved.getOpposite()))) {
                this.movingTowards = l;
                return;
            }
            if (i == 1000000) {
                this.movingTowards = new Location(0, 0);
            }
        }
    }

}
