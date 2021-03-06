package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.wrappers.items.GroundItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.bmc.mclib.constants.McItems.ROBE_BOTTOM;
import static com.bmc.mclib.constants.McItems.ROBE_TOP;
import static com.bmc.mclib.constants.McWorlds.F2P_WORLDS;

public class HopWorldsTask extends Task {
    private List<GroundItem> items;
    private LootTask topLootTask;
    private LootTask bottomLootTask;

    public HopWorldsTask(McScript s, LootTask topLootTask, LootTask bottomLootTask) {
        super(s, "Hopping Worlds");
        this.topLootTask = topLootTask;
        this.bottomLootTask = bottomLootTask;
        items = new ArrayList<GroundItem>();
    }

    @Override
    public boolean validate() {
        if(!s.getInventory().isFull() && !areNoItems()){
            return false;
        }else{
            Random r = new Random();
            s.sleep(1000 + r.nextInt(2000));
            this.refreshObjects();
            return !s.getInventory().isFull() && areNoItems();
        }
    }

    @Override
    public void execute() {
        int oldWorld = s.getClient().getCurrentWorld();
        List<Integer> availableWorlds = new ArrayList<Integer>(F2P_WORLDS);
        availableWorlds.remove(new Integer(oldWorld));
        int world = availableWorlds.get(r.nextInt(availableWorlds.size() - 1));

        s.getWorldHopper().hopWorld(world);
        s.sleepUntil(() -> oldWorld != s.getClient().getCurrentWorld() && s.getLocalPlayer().exists(), 7000);
        delay = r.nextInt(300) + 200;
    }

    @Override
    public void refreshObjects(){
        if (items != null) items.clear();
        if(topLootTask != null && topLootTask.enabled){
            items.addAll(s.getGroundItems().all(i -> i != null && i.getID() == ROBE_TOP));
        }

        if(bottomLootTask != null && bottomLootTask.enabled){
            items.addAll(s.getGroundItems().all(i -> i != null && i.getID() == ROBE_BOTTOM));
        }
    }

    private boolean areNoItems(){return items == null || items.size() <= 0;}

}
