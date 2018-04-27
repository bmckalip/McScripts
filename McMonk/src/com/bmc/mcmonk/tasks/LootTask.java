package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.wrappers.items.GroundItem;

import java.util.Random;

import static com.bmc.mclib.constants.McAreas.MONK_LOOTING_ROOM;
import static com.bmc.mclib.constants.McItems.ROBE_BOTTOM;
import static com.bmc.mclib.constants.McItems.ROBE_TOP;
import static com.bmc.mclib.constants.McTiles.ROBE_BOTTOM_LOCATION;
import static com.bmc.mclib.constants.McTiles.ROBE_TOP_LOCATION;

public class LootTask extends Task {
    //loot counter static variables
    public static int currentTopCount;
    public static int currentBottomCount;
    public static int topsLooted = 0;
    public static int bottomsLooted = 0;

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

        }else{
            s.log("invalid item");
        }
        this.refreshItems();
    }

    @Override
    public boolean validate() {
        if(s.getInventory().isFull()) return false;
        this.refreshItems();
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
    public int execute() {
        Task.previousTask = toString();
        Random r = new Random();
        if(r.nextInt(5) == 0){
            if(r.nextInt(2) == 0){
                s.getCamera().rotateToTile(lootLocation);
            }else{
                s.getCamera().mouseRotateToTile(lootLocation);
            }
        }
        for(GroundItem item : items){
            if (item.getID() == this.itemToLoot) {
                if (MONK_LOOTING_ROOM.contains(s.getLocalPlayer())) {
                    item.interact("Take");
                } else {
                    if(r.nextInt(3) == 0) {
                        if(r.nextInt(2) == 0){
                            s.getCamera().rotateToTile(lootLocation);
                        }else{
                            s.getCamera().mouseRotateToTile(lootLocation);
                        }
                    }
                    item.interactForceRight("Take");
                }
                break;
            }
        }

        return r.nextInt(300) + 1600;
    }

    private void refreshItems(){
        currentBottomCount = s.getInventory().count(ROBE_BOTTOM);
        currentTopCount = s.getInventory().count(ROBE_TOP);
        items = s.getGroundItems().getGroundItems(lootLocation);
    }

    @Override
    public String toString(){
        return "Looting Robes";
    }
}
