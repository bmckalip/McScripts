package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.wrappers.items.GroundItem;

import static com.bmc.mclib.constants.McAreas.MONK_LOOTING_ROOM;
import static com.bmc.mclib.constants.McItems.ROBE_BOTTOM;
import static com.bmc.mclib.constants.McItems.ROBE_TOP;
import static com.bmc.mclib.constants.McTiles.ROBE_BOTTOM_LOCATION;
import static com.bmc.mclib.constants.McTiles.ROBE_TOP_LOCATION;

public class LootTask extends Task {
    //loot counter variables
    public int currentTopCount;
    public int currentBottomCount;
    public int topsLooted = 0;
    public int bottomsLooted = 0;

    private int itemToLoot;

    private Tile lootLocation;

    private GroundItem[] items;

    public LootTask(McScript s, int itemToLoot){
        super(s);
        this.itemToLoot = itemToLoot;
        if(itemToLoot == ROBE_TOP){
            lootLocation = ROBE_TOP_LOCATION;
        }else if(itemToLoot == ROBE_BOTTOM){
            lootLocation = ROBE_BOTTOM_LOCATION;
        }
    }

    @Override
    public boolean validate() {
        if(s.getInventory().isFull()) return false;
        if(items != null && items.length > 0){
            for(GroundItem item : items){
                if(item.getID() == this.itemToLoot){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void execute() {
        adjustCamera(20, 50);
        for(GroundItem item : items){
            if (item.getID() == this.itemToLoot) {
                if (MONK_LOOTING_ROOM.contains(s.getLocalPlayer())) {
                    item.interact("Take");
                } else {
                    adjustCamera(33, 50);
                    item.interactForceRight("Take");
                }
                break;
            }
        }
        delay = r.nextInt(300) + 1600;
    }

    @Override
    public void refreshObjects(){
        currentBottomCount = s.getInventory().count(ROBE_BOTTOM);
        currentTopCount = s.getInventory().count(ROBE_TOP);
        items = s.getGroundItems().getGroundItems(lootLocation);
    }

    @Override
    public String toString(){ return itemToLoot == ROBE_TOP ? "Looting Robe Top" : "Looting Robe Bottom"; }

    private void adjustCamera(int executionPercent, int byMousePercent) {
        if (r.nextInt(1/ (executionPercent / 100)) == 0) {
            if (r.nextInt(1/ (byMousePercent / 100)) == 0) {
                s.getCamera().rotateToTile(lootLocation);
            } else {
                s.getCamera().mouseRotateToTile(lootLocation);
            }
        }
    }
}
