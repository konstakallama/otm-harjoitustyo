/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

/**
 *
 * @author konstakallama
 */
import domain.gamemanager.AttackResult;
import domain.gamemanager.AttackResultType;
import domain.gamemanager.CommandResult;
import domain.gamemanager.GameManager;
import domain.mapobject.Inventory;
import domain.items.InventoryItem;
import domain.items.ItemDb;
import domain.items.ItemType;
import domain.map.Map;
import domain.mapobject.Player;
import domain.gamemanager.PlayerCommand;
import domain.gamemanager.PlayerCommandParser;
import domain.gamemanager.PlayerCommandType;
import domain.items.MapItem;
import domain.map.Room;
import domain.mapobject.PlayerStats;
import domain.map.Terrain;
import domain.map.VisibilityStatus;
import domain.support.Location;
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
    Label stamina = new Label();
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

        this.createLogs(5);
        addLogs(logGrid);

        framework.setCenter(mapCanvas);
        framework.setTop(upperBox);
        framework.setBottom(logGrid);

        Scene scene = new Scene(framework);
        primaryStage.setScene(scene);

        updateUpperGrid();

        this.defaultButtonEvents();

        gm.addEnemy();

        drawMap(drawer, gm.getMap());

        scene.setOnKeyPressed((event) -> {

            if (!(this.status == UIStatus.GAME_OVER)) {
                if (this.status == UIStatus.MAP) {
                    ArrayList<CommandResult> results = gm.playCommand(this.playerCommandParser.parseCommand(event.getCode(), gm));
                    this.parseCommandResults(results);
                    if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.INVENTORY) {
                        this.updateLog("Opening the inventory...");
                        this.inventorySreen();
                    } else if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.MENU) {
                        this.menuButtonEvent();
                    } else if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.PICK_UP) {
                        this.pickUp();
                    }

                    if (results != null) {
                        if (results.size() > 0) {
                            if (results.get(0).getAttackResult() != null) {
                                if (results.get(0).getAttackResult().isLevelUp()) {
                                    this.levelUpScreen();
                                }

                            }

                            if (gm.getPlayer().getStats().getStamina() == 0) {
                                this.updateLog("You are starving! You will take 1 damage each turn unless you eat something.");
                                if (gm.getPlayer().getStats().isDead()) {
                                    this.gameOver(this.starveGameOverMessage());
                                }
                            }
                        }
                    }

                } else if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.CLOSE && this.status == UIStatus.MENU) {
                    this.closeMenu();
                }

                this.updateUpperGrid();
                drawMap(drawer, gm.getMap());
            }

        });

        primaryStage.show();
    }

    public void drawMap(GraphicsContext drawer, Map map) {
        for (int i = 0; i < map.getEnemies().length; i++) {
            for (int j = 0; j < map.getEnemies()[0].length; j++) {
                if (map.getTerrain(i, j) == Terrain.WALL || map.getVisibility(i, j) == VisibilityStatus.UNKNOWN) {
                    paintTile(i * pixelSize, j * pixelSize, "Black", drawer);
                } else if (map.getVisibility(i, j) == VisibilityStatus.KNOWN) {
                    paintTile(i * pixelSize, j * pixelSize, "gray", drawer);
                } else if (map.getTerrain(i, j) == Terrain.FLOOR) {
                    paintTile(i * pixelSize, j * pixelSize, "White", drawer);
                } else if (map.getTerrain(i, j) == Terrain.CORRIDOR) {
                    paintTile(i * pixelSize, j * pixelSize, "White", drawer);
                } else if (map.getTerrain(i, j) == Terrain.STAIRS) {
                    paintTile(i * pixelSize, j * pixelSize, "Yellow", drawer);
                }

                if (map.getVisibility(i, j) == VisibilityStatus.IN_RANGE) {
                    if (map.getEnemy(i, j) != null) {
                        paintTile(i * pixelSize, j * pixelSize, "Red", drawer);
                    } else if (map.getItem(i, j) != null) {
                        paintTile(i * pixelSize, j * pixelSize, "#00FFFF", drawer);
                    }
                }

            }
        }
        paintTile(map.getPlayer().getX() * pixelSize, map.getPlayer().getY() * pixelSize, "#64FE2E", drawer);

//        drawCorridorStarts(drawer, map);
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

    private void gameOver(String msg) {
        this.status = UIStatus.GAME_OVER;

        GridPane grid = new GridPane();

        Label goText = new Label("Game Over");
        goText.setAlignment(Pos.CENTER);

        Label detailedMessage = new Label(msg);
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

    private void updateUpperGrid() {
        updateKills(enemiesKilled);
        updateHP(hp, gm.getPlayer());
        updateTurns();
        updateFloor();
        updateStamina();
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
        box.getChildren().addAll(enemiesKilled, turnCounter, hp, floor, menu, stamina);
        box.setSpacing(7);
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

    public PlayerStats createTestPlayerStats() {
        return new PlayerStats(1, 1, 1, 1, 1, itemDb.createTestWeapon(), itemDb.createTestArmor());
    }

    private Inventory createInventory() {
        return this.createTestInventory();
    }

    public Inventory createTestInventory() {
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
                        gameOver(this.getGameOverMessage(cr.getAttackResult()));
                        break;
                    } else if (gm.getPlayer().getCurrentHP() <= 0 && gm.getPlayer().getStats().getStamina() <= 0) {
                        gameOver(starveGameOverMessage());
                    }
                }
            }
        }
        this.updateUpperGrid();
        drawMap(drawer, gm.getMap());
    }

    private void getInventoryButtonPressed(InventoryItem i, BorderPane framework) {
        if (i.getItemType() == ItemType.CONSUMABLE) {
            if (i.getEffect().applyEffectToPlayer(gm.getPlayer())) {
                this.updateLog("You used the " + i.getName() + ".");
                this.updateUpperGrid();
                gm.getPlayer().getInventory().removeItem(i);
                this.closeMenu();
                this.parseCommandResults(gm.playCommand(new PlayerCommand(PlayerCommandType.WAIT)));
            } else {
                this.updateLog("You can't use that now.");
            }

        } else if (i.getItemType() == ItemType.WEAPON || i.getItemType() == ItemType.ARMOR) {
            if (gm.getPlayer().equip(i)) {
                this.updateLog("You equipped the " + i.getName() + ".");
                this.updateUpperGrid();
                this.closeMenu();
            } else {
                this.updateLog("You cannot equip that now.");
            }

        }
    }

    private void addInventoryButtons(GridPane grid, BorderPane framework) {
        int columnIndex = 0;
        int rowIndex = 0;
        Label help = new Label("");
        help.setMinHeight(200);

        for (InventoryItem i : gm.getPlayer().getInventory().getItems()) {
            Button b = new Button(i.getName());
            b.setMinWidth(150);

            b.setOnMouseClicked((event) -> {
                getInventoryButtonPressed(i, framework);
            });

            b.setOnMouseMoved((event) -> {
                help.setText(i.getDescription());
            });

            grid.add(b, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex == 4) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        rowIndex += 2;
        grid.add(help, 0, rowIndex);

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
        Button discard = new Button("Discard items");

        stats.setMinWidth(100);
        inventory.setMinWidth(100);
        discard.setMinWidth(100);

        inventory.setOnMouseClicked((event) -> {
            this.inventorySreen();
        });

        stats.setOnMouseClicked((event) -> {
            this.statScreen();
        });

        discard.setOnMouseClicked((event) -> {
            this.discardScreen();
        });

        VBox box = new VBox();
        box.getChildren().addAll(stats, inventory, discard);
        return box;
    }

    private void statScreen() {
        VBox box = new VBox();
        Label name = new Label(gm.getPlayer().getName());
        Label level = new Label("Level " + gm.getPlayer().getStats().getLevel());
        Label weapon = new Label("Equipped Weapon: " + gm.getPlayer().getStats().getWeapon().getName());
        Label armor = new Label("Equipped Armor: " + gm.getPlayer().getStats().getArmor().getName());
        Label statHP = new Label("HP: " + gm.getPlayer().getCurrentHP() + "/" + gm.getPlayer().getMaxHP());
        Label stamina = new Label("Stamina: " + gm.getPlayer().getStats().getStamina() + "/" + gm.getPlayer().getStats().getMaxStamina());
        Label str = new Label("Strength: " + gm.getPlayer().getStats().getStr());
        Label con = new Label("Constitution: " + gm.getPlayer().getStats().getCon());
        Label intel = new Label("Intelligence: " + gm.getPlayer().getStats().getIntel());
        Label dex = new Label("Dexterity: " + gm.getPlayer().getStats().getDex());
        Label exp = new Label("Exp. points: " + gm.getPlayer().getStats().getExp());
        Label toNextLevel = new Label("To next level: " + gm.getPlayer().getStats().expToNextLevel());
        Label help = new Label("");

        help.setMinHeight(200);

        name.setOnMouseClicked((event) -> {
            help.setText(nameHelp());
        });

        level.setOnMouseClicked((event) -> {
            help.setText(levelHelp());
        });

        weapon.setOnMouseClicked((event) -> {
            help.setText(gm.getPlayer().getStats().getWeapon().getDescription());
        });

        armor.setOnMouseClicked((event) -> {
            help.setText(gm.getPlayer().getStats().getArmor().getDescription());
        });

        statHP.setOnMouseClicked((event) -> {
            help.setText(hpHelp());
        });

        str.setOnMouseClicked((event) -> {
            help.setText(strHelp());
        });

        con.setOnMouseClicked((event) -> {
            help.setText(conHelp());
        });

        intel.setOnMouseClicked((event) -> {
            help.setText(intHelp());
        });

        dex.setOnMouseClicked((event) -> {
            help.setText(dexHelp());
        });

        exp.setOnMouseClicked((event) -> {
            help.setText(expHelp());
        });

        toNextLevel.setOnMouseClicked((event) -> {
            help.setText(nextLevelHelp());
        });

        stamina.setOnMouseClicked((event) -> {
            help.setText(staminaHelp());
        });

        Label space1 = new Label("");
        Label space2 = new Label("");
        Label space3 = new Label("");
        Label space4 = new Label("");
        Label space5 = new Label("");

        box.getChildren().addAll(name, level, statHP, stamina, space1, weapon, armor, space3, str, con, intel, dex, space2, exp, toNextLevel, space4, space5, help);
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);
        framework.setCenter(box);

    }

    private void menuButtonEvent() {
        if (this.status == UIStatus.MAP) {
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

    }

    private void closeMenu() {
        this.status = UIStatus.MAP;
        this.mapCanvas.setVisible(true);
        this.framework.setLeft(null);
        this.defaultButtonEvents();
        this.framework.setCenter(mapCanvas);
    }

    private void levelUpScreen() {
        this.status = UIStatus.LEVEL_UP;
        VBox box = new VBox();
        Label msg = new Label("You have leveled up! Choose a stat to increase:");
        Label space = new Label("");
        Button str = new Button("Strength (" + gm.getPlayer().getStats().getStr() + ")");
        Button con = new Button("Constitution (" + gm.getPlayer().getStats().getCon() + ")");
        Button intel = new Button("Intelligence (" + gm.getPlayer().getStats().getIntel() + ")");
        Button dex = new Button("Dexterity (" + gm.getPlayer().getStats().getDex() + ")");
        Label space2 = new Label("");
        Label help = new Label("");

        help.setMinHeight(50);

        str.setOnMouseClicked((event) -> {
            gm.getPlayer().getStats().increaseStr();
            this.updateUpperGrid();
            this.closeMenu();
        });

        con.setOnMouseClicked((event) -> {
            gm.getPlayer().getStats().increaseCon();
            this.updateUpperGrid();
            this.closeMenu();
        });

        intel.setOnMouseClicked((event) -> {
            gm.getPlayer().getStats().increaseInt();
            this.updateUpperGrid();
            this.closeMenu();
        });

        dex.setOnMouseClicked((event) -> {
            gm.getPlayer().getStats().increaseDex();
            this.updateUpperGrid();
            this.closeMenu();
        });

        str.setOnMouseMoved((event) -> {
            help.setText(strHelp());
        });

        con.setOnMouseMoved((event) -> {
            help.setText(conHelp());
        });

        intel.setOnMouseMoved((event) -> {
            help.setText(intHelp());
        });

        dex.setOnMouseMoved((event) -> {
            help.setText(dexHelp());
        });

        box.getChildren().addAll(msg, space, str, con, intel, dex, space2, help);
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);
        framework.setCenter(box);
    }

    private String strHelp() {
        return "Strength increases the amount of damage you deal with weapons.\nWeapons also require a certain amount of strength to be equipped.";
    }

    private String conHelp() {
        return "Constitution increases your maximum HP and stamina.";
    }

    private String intHelp() {
        return "Intelligence increases the damage you do with spells as well as the chance to hit with a spell.\nYou also gain new spellbook slots (up to 5) as your intelligence increases.";
    }

    private String dexHelp() {
        return "Dexterity increases your chance to hit with and evede attacks.";
    }

    private String nameHelp() {
        return "The name of your adventurer.";
    }

    private String levelHelp() {
        return "Your current level.\nYou gain 1 point in a stat of your choice at each level up and 1 point in each stat every 5 levels.";
    }

    private String hpHelp() {
        return "Your current and maximum hp. If your hp reaches 0, you die.";
    }

    private String expHelp() {
        return "Your current experience points.";
    }

    private String nextLevelHelp() {
        return "The amonut of experience you need to reach the next level.";
    }

    private String staminaHelp() {
        return "Stamina decreases by 1 each turn.\n"
                + "When it reaches 0, you lose 1 hp each turn until you starve to death.";
    }

    private void updateStamina() {
        stamina.setText("Stamina: " + gm.getPlayer().getStats().getStamina() + "/" + gm.getPlayer().getStats().getMaxStamina());
    }

    private void starveGameOver() {
    }

    private String starveGameOverMessage() {
        return "You starved to death on turn " + gm.getGmStats().getTurns() + " on floor " + gm.getMap().getFloor() + ".";
    }

    private void drawCorridorStarts(GraphicsContext drawer, Map map) {
        System.out.println("***");
        for (Room r : map.getRooms()) {
            System.out.println("r: " + r);
            for (Location l : r.getCorridorStarts()) {
                if (l != null) {
                    paintTile(l.getX() * pixelSize, l.getY() * pixelSize, "blue", drawer);
//                    System.out.println(l);
                }

            }
        }

    }

    private void discardScreen() {
        this.status = UIStatus.MENU;

        GridPane grid = new GridPane();

        this.addDiscardButtons(grid, framework);

        framework.setCenter(grid);
    }

    private void addDiscardButtons(GridPane grid, BorderPane framework) {
        int columnIndex = 0;
        int rowIndex = 0;
        Label help = new Label("");
        help.setMinHeight(200);

        for (InventoryItem i : gm.getPlayer().getInventory().getItems()) {
            Button b = new Button(i.getName());
            b.setMinWidth(150);

            b.setOnMouseClicked((event) -> {
                getDiscardButtonPressed(i, framework);
            });

            b.setOnMouseMoved((event) -> {
                help.setText(i.getDescription());
            });

            grid.add(b, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex == 4) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        rowIndex += 2;
        grid.add(help, 0, rowIndex);
    }

    private void getDiscardButtonPressed(InventoryItem i, BorderPane framework) {
        int x = gm.getPlayer().getX();
        int y = gm.getPlayer().getY();
        if (gm.getMap().getItem(x, y) == null) {
            gm.getMap().addItem(x, y, new MapItem(x, y, gm.getMap(), i.getName(), true));
            this.updateLog("You discarded the " + i.getName() + ".");
            gm.getPlayer().getInventory().removeItem(i);
            this.closeMenu();
        } else {
            this.updateLog("You are already standing on an item.");
        }
    }

    private void pickUp() {
        int x = gm.getPlayer().getX();
        int y = gm.getPlayer().getY();
        if (gm.getMap().getItem(x, y) != null) {
            CommandResult cr = gm.getMap().pickUp(x, y);
            this.updateLog(cr.getLogMessage());
        }
    }

}
