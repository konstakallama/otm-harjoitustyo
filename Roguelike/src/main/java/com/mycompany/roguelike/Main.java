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
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.util.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class Main extends Application {

    ArrayList<Label> logs = new ArrayList<>();

    Label enemiesKilled = new Label();
    Label turnCounter = new Label();
    Label hp = new Label();
    Label floor = new Label();
    ItemDb itemDb = new ItemDb();
    Button backToMap = new Button("Back to map");
    Canvas mapCanvas;
    GraphicsContext drawer;
    BorderPane framework = new BorderPane();

    static int pixelSize = 14;

    GameManager gm;
    PlayerCommandParser playerCommandParser = new PlayerCommandParser();

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.gm = new GameManager(this.createPlayerStats(), this.createInventory());

        mapCanvas = new Canvas(gm.getMapW() * this.pixelSize, gm.getMapH() * this.pixelSize);
        drawer = mapCanvas.getGraphicsContext2D();

        HBox upperBox = new HBox();
        this.addUpperLabelsToHBox(upperBox);

        GridPane logGrid = new GridPane();

        this.createLogs(4);
        addLogs(logGrid);

        framework.setCenter(mapCanvas);
        framework.setTop(upperBox);
        framework.setBottom(logGrid);

        Scene scene = new Scene(framework);
        primaryStage.setScene(scene);

        updateUpperGrid(gm.getPlayer());

        backToMap.setOnMouseClicked((event) -> {
            framework.setCenter(mapCanvas);
        });

        gm.addEnemy();

        drawMap(drawer, gm.getMap());

        scene.setOnKeyPressed((event) -> {

            this.parseCommandResults(gm.playCommand(this.playerCommandParser.parseCommand(event.getCode(), gm)));

            if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.INVENTORY) {
                this.updateLog("Opening the inventory...");
                this.inventorySreen(primaryStage, framework);
            }

            this.updateUpperGrid(gm.getPlayer());
            drawMap(drawer, gm.getMap());

        });

        primaryStage.show();
    }

    public void drawMap(GraphicsContext drawer, Map map) {
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
                    paintTile(i * pixelSize, j * pixelSize, "#00FFFF", drawer);
                }
            }
        }
        paintTile(map.getPlayer().getX() * pixelSize, map.getPlayer().getY() * pixelSize, "#64FE2E", drawer);

    }

    private void paintTile(int x, int y, String color, GraphicsContext drawer) {
        drawer.setFill(Paint.valueOf(color));
        drawer.fillRect(x, y, pixelSize, pixelSize);
    }

    private void updateHP(Label hp, Player p) {
        hp.setText("HP: " + p.getCurrentHP() + "/" + p.getMaxHP() + "   ");
    }

    private void updateKills(Label enemiesKilled) {
        enemiesKilled.setText("Enemies Killed: " + gm.getGmStats().getEnemiesKilled() + "   ");
    }

    private void gameOver(AttackResult result) {
        GridPane grid = new GridPane();

        Label goText = new Label("Game Over");
        goText.setAlignment(Pos.CENTER);

        Label detailedMessage = new Label(getGameOverMessage(result));
        detailedMessage.setAlignment(Pos.CENTER);

        grid.add(goText, 0, 0);
        grid.add(detailedMessage, 0, 1);

        grid.setAlignment(Pos.CENTER);
        grid.setPrefSize(gm.getMapW() * pixelSize, gm.getMapH() * pixelSize);
        
        this.framework.setCenter(grid);

//        Scene gameOverScreen = new Scene(grid);
//        stage.setScene(gameOverScreen);
//        stage.show();
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
        floor.setText("Floor: " + gm.getMap().getFloor() + "   ");
    }

    private void addUpperLabelsToGrid(GridPane upperGrid) {
        upperGrid.add(enemiesKilled, 1, 0);
        upperGrid.add(turnCounter, 2, 0);
        upperGrid.add(hp, 0, 0);
        upperGrid.add(floor, 3, 0);
        upperGrid.add(backToMap, 4, 0);
    }

    private void addUpperLabelsToHBox(HBox box) {
        box.getChildren().addAll(enemiesKilled, turnCounter, hp, floor, backToMap);
        box.setSpacing(10);
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

    private void inventorySreen(Stage stage, BorderPane framework) {
        GridPane grid = new GridPane();

        this.addInventoryButtons(grid, framework);

        framework.setCenter(grid);

//        Scene inventoryScreen = new Scene(grid);
//        stage.setScene(inventoryScreen);
//        stage.show();
    }

    private void parseCommandResults(ArrayList<CommandResult> results) {
        for (CommandResult cr : results) {
            if (cr.isSuccess()) {
                if (cr.hasLogMessage) {
                    this.updateLog(cr.getLogMessage());
                    if (cr.getAttackResult().getAttacker().isEnemy() && cr.getAttackResult().getType() == AttackResultType.KILL) {
                        gameOver(cr.getAttackResult());
                        break;
                    }
                }
            }
        }
        this.updateUpperGrid(gm.getPlayer());
        drawMap(drawer, gm.getMap());
    }

    private void getInventoryButtonPressed(InventoryItem i, BorderPane framework) {
        if (i.itemType == ItemType.CONSUMABLE) {
            if (i.getEffect().applyEffectToPlayer(gm.getPlayer())) {
                this.updateLog("You used the " + i.getName() + ".");
                this.updateUpperGrid(gm.getPlayer());
                gm.getPlayer().getInventory().removeItem(i);
                framework.setCenter(mapCanvas);
                this.parseCommandResults(gm.playCommand(new PlayerCommand(PlayerCommandType.WAIT)));
            } else {
                this.updateLog("You can't use that now.");
            }

        }
    }

    private void addInventoryButtons(GridPane grid, BorderPane framework) {
        int columnIndex = 0;
        int rowIndex = 0;

        for (InventoryItem i : gm.getPlayer().getInventory().getItems()) {
            Button b = new Button(i.getName());

            b.setOnMouseClicked((event) -> {
                getInventoryButtonPressed(i, framework);
            });

            grid.add(b, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex == 5) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

}
