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

    public Enemy(int x, int y, Map map, EnemyStats stats, boolean visible) {
        super(map, x, y, stats.getType().getName());
        this.stats = stats;
        this.visible = visible;
        this.hasMoved = false;
        this.lastMoved = randomDirection();
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
        if (seesPlayer()) {
            if (map.isOccupied(x + pd.xVal(), y + pd.yVal())) {
                this.randomMove();
            } else {
                this.move(pd);
            }
        } else if (nextToCorridor()) {
            System.out.println("*");
            this.move(this.corridorDir());
        } else if (map.isOccupied(x + this.lastMoved.xVal(), y + this.lastMoved.yVal())) {
            if (inCorner()) {
                moveInCorner();
            } else {
                randomTurn();
            }
        } else {
            this.move(this.lastMoved);
        }

    }

    private boolean inCorridor(int x, int y) {
        return (map.isOccupied(x + 1, y) && map.isOccupied(x - 1, y) && !map.isOccupied(x, y + 1) && !map.isOccupied(x, y - 1)) ||
                (map.isOccupied(x, y + 1) && map.isOccupied(x, y - 1) && !map.isOccupied(x + 1, y) && !map.isOccupied(x - 1, y));
    }

    private boolean seesPlayer() {
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
        int visionRange = 100;
        int k = 0;

        while (!q.isEmpty() && k < visionRange) {
            Location l = q.poll();

            if (map.hasPlayer(l.getX(), l.getY())) {
                return true;
            }

            Direction d = Direction.UP;

            for (int i = 0; i < 4; i++) {
                Location ld = l.locInDir(d);
                if (ld.getX() >= 0 && ld.getY() >= 0) {
                    if (!visited[ld.getX()][ld.getY()]) {
                        visited[ld.getX()][ld.getY()] = true;
                        if (map.hasPlayer(ld.getX(), ld.getY())) {
                            return true;
                        }
                        if (!map.isOccupied(ld.getX(), ld.getY())) {
                            q.add(ld);
                        }
                    }
                }

                d = d.getClockwiseTurn();
            }
            k++;
        }

        return false;
    }

}
