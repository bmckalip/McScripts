package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.wrappers.interactive.GameObject;

import java.util.Random;

import static com.bmc.mclib.constants.McObjects.LADDER_UP;
import static com.bmc.mclib.constants.McTiles.MONK_LADDER_UP_TILE;

public class ClimbUpTask extends Task {
    GameObject[] ladders;

    public ClimbUpTask(McScript s) {
        super(s);
        refreshLadders();
    }

    @Override
    public boolean validate() {
        return ladderIsValid() && s.getInventory().isEmpty() && s.getLocalPlayer().getZ() == 0;
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        Random r = new Random();
        refreshLadders();
        if (ladderIsValid()) {
            if (ladders[0].isOnScreen()) {
                ladders[0].interact("Climb-Up");
                return r.nextInt(300) + 1400;
            } else {
                s.getWalking().walk(ladders[0]);
                if (!s.getWalking().isRunEnabled()) {
                    return r.nextInt(700) + 1500;
                }
            }
        }
        return r.nextInt(700) + 300;
    }
    private boolean ladderIsValid(){
        refreshLadders();
        return ladders.length > 0 && ladders[0].exists() && ladders[0].getID() == LADDER_UP;
    }

    private void refreshLadders(){
        ladders = s.getGameObjects().getObjectsOnTile(MONK_LADDER_UP_TILE);
    }

    @Override
    public String toString(){
        return "Climbing Up Ladder";
    }
}
