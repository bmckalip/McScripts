package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.wrappers.interactive.GameObject;

import static com.bmc.mclib.constants.McAreas.MONK_LOOTING_ROOM;
import static com.bmc.mclib.constants.McObjects.DOOR;
import static com.bmc.mclib.constants.McTiles.MONK_DOOR_TILE;

public class OpenDoorTask extends Task {
    private GameObject[] doors;
    public OpenDoorTask(McScript s) {
        super(s, "Opening Door");
    }

    @Override
    public boolean validate() {
        return  doorIsClosed() &&
                s.getLocalPlayer().getZ() == 1 && (
                (!MONK_LOOTING_ROOM.contains(s.getLocalPlayer()) && !s.getInventory().isFull()) ||
                (MONK_LOOTING_ROOM.contains(s.getLocalPlayer()) && s.getInventory().isFull()));
    }

    @Override
    public void execute() {
        if(doors != null && doors.length > 0){
            GameObject door = doors[0];
            if(door != null) door.interact("Open");
        }
        delay = r.nextInt(300) + 500;
    }

    private boolean doorIsClosed(){
        GameObject[] doors = s.getGameObjects().getObjectsOnTile(MONK_DOOR_TILE);
        return doors.length > 0 && doors[0].exists() && doors[0].getID() == DOOR;
    }

    @Override
    public void refreshObjects(){
        doors = s.getGameObjects().getObjectsOnTile(MONK_DOOR_TILE);
    }
}
