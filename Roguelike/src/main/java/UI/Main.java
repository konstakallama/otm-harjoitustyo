/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

/**
 *
 * @author konstakallama
 */
import domain.AttackResult;
import domain.AttackResultType;
import domain.CommandResult;
import domain.GameManager;
import domain.Inventory;
import domain.InventoryItem;
import domain.ItemDb;
import domain.ItemType;
import domain.Map;
import domain.Player;
import domain.PlayerCommand;
import domain.PlayerCommandParser;
import domain.PlayerCommandType;
import domain.PlayerStats;
import domain.Terrain;
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
import javafx.scene.layout.VBox;

public class Main extends Application {

    ArrayList<Label> logs = new ArrayList<>();

    Label enemiesKilled = new Label();
    Label turnCounter = new Label();
    Label hp = new Label();
    Label floor = new Label();
    ItemDb itemDb = new ItemDb();
    Button backToMap = new Button("Open Inventory");
    Canvas mapCanvas;
    GraphicsContext drawer;
    BorderPane framework = new BorderPane();
    Button menu = new Button("Menu");
    UIStatus status = UIStatus.MAP;

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

        this.defaultButtonEvents();

        gm.addEnemy();

        drawMap(drawer, gm.getMap());

        scene.setOnKeyPressed((event) -> {

            if (!(this.status == UIStatus.GAME_OVER)) {
                if (this.status == UIStatus.MAP) {
                    this.parseCommandResults(gm.playCommand(this.playerCommandParser.parseCommand(event.getCode(), gm)));
                    if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.INVENTORY) {
                        this.updateLog("Opening the inventory...");
                        this.inventorySreen();
                    } else if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.MENU) {
                        this.menuButtonEvent();
                    }
                } else if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.CLOSE) {
                    this.closeMenu();
                }

                this.updateUpperGrid(gm.getPlayer());
                drawMap(drawer, gm.getMap());
            }

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
        this.status = UIStatus.GAME_OVER;

        GridPane grid = new GridPane();

        Label goText = new Label("Game Over");
        goText.setAlignment(Pos.CENTER);

        Label detailedMessage = new Label(getGameOverMessage(result));
        detailedMessage.setAlignment(Pos.CENTER);

        grid.add(goText, 0, 0);
        grid.add(detailedMessage, 0, 1);

        grid.setAlignment(Pos.CENTER);
        grid.setPrefSize(gm.getMapW() * pixelSize, gm.getMapH() * pixelSize);

        menu.setDisable(true);

        this.framework.setCenter(grid);
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
        box.getChildren().addAll(enemiesKilled, turnCounter, hp, floor, menu);
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

    private void inventorySreen() {
        this.status = UIStatus.MENU;

        GridPane grid = new GridPane();

        this.addInventoryButtons(grid, framework);

        framework.setCenter(grid);
    }

    private void parseCommandResults(ArrayList<CommandResult> results) {
        for (CommandResult cr : results) {
            if (cr.isSuccess()) {
                if (cr.hasLogMessage()) {
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
        if (i.getItemType() == ItemType.CONSUMABLE) {
            if (i.getEffect().applyEffectToPlayer(gm.getPlayer())) {
                this.updateLog("You used the " + i.getName() + ".");
                this.updateUpperGrid(gm.getPlayer());
                gm.getPlayer().getInventory().removeItem(i);
//                framework.setCenter(mapCanvas);
                this.closeMenu();
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

    private void backToMapEvent() {
        backToMap.setText("Back to map");
        this.inventorySreen();
        backToMap.setOnMouseClicked((event) -> {
            backToMap.setText("Open Inventory");
            this.closeMenu();
            backToMap.setOnMouseClicked((event2) -> {
                this.backToMapEvent();
            });
        });
    }

    private void defaultButtonEvents() {
        backToMap.setText("Open Inventory");
        backToMap.setOnMouseClicked((event) -> {
            this.backToMapEvent();
        });

        menu.setText("Menu");
        menu.setOnMouseClicked((event) -> {
            this.menuButtonEvent();
        });
    }

    private void openMenu() {
        VBox menuIndex = this.createMenuButtons();
        this.framework.setLeft(menuIndex);
        this.mapCanvas.setVisible(false);
        this.status = UIStatus.MENU;
    }

    private VBox createMenuButtons() {
        Button stats = new Button("Stats");
        Button inventory = new Button("Inventory");

        inventory.setOnMouseClicked((event) -> {
            this.inventorySreen();
        });

        stats.setOnMouseClicked((event) -> {
            this.statScreen();
        });

        VBox box = new VBox();
        box.getChildren().addAll(stats, inventory);
        return box;
    }

    private void statScreen() {
        VBox box = new VBox();
        Label name = new Label(gm.getPlayer().getName());
        Label level = new Label("Level " + gm.getPlayer().getStats().getLevel());
        Label statHP = new Label("HP: " + gm.getPlayer().getCurrentHP() + "/" + gm.getPlayer().getMaxHP());
        Label str = new Label("Strength: " + gm.getPlayer().getStats().getStr());
        Label con = new Label("Constitution: " + gm.getPlayer().getStats().getCon());
        Label intel = new Label("Intelligence: " + gm.getPlayer().getStats().getIntel());
        Label dex = new Label("Dexterity: " + gm.getPlayer().getStats().getDex());
        Label exp = new Label("Exp. points: " + gm.getPlayer().getStats().getExp());
        Label toNextLevel = new Label("To next level: " + gm.getPlayer().getStats().expToNextLevel());

        Label space1 = new Label("");
        Label space2 = new Label("");

        box.getChildren().addAll(name, level, statHP, space1, str, con, intel, dex, space2, exp, toNextLevel);
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);
        framework.setCenter(box);

    }

    private void menuButtonEvent() {
        this.openMenu();
        menu.setText("Close");
        menu.setOnMouseClicked((event) -> {
            this.closeMenu();
            menu.setOnMouseClicked((event2) -> {
                menu.setText("Menu");
                this.menuButtonEvent();
            });
        });
    }

    private void closeMenu() {
        this.status = UIStatus.MAP;
        this.mapCanvas.setVisible(true);
        this.framework.setLeft(null);
        this.defaultButtonEvents();
        this.framework.setCenter(mapCanvas);
    }

}
