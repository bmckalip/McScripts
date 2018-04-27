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
        super(s);
        this.topLootTask = topLootTask;
        this.bottomLootTask = bottomLootTask;
        items = new ArrayList<GroundItem>();
        refreshItems();
    }

    @Override
    public boolean validate() {
        this.refreshItems();
        if(!s.getInventory().isFull() && !areNoItems()){
            return false;
        }else{
            Random r = new Random();
            s.sleep(1000 + r.nextInt(2000));
            this.refreshItems();
            return !s.getInventory().isFull() && areNoItems();
        }
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        Random r = new Random();
        int oldWorld = s.getClient().getCurrentWorld();
        List<Integer> availableWorlds = new ArrayList<Integer>(F2P_WORLDS);
        availableWorlds.remove(new Integer(oldWorld));
        int world = availableWorlds.get(r.nextInt(availableWorlds.size() - 1));

        s.getWorldHopper().hopWorld(world);
        s.sleepUntil(() -> oldWorld != s.getClient().getCurrentWorld() && s.getLocalPlayer().exists(), 7000);
        return r.nextInt(300) + 200;
    }

    @Override
    public String toString(){
        return "Hopping Worlds";
    }

    private void refreshItems(){
        items.clear();

        if(topLootTask.enabled){
            items.addAll(s.getGroundItems().all(i -> i != null && i.getID() == ROBE_TOP));
        }

        if(bottomLootTask.enabled){
            items.addAll(s.getGroundItems().all(i -> i != null && i.getID() == ROBE_BOTTOM));
        }
    }

    private boolean areNoItems(){return items == null || items.size() <= 0;}

}
