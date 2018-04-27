package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.wrappers.interactive.GameObject;

import java.util.Random;

import static com.bmc.mclib.constants.McTiles.MONK_DOOR_TILE;
import static com.bmc.mclib.constants.McTiles.MONK_STUCK_TILE;

public class CloseDoorTask extends Task {
    private GameObject[] doors;

    public CloseDoorTask(McScript s){ super(s); }

    @Override
    public boolean validate() {
        refreshDoors();
        return s.getLocalPlayer().getTile().equals(MONK_STUCK_TILE) && doorIsOpen();
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        Random r = new Random();
        refreshDoors();
        for(GameObject door : doors){
            if(door != null && door.exists() && door.hasAction("Close")) {
                door.interact("Close");
            }
        }
        return r.nextInt(300) + 1400;
    }

    private boolean doorIsOpen(){
        refreshDoors();

        for(GameObject door : doors){
            if(door != null && door.exists() && door.hasAction("Close")) {
                return true;
            }
        }
        return false;
    }

    private void refreshDoors(){
        doors = s.getGameObjects().getObjectsOnTile(MONK_DOOR_TILE);
    }

    @Override
    public String toString(){
        return "Closing Door";
    }
}
