import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;

import java.util.List;

//@ScriptManifest(category = Category.MONEYMAKING, name = "McMonk", author = "Bmc", version = 1.0, description="Loots monk's robes from the monastery. Requires 31 prayer.")

public class McMonk extends AbstractScript {
    private List<World> worlds;
    private static enum state {IDLE, BANKING, TRANSIT, LOOTING, STOP};
    private state prevState, currState;
    private int totalLooted, prevTotal, currTotal;
    private boolean lootBoth = true; //if false, only loot tops
    private boolean doHop = false;   //if true, hop worlds when nothing to loot

    private Player player;
    private Inventory inventory;
    private Area destination;
    private Area Bank, Mon_0, Mon_1, Mon_Room;
    private GroundItem ROBE_TOP, ROBE_BOTTOM;
    private GameObject ladder, door, table;

    @Override
    public void onStart(){
        worlds = getWorlds().f2p();
        worlds.remove(getWorlds().getWorld(381));
        worlds.remove(getWorlds().getWorld(385));
        defineAreas();
        totalLooted = 0;
        currState = state.IDLE;
        destination = Mon_Room;
        refreshObjects();
    }

    @Override
    public int onLoop(){

        int sleepFor;
        //prevTotal = inventory.count(i -> i.getID() == ROBE_BOTTOM.getID() && i.getID() == ROBE_TOP.getID());
        nextState();        //transition to next state
        sleepFor = execute(50);  //arg = % chance of antiban action occuring
//        currTotal = inventory.count(i -> i.getID() == ROBE_BOTTOM.getID() && i.getID() == ROBE_TOP.getID());
//        if (currTotal > prevTotal) {
//            totalLooted++;
            //log(Integer.toString(totalLooted));
//       }
        return sleepFor;
    }

    private void nextState() {
        state prevState = currState;
        refreshObjects();

        //defines state transitions
        switch (prevState) {
            case LOOTING:
                if(inventory.isFull()){
                    currState = state.TRANSIT;
                    destination = Bank;
                }
                if(ROBE_TOP == null | ROBE_BOTTOM == null) {
                    currState = state.IDLE;
                }
                break;
            case BANKING:
                if (inventory.isEmpty()) {
                    currState = state.TRANSIT;
                    destination = Mon_Room;
                }
                break;
            case IDLE:
                if(inventory.isFull()){
                    currState = state.TRANSIT;
                    destination = Bank;
                }else if(inventory.isEmpty()){
                    currState = state.TRANSIT;
                    destination = Mon_Room;
                }else {
                    currState = state.TRANSIT;
                    if (Calculations.distance(player.getTile(), Mon_0.getCenter()) < Calculations.distance(player.getTile(), Bank.getCenter())) {
                        destination = Mon_Room;
                    } else {
                        destination = Bank;
                    }
                }
                break;
            case TRANSIT:
                if(destination == null){
                    if(Bank.contains(player)){
                        currState = state.BANKING;
                    }else if(Mon_Room.contains(player)){
                        currState = state.LOOTING;
                    }else{
                        currState = state.IDLE;
                    }
                }else if(destination.contains(player)){
                    //if player at dest
                    destination = null;
                }else{
                    //if player not at dest
                }
                break;
            case STOP:
                break;
            default:
                currState = state.IDLE;
                log("transitioning on invalid state:" + currState);
                break;
        }

        //log state changes
        if (prevState != currState) {
            //log("Transitioned to state: " + currState.toString());
            if(destination != null){
                //log("Destination: " + destination.getCenter().toString());
            }
        }
    }

    private int execute(int antibanLikelihood){
        if(!lootBoth) {
            ROBE_BOTTOM = null;
        }

        switch(currState){
            case TRANSIT:
                return walk();
            case BANKING:
                return bank();
            case LOOTING:
                return loot();
            case IDLE:
                if(!doHop){
                   return antiban(antibanLikelihood);
                }else {
                    if (Calculations.random(100) < 20) {
                        return hopWorlds();
                    } else {
                        return antiban(antibanLikelihood);
                    }
                }
            case STOP:
                stop();
                return 0;
            default:
                log("executing on invalid state:" + currState);
                return Calculations.random(300, 500);
        }

    }

    public void refreshObjects(){
        player = getLocalPlayer();
        inventory = getInventory();
        ROBE_TOP = getGroundItems().closest(544);
        ROBE_BOTTOM = getGroundItems().closest(542);

        ladder = getGameObjects().closest("Ladder");
        door = getGameObjects().closest("Door");
        table = getGameObjects().closest("Table");
    }

