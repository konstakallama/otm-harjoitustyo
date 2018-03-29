/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.roguelike;

/**
 *
 * @author konstakallama
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.util.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Main extends Application {

    int counter = 0;
    static int kills = 0;
    static int enemiesCreated = 0;
    Label log1 = new Label();
    Label log2 = new Label();
    Label log3 = new Label();
    Label log4 = new Label();

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Canvas canvas = new Canvas(500, 500);
        GraphicsContext drawer = canvas.getGraphicsContext2D();

        GridPane upperGrid = new GridPane();
        Label hp = new Label();
        upperGrid.add(hp, 0, 0);
        Label enemiesKilled = new Label();
        GridPane logGrid = new GridPane();
        logGrid.add(log1, 0, 3);
        logGrid.add(log2, 0, 2);
        logGrid.add(log3, 0, 1);
        logGrid.add(log4, 0, 0);
        updateKills(enemiesKilled);

        upperGrid.add(enemiesKilled, 2, 0);

        BorderPane framework = new BorderPane();

        framework.setCenter(canvas);
        framework.setTop(upperGrid);
        framework.setBottom(logGrid);

        Scene scene = new Scene(framework);
        primaryStage.setScene(scene);

        MapGenerator m = new MapGenerator();
        Map map = m.createMap(50, 50);

        Player p = new Player(map, 10, 4, new PlayerStats(1, 1, 1, 1, 1, null, null), null);
        map.setPlayer(p);
        updateHP(hp, p);

        addEnemy(map);

        drawMap(drawer, map);

        scene.setOnKeyPressed((event) -> {
            boolean turnTaken = false;

            if (event.getCode().equals(KeyCode.UP)) {
                if (!p.move(Direction.UP)) {
                    if (parseAttackResult(p.attack(Direction.UP))) {
                        turnTaken = true;
                    }
                } else {
                    turnTaken = true;
                }

            } else if (event.getCode() == KeyCode.DOWN) {
                if (!p.move(Direction.DOWN)) {
                    if (parseAttackResult(p.attack(Direction.DOWN))) {
                        turnTaken = true;
                    }
                } else {
                    turnTaken = true;
                }

            } else if (event.getCode() == KeyCode.RIGHT) {
                if (!p.move(Direction.RIGHT)) {
                    if (parseAttackResult(p.attack(Direction.RIGHT))) {
                        turnTaken = true;
                    }
                } else {
                    turnTaken = true;
                }

            } else if (event.getCode() == KeyCode.LEFT) {
                if (!p.move(Direction.LEFT)) {
                    if (parseAttackResult(p.attack(Direction.LEFT))) {
                        turnTaken = true;
                    }
                } else {
                    turnTaken = true;
                }

            } else if (event.getCode() == KeyCode.SPACE) {
                turnTaken = true;
            }

            if (turnTaken) {
                ArrayList<AttackResult> results = map.takeTurns();
                for (AttackResult result : results) {
                    parseAttackResult(result);
                }
                counter++;
                updateKills(enemiesKilled);
                updateHP(hp, p);
                drawMap(drawer, map);
            }

            if (counter == 15) {
                addEnemy(map);
                counter = 0;
            }

        });

        primaryStage.show();
    }

    public static void drawMap(GraphicsContext drawer, Map map) {
        for (int i = 0; i < map.getEnemies().length; i++) {
            for (int j = 0; j < map.getEnemies()[0].length; j++) {
                if (map.getTerrain(i, j) == Terrain.WALL) {
                    paintTile(i * 10, j * 10, "Black", drawer);
                } else if (map.getTerrain(i, j) == Terrain.FLOOR) {
                    paintTile(i * 10, j * 10, "White", drawer);
                } else if (map.getTerrain(i, j) == Terrain.STAIRS) {
                    paintTile(i * 10, j * 10, "Yellow", drawer);
                }
                if (map.getEnemy(i, j) != null) {
                    paintTile(i * 10, j * 10, "Red", drawer);
                } else if (map.getItem(i, j) != null) {
                    paintTile(i * 10, j * 10, "Blue", drawer);
                }
            }
        }
        paintTile(map.getPlayer().getX() * 10, map.getPlayer().getY() * 10, "Green", drawer);

    }

    private static void paintTile(int x, int y, String color, GraphicsContext drawer) {
        drawer.setFill(Paint.valueOf(color));
        drawer.fillRect(x, y, 10, 10);
    }

    public static void addEnemy(Map map) {
        Random random = new Random();

        int x = random.nextInt(50);
        int y = random.nextInt(50);

        while (map.isOccupied(x, y)) {
            x = random.nextInt(50);
            y = random.nextInt(50);
        }

        Enemy e = new Enemy(x, y, new EnemyType("test enemy " + enemiesCreated), map, new EnemyStats(2, 2, 2, 2, 2, null, null, null, 0), true);
        enemiesCreated++;
        map.addEnemy(x, y, e);
    }

    private void updateHP(Label hp, Player p) {
        hp.setText("HP: " + p.getCurrentHP() + "/" + p.getMaxHP() + "   ");
    }

    private void updateKills(Label enemiesKilled) {
        enemiesKilled.setText("Enemies Killed: " + kills);
    }

    private boolean parsePlayerAttackResult(AttackResult result) {
        if (result.getType() == AttackResultType.HIT) {
            updateLog("You hit the " + result.getTarget().getName() + " dealing " + result.getDamageDealt() + " damage.");
        }
        if (result.getType() == AttackResultType.MISS) {
            updateLog("You miss the " + result.getTarget().getName() + ".");
        }
        if (result.getType() == AttackResultType.KILL) {
            updateLog("You kill the " + result.getTarget().getName() + ".");
            kills++;
        }
        return true;
    }

    private boolean parseAttackResult(AttackResult result) {
        if (result.getType() == AttackResultType.FAIL) {
            return false;
        }
        if (result.getAttacker().isEnemy()) {
            return parseEnemyAttackResult(result);
        } else {
            return parsePlayerAttackResult(result);
        }
    }

    private boolean parseEnemyAttackResult(AttackResult result) {
        if (result.getType() == AttackResultType.HIT) {
            updateLog("The " + result.getAttacker().getName() + " attacks you and deals " + result.getDamageDealt() + " damage.");
        }
        if (result.getType() == AttackResultType.MISS) {
            updateLog("The " + result.getAttacker().getName() + " misses you.");
        }
        if (result.getType() == AttackResultType.KILL) {
            updateLog("The " + result.getAttacker().getName() + " attacks and kills you.");
            gameOver();
        }
        return true;
    }

    private void gameOver() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void updateLog(String newMessage) {
        log4.setText(log3.getText());
        log3.setText(log2.getText());
        log2.setText(log1.getText());
        log1.setText(newMessage);
    }

}
