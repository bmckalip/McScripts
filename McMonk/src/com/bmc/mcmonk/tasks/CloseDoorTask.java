package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.wrappers.interactive.GameObject;

import static com.bmc.mclib.constants.McTiles.MONK_DOOR_TILE;
import static com.bmc.mclib.constants.McTiles.MONK_STUCK_TILE;

public class CloseDoorTask extends Task {
    private GameObject[] doors;

    public CloseDoorTask(McScript s){ super(s, "Closing Door"); }

    @Override
    public boolean validate() {
        return s.getLocalPlayer().getTile().equals(MONK_STUCK_TILE) && doorIsOpen();
    }

    @Override
    public void execute() {
        for(GameObject door : doors){
            if(door != null && door.exists() && door.hasAction("Close")) {
                door.interact("Close");
            }
        }
        delay = r.nextInt(300) + 1400;
    }

    private boolean doorIsOpen(){
        for(GameObject door : doors){
            if(door != null && door.exists() && door.hasAction("Close")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void refreshObjects(){
        doors = s.getGameObjects().getObjectsOnTile(MONK_DOOR_TILE);
    }
}
