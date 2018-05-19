package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.wrappers.interactive.GameObject;

import static com.bmc.mclib.constants.McObjects.MONK_LADDER_DOWN;
import static com.bmc.mclib.constants.McTiles.MONK_LADDER_DOWN_TILE;

public class ClimbDownTask extends Task{
    private GameObject[] ladders;

    public ClimbDownTask(McScript s){
        super(s);
    }

    @Override
    public boolean validate() {
        return ladderIsValid() && s.getInventory().isFull() && s.getLocalPlayer().getZ() == 1;
    }

    @Override
    public void execute() {
        if(ladderIsValid()) ladders[0].interact("Climb-Down");

        if (!s.getWalking().isRunEnabled()) {
            delay = r.nextInt(700) + 1500;
        }else{
            delay = r.nextInt(300) + 700;
        }
    }

    private boolean ladderIsValid(){
        return ladders != null && ladders.length > 0 && ladders[0].exists() && ladders[0].getID() == MONK_LADDER_DOWN;
    }

    @Override
    public void refreshObjects(){
        ladders = s.getGameObjects().getObjectsOnTile(MONK_LADDER_DOWN_TILE);
    }

    @Override
    public String toString(){
        return "Climbing Down Ladder";
    }
}
