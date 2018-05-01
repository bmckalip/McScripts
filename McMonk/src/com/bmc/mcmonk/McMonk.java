package com.bmc.mcmonk;

import com.bmc.mclib.constants.McColors;
import com.bmc.mclib.script.McScript;
import com.bmc.mclib.taskmanager.TaskList;
import com.bmc.mclib.tasks.Task;
import com.bmc.mclib.utility.McCalculations;
import com.bmc.mclib.utility.McFormatting;
import com.bmc.mclib.utility.McGUI;
import com.bmc.mclib.utility.McPaint;
import com.bmc.mcmonk.tasks.*;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

import static com.bmc.mclib.constants.McItems.ROBE_BOTTOM;
import static com.bmc.mclib.constants.McItems.ROBE_TOP;

@ScriptManifest(category = Category.MONEYMAKING, author = "BMC", version=0.1, name="McMonk")
public class McMonk extends McScript{
    //state
    private long startTime;
    private Image background;

    //paint resources
    private final String paintBackgroundURL = "https://i.imgur.com/WQH38mZ.png";
    private final String font = "Arial";

    //localization
    private final String guiTitle = "McMonk Settings";
    private final String lootTopsString = "Loot Tops";
    private final String lootBottomsString = "Loot Bottoms";
    private final String hopWorldsString = "Hop Worlds";
    private final String startScriptString = "Start McMonk";


    //GUI settings
    private boolean doLootBottoms = true;
    private boolean doLootTops = true;
    private boolean doHopWorlds = true;

    //super behaviour
    @Override
    public void onStart(){
        super.onStart();
        startTime = System.currentTimeMillis();
        setupPaintResources();
        this.getWalking().setRunThreshold(40);
        hasGUI = true;
    }

    @Override
    public void initializeTasks() {
        OpenBankTask openBankTask      = new OpenBankTask(this);
        DepositTask depositTask        = new DepositTask(this);
        GainMonasteryAccess gainAccess = new GainMonasteryAccess(this);
        ClimbUpTask climbUpTask        = new ClimbUpTask(this);
        OpenDoorTask openDoorTask      = new OpenDoorTask(this);
        CloseDoorTask closeDoorTask    = new CloseDoorTask(this);
        EquipTask equipTopTask         = new EquipTask(this, ROBE_TOP);
        EquipTask equipBottomTask      = new EquipTask(this, ROBE_BOTTOM);
        LootTask lootTopTask           = new LootTask(this, ROBE_TOP);
        LootTask lootBottomTask        = new LootTask(this, ROBE_BOTTOM);
        HopWorldsTask hopWorldsTask    = new HopWorldsTask(this, lootTopTask, lootBottomTask);
        ClimbDownTask climbDownTask    = new ClimbDownTask(this);

        Task[] tasks = {
                openBankTask,
                depositTask,
                gainAccess,
                climbUpTask,
                openDoorTask,
                closeDoorTask,
                equipTopTask,
                equipBottomTask,
                lootTopTask,
                lootBottomTask,
                hopWorldsTask,
                climbDownTask
        };

        setTasks(new TaskList(tasks));

        if(!doLootBottoms) lootBottomTask.enabled = equipBottomTask.enabled = false;
        if(!doLootTops)    lootTopTask.enabled = equipTopTask.enabled = false;
        if(!doHopWorlds)   hopWorldsTask.enabled = false;
    }

    @Override
    public void onPaint(Graphics g){
        if(McPaint.shouldPaint(this, hasGUI, guiCompleted)){
            incrementLootCount();
            if(background != null){ g.drawImage(background, 0, 338, null); }
            addPaintText(g);
        }
    }

    @Override
    public void createGUI() {
        JFrame gui = McGUI.createDefaultGUI(guiTitle, 300, 150);
        addGUICheckboxes(gui);
        addGUIButtons(gui);
        gui.pack();
        gui.setVisible(true);
    }

    //local behavior
    //paint setup methods
    private void addPaintText(Graphics g) {
        g.setColor(McColors.MONK_BROWN);
        g.setFont(new Font(font, Font.PLAIN, 10));
        g.drawString(Integer.toString(LootTask.topsLooted), 102, 364);
        g.drawString(Integer.toString(LootTask.bottomsLooted), 102, 376);
        g.drawString(McFormatting.D2F.format(McCalculations.getRobesPerHour(LootTask.topsLooted + LootTask.bottomsLooted, startTime)), 102, 388);
        g.drawString(McFormatting.getElapsedTimestamp(startTime), 436, 388);
    }

    private void setupPaintResources(){
        try { background = ImageIO.read(new URL(paintBackgroundURL)); }
        catch (IOException e) {}
    }

    //GUI setup methods
    private void addGUICheckboxes(JFrame gui){
        JPanel settings = new JPanel();
        settings.setLayout(new GridLayout(3, 1));

        JCheckBox lootTopsCheckbox = McGUI.createCheckbox(lootTopsString, doLootTops);
        JCheckBox lootBottomsCheckbox = McGUI.createCheckbox(lootBottomsString, doLootBottoms);
        JCheckBox hopWorldsCheckbox = McGUI.createCheckbox(hopWorldsString, doHopWorlds);

        lootTopsCheckbox.addActionListener(e -> doLootBottoms = lootTopsCheckbox.isSelected());
        lootBottomsCheckbox.addActionListener(e -> doLootBottoms = lootBottomsCheckbox.isSelected());
        hopWorldsCheckbox.addActionListener(e -> doHopWorlds = hopWorldsCheckbox.isSelected());

        settings.add(lootTopsCheckbox);
        settings.add(lootBottomsCheckbox);
        settings.add(hopWorldsCheckbox);

        gui.getContentPane().add(settings, BorderLayout.PAGE_START);
    }

    private void addGUIButtons(JFrame gui){
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 0));
        JButton button = McGUI.createButton(startScriptString);
        button.addActionListener(e -> {
            initializeTasks();
            guiCompleted = true;
            gui.dispose();
        });
        gui.getContentPane().add(button, BorderLayout.PAGE_END);
    }

    //utility methods
    private void incrementLootCount() {
        if(getInventory().count(ROBE_TOP) > LootTask.currentTopCount && !isIdle){
            LootTask.topsLooted++;
            LootTask.currentTopCount = getInventory().count(ROBE_TOP);
        }

        if(getInventory().count(ROBE_BOTTOM) > LootTask.currentBottomCount && !isIdle){
            LootTask.bottomsLooted++;
            LootTask.currentBottomCount = getInventory().count(ROBE_BOTTOM);
        }
    }
}