    public void defineAreas(){
        Mon_Room = new Area(3056, 3489, 3059, 3486, 1);
        Mon_1 = new Area(3059, 3482, 3044, 3500, 1);
        Mon_0 = new Area(3059, 3482, 3044, 3500, 0);
        Bank = new Area(3091, 3488, 3094, 3499, 0);
    }

    private int walk(){
        getWalking().setRunThreshold(30);
        int sleepFor = Calculations.random(500, 2000);
        if(destination == Mon_Room){
            if(!Mon_0.contains(player) && !Mon_1.contains(player)){
                getWalking().walk(Mon_0.getCenter());
                sleepFor = Calculations.random(500,2000);
            }else if(Mon_0.contains(player)){
                if(Calculations.distance(player.getTile(), ladder.getTile()) < Calculations.random(8, 15)){
                    ladder.interact();
                    sleepFor = Calculations.random(1000,2000);
                }else{
                    getWalking().walk(ladder);
                    sleepFor = Calculations.random(1000,2000);
                }
            }else{
                if(door.hasAction("Open")) {
                    if(!Mon_Room.contains(player)) {
                        door.interact("Open");
                    }
                    sleepFor = Calculations.random(1000,2000);
                }else {
                    if (!Mon_Room.contains(player)) {
                        getWalking().walkOnScreen(Mon_Room.getNearestTile(table).getRandomizedTile());
                        sleepFor = Calculations.random(1000,2000);
                    }else{
                        destination = null;
                        sleepFor = 0;
                    }
                }
            }
        }else if(destination == Bank){
            if(Mon_Room.contains(player)){
                if(door.hasAction("Open")){
                    door.interact("Open");
                    sleepFor = Calculations.random(1000,2000);
                }else{
                    ladder.interact();
                    sleepFor = Calculations.random(1000,2000);
                }
            }else if(Mon_1.contains(player)){
                ladder.interact();
                sleepFor = Calculations.random(1000,2000);
            }else if(!Bank.contains(player)) {
                getWalking().walk(Bank.getCenter().getRandomizedTile());
                sleepFor = Calculations.random(500,2000);
            }else{
                destination = null;
                sleepFor = 0;
            }
        }
        return sleepFor;
    }

    private int bank(){
        if(!getBank().isOpen()){
            getBank().openClosest();
        }else{
            getBank().depositAllItems();
            getBank().depositAllEquipment();
        }
        return  Calculations.random(700, 1400);
    }

    private int loot() {

        if (!getEquipment().contains(544) && inventory.contains(544))
            inventory.interact(544, "Wear");
        if (!getEquipment().contains(542) && inventory.contains(542))
            inventory.interact(542, "Wear");
        if (ROBE_TOP != null && ROBE_TOP.exists()) {
            ROBE_TOP.interact();
            return Calculations.random(1000, 1500);
        } else if (ROBE_BOTTOM != null && ROBE_BOTTOM.exists()) {
            ROBE_BOTTOM.interact();
            return Calculations.random(1000, 2000);
        } else {
            return 0;
        }
    }

    private int antiban(int antibanLikelihood) {
        if (Calculations.random(100) <= antibanLikelihood) {

            if (Calculations.random(100) <= 15 && table != null) {
                getCamera().rotateToEntity(table);
                //log("Antiban triggered");
                return Calculations.random(400, 1000);
            }

            if (table != null) {
                int rand = Calculations.random(2, 10);
                if (getMouse().getIdleTime() >= rand) {
                    if (rand > 5) {
                        getMouse().move(table);
                    }
                    //log("Antiban triggered");
                    return Calculations.random(400, 1000);
                } else {
                    getMouse().move();
                }
            }


        if (Calculations.random(100) <= 10 && table != null) {
            if (door != null) {
                getCamera().rotateToEntity(door);
            }
            //log("Antiban triggered");
            return Calculations.random(400, 1000);
        }

        if (Calculations.random(100) <= 15 && table != null) {
            getCamera().rotateToPitch(Calculations.random(40));
            //log("Antiban triggered");
            return Calculations.random(400, 1000);
        }

        return Calculations.random(400, 1000);

        }else{
            return 0;
        }
    }

    private int hopWorlds(){
        if(ROBE_BOTTOM != null || ROBE_TOP != null){
            return 0;
        }
        //log(worlds.toString());
        if(getWorldHopper().hopWorld(worlds.get(Calculations.random(worlds.size()) - 1))){
            return Calculations.random(3000, 5000);
        }else{
           return Calculations.random(400, 800);
        }

        //log(getClient().getLoginResponse().getResponses().toString());

    }
}