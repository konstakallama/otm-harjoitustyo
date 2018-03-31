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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Main extends Application {
    
    ArrayList<Label> logs = new ArrayList<>();
      
    Label enemiesKilled = new Label();
    Label turnCounter = new Label();
    Label hp = new Label();
    Label floor = new Label();
    ItemDb itemDb = new ItemDb();
    
    static int pixelSize = 14;

    GameManager gm;
    PlayerCommandParser playerCommandParser = new PlayerCommandParser();

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        this.gm = new GameManager(this.createPlayerStats(), this.createInventory());
              
        Canvas canvas = new Canvas(gm.getMapW() * this.pixelSize, gm.getMapH() * this.pixelSize);
        GraphicsContext drawer = canvas.getGraphicsContext2D();

        GridPane upperGrid = new GridPane();
        addUpperLabels(upperGrid);

        GridPane logGrid = new GridPane();
        
        this.createLogs(4);
        addLogs(logGrid);

        BorderPane framework = new BorderPane();

        framework.setCenter(canvas);
        framework.setTop(upperGrid);
        framework.setBottom(logGrid);

        Scene scene = new Scene(framework);
        primaryStage.setScene(scene);
        
        updateUpperGrid(gm.getPlayer());

        gm.addEnemy();

        drawMap(drawer, gm.getMap());

        scene.setOnKeyPressed((event) -> {

            ArrayList<CommandResult> results = gm.playCommand(this.playerCommandParser.parseCommand(event.getCode(), gm));

            for (CommandResult cr : results) {
                if (cr.isSuccess()) {
                    if (cr.hasLogMessage) {
                        this.updateLog(cr.getLogMessage());
                        if (cr.getAttackResult().getAttacker().isEnemy() && cr.getAttackResult().getType() == AttackResultType.KILL) {
                            gameOver(primaryStage, cr.getAttackResult());
                            break;
                        }
                    }
                }
            }

            this.updateUpperGrid(gm.getPlayer());
            drawMap(drawer, gm.getMap());

        });

        primaryStage.show();
    }

    public static void drawMap(GraphicsContext drawer, Map map) {
        for (int i = 0; i < map.getEnemies().length; i++) {
            for (int j = 0; j < map.getEnemies()[0].length; j++) {
                if (map.getTerrain(i, j) == Terrain.WALL) {
                    paintTile(i * pixelSize, j * pixelSize, "Black", drawer);
                } else if (map.getTerrain(i, j) == Terrain.FLOOR) {
                    paintTile(i * pixelSize, j * pixelSize, "White", drawer);
                } else if (map.getTerrain(i, j) == Terrain.STAIRS) {
                    paintTile(i * pixelSize, j * pixelSize, "Yellow", drawer);
                }
                if (map.getEnemy(i, j) != null) {
                    paintTile(i * pixelSize, j * pixelSize, "Red", drawer);
                } else if (map.getItem(i, j) != null) {
                    paintTile(i * pixelSize, j * pixelSize, "Blue", drawer);
                }
            }
        }
        paintTile(map.getPlayer().getX() * pixelSize, map.getPlayer().getY() * pixelSize, "Green", drawer);

    }

    private static void paintTile(int x, int y, String color, GraphicsContext drawer) {
        drawer.setFill(Paint.valueOf(color));
        drawer.fillRect(x, y, pixelSize, pixelSize);
    }

    private void updateHP(Label hp, Player p) {
        hp.setText("HP: " + p.getCurrentHP() + "/" + p.getMaxHP() + "   ");
    }

    private void updateKills(Label enemiesKilled) {
        enemiesKilled.setText("Enemies Killed: " + gm.getGmStats().getEnemiesKilled() + "   ");
    }

    private void gameOver(Stage stage, AttackResult result) {
        GridPane grid = new GridPane();

        Label goText = new Label("Game Over");
        goText.setAlignment(Pos.CENTER);

        Label detailedMessage = new Label(getGameOverMessage(result));
        detailedMessage.setAlignment(Pos.CENTER);

        grid.add(goText, 0, 0);
        grid.add(detailedMessage, 0, 1);

        grid.setAlignment(Pos.CENTER);
        grid.setPrefSize(gm.getMapW() * pixelSize, gm.getMapH() * pixelSize);

        Scene gameOverScreen = new Scene(grid);
        stage.setScene(gameOverScreen);
        stage.show();
    }

    private void updateLog(String newMessage) {
        for (int i = 0; i < logs.size() - 1; i++) {
            logs.get(i).setText(logs.get(i + 1).getText());
        }
        logs.get(logs.size() - 1).setText(newMessage);
    }

    private void updateUpperGrid(Player p) {
        updateKills(enemiesKilled);
        updateHP(hp, p);
        updateTurns();
        updateFloor();
    }

    private void updateTurns() {
        turnCounter.setText("Turns: " + gm.getGmStats().getTurns() + "   ");
    }
    
    private void updateFloor() {
        floor.setText("Floor: " + gm.getMap().getFloor());
    }

    private void addUpperLabels(GridPane upperGrid) {
        upperGrid.add(enemiesKilled, 1, 0);
        upperGrid.add(turnCounter, 2, 0);
        upperGrid.add(hp, 0, 0);
        upperGrid.add(floor, 3, 0);

    }
    
    private void createLogs(int amount) {
        for (int i = 0; i < amount; i++) {
            Label log = new Label();
            logs.add(log);
        }
    }

    private void addLogs(GridPane logGrid) {
        for (int i = 0; i < logs.size(); i++) {
            logGrid.add(logs.get(i), 0, i);
        }
    }

    private String getGameOverMessage(AttackResult result) {
        return "You were killed on turn " + gm.getGmStats().getTurns() + " on floor " + gm.getMap().getFloor() + " by " + result.getAttacker().getName() + ".";
    }

    private PlayerStats createPlayerStats() {
        return this.createTestPlayerStats();
    }

    private PlayerStats createTestPlayerStats() {
        return new PlayerStats(1, 1, 1, 1, 1, itemDb.createTestWeapon(), itemDb.createTestArmor());
    }

    private Inventory createInventory() {
        return this.createTestInventory();
    }

    private Inventory createTestInventory() {
        return new Inventory(10);
    }

}
