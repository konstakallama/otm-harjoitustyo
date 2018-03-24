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

public class Main extends Application {

    int counter = 0;

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Canvas canvas = new Canvas(500, 500);
        GraphicsContext drawer = canvas.getGraphicsContext2D();

        BorderPane pane = new BorderPane();

        pane.setCenter(canvas);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);

        MapGenerator m = new MapGenerator();
        Map map = m.createMap(50, 50);

        Player p = new Player(map, 10, 4, null, null);
        map.setPlayer(p);

        addEnemy(map);

        drawMap(drawer, map);

        scene.setOnKeyPressed((event) -> {

            if (event.getCode().equals(KeyCode.UP)) {
                if (!p.move(Direction.UP)) {
                    if (p.attack(Direction.UP)) {
                        counter++;
                    }
                } else {
                    counter++;
                }

            } else if (event.getCode() == KeyCode.DOWN) {
                if (!p.move(Direction.DOWN)) {
                    if (p.attack(Direction.DOWN)) {
                        counter++;
                    }
                } else {
                    counter++;
                }

            } else if (event.getCode() == KeyCode.RIGHT) {
                if (!p.move(Direction.RIGHT)) {
                    if (p.attack(Direction.RIGHT)) {
                        counter++;
                    }
                }else {
                    counter++;
                }

            } else if (event.getCode() == KeyCode.LEFT) {
                if (!p.move(Direction.LEFT)) {
                    if (p.attack(Direction.LEFT)) {
                        counter++;
                    }
                } else {
                    counter++;
                }

            }
            if (counter == 15) {
                addEnemy(map);
                counter = 0;
            }
            drawMap(drawer, map);
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

        while (map.getTerrain(x, y).isOccupied()) {
            x = random.nextInt(50);
            y = random.nextInt(50);
        }

        Enemy e = new Enemy(x, y, null, map, new EnemyStats(2, 2, 2, 2, 2, null, null, null, 0), true);
        map.addEnemy(x, y, e);
    }

}
