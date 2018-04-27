package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.wrappers.interactive.GameObject;

import java.util.Random;

import static com.bmc.mclib.constants.McObjects.MONK_LADDER_DOWN;
import static com.bmc.mclib.constants.McTiles.MONK_LADDER_DOWN_TILE;

public class ClimbDownTask extends Task{
    private GameObject[] ladders;

    public ClimbDownTask(McScript s){
        super(s);
        refreshLadders();
    }

    @Override
    public boolean validate() {
        return ladderIsValid() && s.getInventory().isFull() && s.getLocalPlayer().getZ() == 1;
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        Random r = new Random();
        refreshLadders();
        if(ladderIsValid()) ladders[0].interact("Climb-Down");

        if (!s.getWalking().isRunEnabled()) {
            return r.nextInt(700) + 1500;
        }
        return r.nextInt(300) + 700;
    }

    private boolean ladderIsValid(){
        refreshLadders();
        return ladders != null && ladders.length > 0 && ladders[0].exists() && ladders[0].getID() == MONK_LADDER_DOWN;
    }

    private void refreshLadders(){
        ladders = s.getGameObjects().getObjectsOnTile(MONK_LADDER_DOWN_TILE);
    }

    @Override
    public String toString(){
        return "Climbing Down Ladder";
    }
}
