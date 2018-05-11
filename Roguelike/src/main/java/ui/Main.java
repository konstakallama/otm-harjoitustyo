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
import dao.ScoreDao;
import domain.gamemanager.AttackResult;
import domain.gamemanager.AttackResultType;
import domain.gamemanager.CommandResult;
import domain.gamemanager.GameManager;
import domain.mapobject.player.Inventory;
import domain.items.InventoryItem;
import dao.ItemDb;
import domain.items.ItemType;
import domain.map.Map;
import domain.mapobject.player.Player;
import domain.gamemanager.PlayerCommand;
import domain.gamemanager.PlayerCommandType;
import domain.items.Armor;
import domain.items.MapItem;
import domain.items.Weapon;
import domain.items.WeaponType;
import domain.map.Room;
import domain.mapobject.player.PlayerStats;
import domain.map.Terrain;
import domain.map.VisibilityStatus;
import domain.mapobject.player.RangeType;
import domain.mapobject.player.Spell;
import dao.SpellDb;
import domain.support.Location;
import domain.support.MessageDb;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Main extends Application {

    int testCounter = 0;

    ArrayList<Label> logs = new ArrayList<>();
    ScoreDao sd = new ScoreDao("data/Scores.txt");

    Label level = new Label();
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
    MessageDb mdb = new MessageDb();
    Scene scene;
    HBox spellBox;

    static int pixelSize = 12;

    GameManager gm;
    PlayerCommandParser playerCommandParser = new PlayerCommandParser();

    Stage primaryStage;

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;

        this.mainMenu();

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
                        paintWeaponTypeBorder(i * pixelSize, j * pixelSize, map.getEnemy(i, j).getStats().getWeapon().getType());
                    } else if (map.getItem(i, j) != null) {
                        paintTile(i * pixelSize, j * pixelSize, "#00FFFF", drawer);
                    }
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

    private void updateLevel() {
        level.setText("Level: " + gm.getPlayer().getStats().getLevel() + "   ");
    }

    private void gameOver(String msg) {
        this.status = UIStatus.GAME_OVER;

        GridPane grid = new GridPane();

        Label goText = new Label("Game Over");
        goText.setAlignment(Pos.CENTER);

        Label detailedMessage = new Label(msg);
        detailedMessage.setAlignment(Pos.CENTER);

        Button backToMenu = new Button("Back to main menu");
        backToMenu.setOnMouseClicked((event) -> {
            this.mainMenu();
        });

        grid.add(goText, 0, 0);
        grid.add(detailedMessage, 0, 1);
        grid.add(new Label(""), 0, 2);
        grid.add(backToMenu, 0, 3);

        grid.setAlignment(Pos.CENTER);
        grid.setPrefSize(gm.getMapW() * pixelSize, gm.getMapH() * pixelSize);

        menu.setDisable(true);

        this.framework.setCenter(grid);
    }

    private void updateLog(String newMessage) {
        gm.getGmStats().addLog(newMessage);
        for (int i = 0; i < logs.size() - 1; i++) {
            logs.get(i).setText(logs.get(i + 1).getText());
        }
        logs.get(logs.size() - 1).setText(newMessage);
    }

    private void updateUpperGrid() {
        updateLevel();
        updateHP(hp, gm.getPlayer());
        updateTurns();
        updateFloor();
        updateStamina();

        this.updateSpellBox();
    }

    private void updateTurns() {
        turnCounter.setText("Turns: " + gm.getGmStats().getTurns() + "   ");
    }

    private void updateFloor() {
        floor.setText("Floor: " + gm.getMap().getFloor() + "   ");
    }

    private void addUpperLabelsToHBox(HBox box) {
        box.getChildren().addAll(level, turnCounter, hp, floor, menu, stamina);
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
        return "You were killed on turn " + gm.getGmStats().getTurns() + " on floor " + gm.getMap().getFloor() + " by a " + result.getAttacker() + ".";
    }

    private Inventory createInventory() {
        return this.createTestInventory();
    }

    public Inventory createTestInventory() {
        return new Inventory(20);
    }

    private void inventoryScreen() {
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
                        writeScore(cr.getAttackResult().getAttacker().getName());

                        gameOver(this.getGameOverMessage(cr.getAttackResult()));
                        break;
                    } else if (gm.getPlayer().getCurrentHP() <= 0 && gm.getPlayer().getStats().getCurrentStamina() <= 0) {
                        writeScore("hunger");

                        gameOver(starveGameOverMessage());
                        break;
                    }
                }
            }
        }
        this.updateUpperGrid();
        drawMap(drawer, gm.getMap());
    }

    private void getInventoryButtonPressed(InventoryItem i, BorderPane framework) {
        if (i.getItemType() == ItemType.CONSUMABLE) {

            CommandResult cr = i.getEffect().applyEffectToPlayer(gm.getPlayer());

            if (cr.isSuccess()) {
                this.updateLog(cr.getLogMessage());
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
        help.setMinHeight(300);

        for (InventoryItem i : gm.getPlayer().getInventory().getItems()) {
            Button b = new Button(i.getName());
            b.setMinWidth(200);

            b.setOnMouseClicked((event) -> {
                getInventoryButtonPressed(i, framework);
            });

            b.setOnMouseMoved((event) -> {
                help.setText(i.getDescription());
            });

            grid.add(b, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex == 2) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        rowIndex += 2;
        grid.add(help, 0, rowIndex);

    }

    private void backToMapEvent() {
        backToMap.setText("Back to map");
        this.inventoryScreen();
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
        Button spells = new Button("Spells");
        Button logHistory = new Button("Log history");

        stats.setMinWidth(105);
        inventory.setMinWidth(105);
        discard.setMinWidth(105);
        spells.setMinWidth(105);
        logHistory.setMinWidth(105);

        inventory.setOnMouseClicked((event) -> {
            this.inventoryScreen();
        });

        stats.setOnMouseClicked((event) -> {
            this.statScreen();
        });

        discard.setOnMouseClicked((event) -> {
            this.discardScreen();
        });

        spells.setOnMouseClicked((event) -> {
            this.spellScreen();
        });

        logHistory.setOnMouseClicked((event) -> {
            this.logHistoryScreen();
        });

        VBox box = new VBox();
        box.getChildren().addAll(stats, inventory, discard, spells, logHistory);
        return box;
    }

    private void statScreen() {
        VBox box = new VBox();
        Label name = new Label(gm.getPlayer().getName());
        Label level = new Label("Level " + gm.getPlayer().getStats().getLevel());
        Label weapon = new Label("Equipped Weapon: " + gm.getPlayer().getStats().getWeapon().getName());
        Label armor = new Label("Equipped Armor: " + gm.getPlayer().getStats().getArmor().getName());
        Label statHP = new Label("HP: " + gm.getPlayer().getCurrentHP() + "/" + gm.getPlayer().getMaxHP() + ". Wounded for " + gm.getPlayer().getStats().getWound() + " ponits.");
        Label stamina = new Label("Stamina: " + gm.getPlayer().getStats().getCurrentStamina() + "/" + gm.getPlayer().getStats().getMaxStamina());
        Label str = new Label("Strength: " + gm.getPlayer().getStats().getStr());
        Label con = new Label("Constitution: " + gm.getPlayer().getStats().getCon());
        Label intel = new Label("Intelligence: " + gm.getPlayer().getStats().getInt());
        Label dex = new Label("Dexterity: " + gm.getPlayer().getStats().getDex());
        Label exp = new Label("Exp. points: " + gm.getPlayer().getStats().getExp());
        Label toNextLevel = new Label("To next level: " + gm.getPlayer().getStats().expToNextLevel());
        Label inventorySize = new Label("Inventory size: " + gm.getPlayer().getInventory().getSize());
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

        inventorySize.setOnMouseClicked((event) -> {
            help.setText("The maximum amount of items you can carry.");
        });

        Label space1 = new Label("");
        Label space2 = new Label("");
        Label space3 = new Label("");
        Label space4 = new Label("");
        Label space5 = new Label("");
        Label space6 = new Label("");

        box.getChildren().addAll(name, level, statHP, stamina, space1, weapon, armor, space3, str, con, intel, dex, space2, exp, toNextLevel, space4, inventorySize, space6, space5, help);
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
        this.updateUpperGrid();
        scene.setOnKeyPressed((event) -> {
            controls(event);
        });
        mapCanvas.setOnMouseClicked((event) -> {
            clickOnEnemy(event);
        });
    }

    private void levelUpScreen() {
        this.status = UIStatus.LEVEL_UP;
        VBox box = new VBox();
        Label msg = new Label("You have leveled up! Choose a stat to increase:");
        Label space = new Label("");
        Button str = new Button("Strength (" + gm.getPlayer().getStats().getStr() + ")");
        Button con = new Button("Constitution (" + gm.getPlayer().getStats().getCon() + ")");
        Button intel = new Button("Intelligence (" + gm.getPlayer().getStats().getInt() + ")");
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
        return "Your current and maximum hp. If your hp reaches 0, you die.\n"
                + "Wound is the most recent damage you have suffered.\n"
                + "Any healing will set the wound to 0 and any new damage will replace the existing wound.";
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
        stamina.setText("Stamina: " + gm.getPlayer().getStats().getCurrentStamina() + "/" + gm.getPlayer().getStats().getMaxStamina());
    }

    private String starveGameOverMessage() {
        return "You starved to death on turn " + gm.getGmStats().getTurns() + " on floor " + gm.getMap().getFloor() + ".";
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
            if (columnIndex == 2) {
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

    private void updateSpellBox() {
        this.spellBox.getChildren().clear();
        Label l = new Label("Spells: ");
        this.spellBox.getChildren().add(l);
        for (Spell s : gm.getPlayer().getStats().getSpells()) {
            Button b = new Button(s.getName());
            b.setMinWidth(75);
            b.setOnMouseClicked((event) -> {
                getSpellButtonEvent(s);
            });

            if (!s.canBeUsed()) {
                b.setDisable(true);
                b.setText(s.getName() + "(" + s.getTurnsInCooldown() + ")");
            } else {
                b.setDisable(false);
            }

            this.spellBox.getChildren().add(b);
        }

        for (int i = 0; i < gm.getPlayer().getStats().getFreeSpellBookSlots(); i++) {
            Button b = new Button("(free slot)");
            b.setMinWidth(75);
            this.spellBox.getChildren().add(b);
        }
    }

    private void getSpellButtonEvent(Spell s) {
        if (this.status == UIStatus.MAP) {
            this.status = UIStatus.TARGETING;

            this.updateLog(mdb.getCastingSpellMsg());

            this.paintCastingRange(s);

            scene.setOnKeyPressed((event) -> {
                if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.CLOSE && this.status != UIStatus.GAME_OVER) {
                    this.closeMenu();
                    this.drawMap(drawer, gm.getMap());
                }
            });

            this.mapCanvas.setOnMouseClicked((event) -> {
                if (this.status == UIStatus.TARGETING) {
                    int x = (int) Math.floor(event.getX() / pixelSize);
                    int y = (int) Math.floor(event.getY() / pixelSize);
                    Location l = new Location(x, y);

                    if (gm.getMap().hasEnemy(x, y) && gm.getMap().getVisibility(x, y) == VisibilityStatus.IN_RANGE && s.inRange(gm.getPlayer().getLocation(), new Location(x, y))) {
                        CommandResult cr = s.useOnEnemy(gm.getMap().getEnemy(x, y));
                        if (cr.isSuccess()) {
                            this.updateLog(cr.getLogMessage());
                            if (cr.getAttackResult().isLevelUp()) {
                                this.levelUpScreen();
                            }
                            ArrayList<CommandResult> results = gm.playCommand(new PlayerCommand(PlayerCommandType.WAIT));
                            this.parseCommandResults(results);
                            if (this.status == UIStatus.TARGETING) {
                                this.closeMenu();
                            }

                        }
                    } else if (s.getRangeType() == RangeType.SELF && gm.getPlayer().getLocation().equals(l)) {
                        CommandResult cr = s.useOnPlayer(gm.getPlayer());
                        if (cr.isSuccess()) {
                            this.updateLog(cr.getLogMessage());
                            if (cr.getAttackResult().isLevelUp()) {
                                this.levelUpScreen();
                            }
                            ArrayList<CommandResult> results = gm.playCommand(new PlayerCommand(PlayerCommandType.WAIT));
                            this.parseCommandResults(results);
                            if (this.status == UIStatus.TARGETING) {
                                this.closeMenu();
                            }

                        }
                    }
                }

            });
        }

    }

    private void paintCastingRange(Spell s) {
        for (int i = 0; i < gm.getMapW(); i++) {
            for (int j = 0; j < gm.getMapH(); j++) {
                if (gm.getMap().getVisibility(i, j) == VisibilityStatus.IN_RANGE) {
                    if (s.inRange(gm.getPlayer().getLocation(), new Location(i, j))) {
                        if (gm.getMap().getTerrain(i, j) != Terrain.WALL) {
                            paintBox(i * pixelSize, j * pixelSize, "pink");
                        }
                    }
                }
            }
        }
    }

    private void paintBox(int x, int y, String color) {
        this.drawer.setStroke(Paint.valueOf(color));
        this.drawer.strokeRect(x, y, pixelSize, pixelSize);
    }

    private void controls(KeyEvent event) {
        if (!(this.status == UIStatus.GAME_OVER)) {
            if (this.status == UIStatus.MAP) {
                ArrayList<CommandResult> results = gm.playCommand(this.playerCommandParser.parseCommand(event.getCode(), gm));

                if (results != null) {
                    if (results.size() > 0) {
                        if (results.get(0).getAttackResult() != null) {
                            if (results.get(0).getAttackResult().isLevelUp()) {
                                this.levelUpScreen();
                            }
                        }
                    }
                }

                this.parseCommandResults(results);
                if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.INVENTORY) {
                    this.updateLog("Opening the inventory...");
                    this.inventoryScreen();
                } else if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.MENU) {
                    this.menuButtonEvent();
                } else if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.PICK_UP) {
                    this.pickUp();
                } else if (this.playerCommandParser.parseCommand(event.getCode(), gm).getType() == PlayerCommandType.DISCARD) {
                    this.discardScreen();
                }

                if (results != null) {
                    if (results.size() > 0) {
                        if (gm.getPlayer().getStats().getCurrentStamina() == 0) {
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

    }

    private void spellScreen() {
        Label l = new Label("Mouseover your spells to get detailed information about them.");
        this.framework.setCenter(l);

        SpellDb sdb = new SpellDb();

        for (int i = 1; i < this.spellBox.getChildren().size(); i++) {
            Button b = (Button) this.spellBox.getChildren().get(i);
            if (!b.getText().equals("(free slot)")) {
                b.setOnMouseMoved((event) -> {
                    l.setText(sdb.getHelp(b.getText()));
                });
            }
        }
    }

    private void logHistoryScreen() {
        Label l = new Label("Log History (most recent first):");
        for (int i = gm.getGmStats().getLogHistory().size() - 1; i >= 0; i--) {
            l.setText(l.getText() + "\n" + gm.getGmStats().getLogHistory().get(i));
        }
        framework.setCenter(l);
    }

    private void clickOnEnemy(MouseEvent event) {
        int x = (int) Math.floor(event.getX() / pixelSize);
        int y = (int) Math.floor(event.getY() / pixelSize);

        if (gm.getMap().hasEnemy(x, y)) {
            this.updateLog(mdb.getSeeEnemyMsg(gm.getMap().getEnemy(x, y), gm.getPlayer().getStats().getInt()) + ". " + gm.getMap().getEnemy(x, y).getStats().toString());
        }
    }

    private void paintWeaponTypeBorder(int x, int y, WeaponType type) {
        String color = "";
        if (type == WeaponType.SWORD) {
            color = "red";
        } else if (type == WeaponType.LANCE) {
            color = "blue";
        } else if (type == WeaponType.AXE) {
            color = "green";
        }
        paintThickBox(x, y, color, 2);
    }

    private void paintThickBox(int x, int y, String color, int thickness) {
        this.drawer.setStroke(Paint.valueOf(color));

        for (int i = 0; i < thickness; i++) {
            this.drawer.strokeRect(x + i, y + i, pixelSize - (i * 2), pixelSize - (i * 2));
        }
    }

    private void newGame(PlayerStats playerStats, String name) {
        try {
            this.scene.setRoot(new Label(""));
        } catch (Exception e) {

        }

        newGameInit();

        this.framework = new BorderPane();
        framework.setMinSize(50 * pixelSize, 60 * pixelSize);

        this.gm = new GameManager(playerStats, this.createInventory(), name);

        mapCanvas = new Canvas(gm.getMapW() * this.pixelSize, gm.getMapH() * this.pixelSize);
        drawer = mapCanvas.getGraphicsContext2D();

        HBox statusBox = new HBox();
        this.addUpperLabelsToHBox(statusBox);

        spellBox = new HBox();
        spellBox.setMinHeight(30);
        updateSpellBox();

        VBox upperGrid = new VBox();
        upperGrid.getChildren().addAll(statusBox, spellBox);

        GridPane logGrid = new GridPane();

        this.createLogs(5);
        addLogs(logGrid);

        updateUpperGrid();

        this.defaultButtonEvents();

        gm.addEnemy();

        SpellDb sdb = new SpellDb();

        this.updateSpellBox();

        drawMap(drawer, gm.getMap());

        framework.setCenter(mapCanvas);
        framework.setTop(upperGrid);
        framework.setBottom(logGrid);

        scene = new Scene(framework);
        primaryStage.setScene(scene);

        scene.setOnKeyPressed((event) -> {
            controls(event);
        });
        mapCanvas.setOnMouseClicked((event) -> {
            clickOnEnemy(event);
        });

    }

    private void mainMenu() {
        this.status = UIStatus.MAIN_MENU;
        VBox menuButtons = new VBox();

        Button newGame = new Button("New Game");
        newGame.setOnMouseClicked((event) -> {
            this.characterCreation();
        });

        Button scores = new Button("High Scores");
        scores.setOnMouseClicked((event) -> {
            scoreScreen();
        });

        newGame.setMinWidth(100);
        scores.setMinWidth(100);

        menuButtons.setMinSize(50 * pixelSize, 60 * pixelSize);
        menuButtons.setAlignment(Pos.CENTER);

        menuButtons.getChildren().add(newGame);
        menuButtons.getChildren().add(scores);

        Scene mainMenuScene = new Scene(menuButtons);

        primaryStage.setScene(mainMenuScene);
    }

    private void scoreScreen() {

        ArrayList<Score> l = sd.getScores();
        Collections.sort(l);

        VBox scores = new VBox();
        scores.setMinSize(50 * pixelSize, 60 * pixelSize);
        scores.setAlignment(Pos.CENTER);

        Label lb = new Label("High Scores:");
        scores.getChildren().add(lb);
        scores.getChildren().add(new Label(""));

        for (int i = 0; i < 10; i++) {
            if (i >= l.size()) {
                break;
            }

            Score c = l.get(i);

            String s = (i + 1) + ": ";

            s += "    Name: " + c.getName();
            s += ";    Floor: " + c.getFloor();
            s += ";    Turns: " + c.getTurn();
            s += ";    Player level: " + c.getLevel();
            s += ";    Killed by: " + c.getKilledBy();

            scores.getChildren().add(new Label(s));
        }

        if (l.isEmpty()) {
            lb.setText("No scores to display.");
        }

        scores.getChildren().add(new Label(""));
        scores.getChildren().add(new Label(""));

        Button backToMenu = new Button("Back to main menu");
        backToMenu.setOnMouseClicked((event) -> {

            this.mainMenu();
        });

        scores.getChildren().add(backToMenu);

        Scene scoreScene = new Scene(scores);

        primaryStage.setScene(scoreScene);
    }


    private void writeScore(String killedBy) {
        Score s = new Score(gm.getPlayer().getName(), gm.getMap().getFloor(), gm.getGmStats().getTurns(), gm.getPlayer().getStats().getLevel(), killedBy);
        sd.writeScore(s);
    }

    private void characterCreation() {
        CharCreationStats s = new CharCreationStats(6, 4);

        Label pointsLeft = new Label("Ponits left: " + s.getInitialPoints());
        Label help = new Label("Choose starting stats and a name for your character:");

        GridPane stats = new GridPane();
        Label str = new Label("Strength (1)");
        Label con = new Label("Constitution (1)");
        Label intel = new Label("Intelligence (1)");
        Label dex = new Label("Dexterity (1)");

        Button strP = new Button("+");
        Button conP = new Button("+");
        Button intP = new Button("+");
        Button dexP = new Button("+");

        strP.setOnMouseClicked((event) -> {
            s.increaseStr();
            str.setText("Strength (" + s.getStrength() + ")");
            pointsLeft.setText("Points left: " + (s.getInitialPoints() - s.getPointsUsed()));
        });

        conP.setOnMouseClicked((event) -> {
            s.increaseCon();
            con.setText("Constitution (" + s.getConnstitution() + ")");
            pointsLeft.setText("Points left: " + (s.getInitialPoints() - s.getPointsUsed()));

        });

        intP.setOnMouseClicked((event) -> {
            s.increaseInt();
            intel.setText("Intelligence (" + s.getIntelligence() + ")");
            pointsLeft.setText("Points left: " + (s.getInitialPoints() - s.getPointsUsed()));

        });

        dexP.setOnMouseClicked((event) -> {
            s.increaseDex();
            dex.setText("Dexterity (" + s.getDexterity() + ")");
            pointsLeft.setText("Points left: " + (s.getInitialPoints() - s.getPointsUsed()));

        });

        Button strM = new Button("-");
        Button conM = new Button("-");
        Button intM = new Button("-");
        Button dexM = new Button("-");

        strM.setOnMouseClicked((event) -> {
            s.decreaseStr();
            str.setText("Strength (" + s.getStrength() + ")");
            pointsLeft.setText("Points left: " + (s.getInitialPoints() - s.getPointsUsed()));

        });

        conM.setOnMouseClicked((event) -> {
            s.decreaseCon();
            con.setText("Constitution (" + s.getConnstitution() + ")");
            pointsLeft.setText("Points left: " + (s.getInitialPoints() - s.getPointsUsed()));

        });

        intM.setOnMouseClicked((event) -> {
            s.decreaseInt();
            intel.setText("Intelligence (" + s.getIntelligence() + ")");
            pointsLeft.setText("Points left: " + (s.getInitialPoints() - s.getPointsUsed()));

        });

        dexM.setOnMouseClicked((event) -> {
            s.decreaseDex();
            dex.setText("Dexterity (" + s.getDexterity() + ")");
            pointsLeft.setText("Points left: " + (s.getInitialPoints() - s.getPointsUsed()));

        });

        stats.add(str, 0, 0);
        stats.add(con, 1, 0);
        stats.add(intel, 2, 0);
        stats.add(dex, 3, 0);

        stats.add(strP, 0, 1);
        stats.add(conP, 1, 1);
        stats.add(intP, 2, 1);
        stats.add(dexP, 3, 1);

        stats.add(strM, 0, 2);
        stats.add(conM, 1, 2);
        stats.add(intM, 2, 2);
        stats.add(dexM, 3, 2);

        TextField nameField = new TextField();
        nameField.setText("name");
        
        Button start = new Button("Start!");
        start.setOnMouseClicked((event) -> {
            if (nameField.getText().length() > 30) {
                help.setText("The Name of your character must be less than 30 characters long.");
            } else {
                ItemDb idb = new ItemDb();
                Weapon w = null;
                Armor a = null;
                try {
                    w = (Weapon) idb.itemConverter("stick");
                    a = (Armor) idb.itemConverter("clothes");
                } catch (Exception ex) {
                    
                }
                
                PlayerStats ps = new PlayerStats(1, s.getStrength(), s.getConnstitution(), s.getIntelligence(), s.getDexterity(), w, a);
                this.newGame(ps, nameField.getText());
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(help, new Label(""), nameField, new Label(""), stats, new Label(""), pointsLeft, new Label(""), start);
        vbox.setAlignment(Pos.CENTER);
        
        vbox.setMinSize(50 * pixelSize, 60 * pixelSize);
        
        Scene charCreationScene = new Scene(vbox);

        primaryStage.setScene(charCreationScene);

    }

    private void newGameInit() {
        logs = new ArrayList<>();

        level = new Label();
        turnCounter = new Label();
        hp = new Label();
        floor = new Label();
        stamina = new Label();
        itemDb = new ItemDb();
        backToMap = new Button("Open Inventory");
        framework = new BorderPane();
        menu = new Button("Menu");
        status = UIStatus.MAP;
    }

}
