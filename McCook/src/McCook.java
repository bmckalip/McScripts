package mccook;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.world.*;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.WidgetChild;

@ScriptManifest(category = Category.MONEYMAKING, name = "McCook", author = "Bmc", version = 1.0)

public class McCook extends AbstractScript{
    private static enum ScriptState {STOP, IDLE, TRANSIT, BANKING, COOKING};
    private static final String[] foods = {"Raw Anchovies", "Raw Shrimp"};
    private ScriptState currState;
    private Area destination;
    private static final Area BANK_AREA = new Area(3091, 3488, 3094, 3499, 0);
    private static final Area RANGE_AREA = new Area(3081, 3496, 3077, 3494, 0);
    private static final Area YARD_AREA = new Area(3086, 3500, 3077, 3494, 0);


    private Entity player;
    private Inventory inventory;
    private GameObject gate, door, range;
    private NPC banker;

    private static String food;

    private void nextstate(){
        ScriptState prevState = currState;
        refreshObjects();

        //defines state transitions
        switch(prevState){
            case BANKING:
            case COOKING:
                currState = ScriptState.IDLE;
                break;
            case IDLE:
                if(getLocalPlayer().isInteracting(banker) || getLocalPlayer().isAnimating()){
                    break;
                }
                if(inventory.count(food) == 0){
                    currState = ScriptState.TRANSIT;
                    destination = BANK_AREA;
                }else if(inventory.count(food) == 28){
                    currState = ScriptState.TRANSIT;
                    destination = RANGE_AREA;
                }else{
                    double distToBank = Double.MAX_VALUE;
                    double distToRange = Double.MAX_VALUE;

                    try{
                        distToBank = Calculations.distance(player.getTile(), banker.getTile());
                    }catch(NullPointerException x){
                        currState = ScriptState.TRANSIT;
                        destination = RANGE_AREA;
                    }

                    try{
                        distToRange = Calculations.distance(player.getTile(), range.getTile());
                    }catch(NullPointerException x){
                        currState = ScriptState.TRANSIT;
                        destination = BANK_AREA;
                    }

                    if(distToBank < distToRange){
                        currState = ScriptState.TRANSIT;
                        destination = BANK_AREA;
                    }else{
                        currState = ScriptState.TRANSIT;
                        destination = RANGE_AREA;
                    }
                }
                break;
            case TRANSIT:
                if(destination == BANK_AREA){
                    if(BANK_AREA.contains(player)) currState = ScriptState.BANKING;
                }else if(destination == RANGE_AREA) {
                    if(RANGE_AREA.contains(player)) currState = ScriptState.COOKING;
                }
                break;
            case STOP:
                break;
            default:
                //log("invalid state:" + currState);
                break;
        }
        //log state changes
        if(prevState != currState) log("Transitioned to state: " + currState.toString() + "; Destination: " + destination.getCenter().toString());
    }

    private int execute(int antibanLikelihood){

        if(Calculations.random(100) <= antibanLikelihood){
            return antiban();
        }

        switch(currState){
            case TRANSIT:
                if(!destination.contains(player.getTile())){
                    getWalking().walk(destination.getRandomTile());
                }
                return  500;
            //return walk(destination);
            case BANKING:
                return bank();
            case COOKING:
                return cook();
            case STOP:
                stop();
                return 0;
            default:
                //log("invalid state:" + currState);
                return Calculations.random(300, 500);
        }
    }

    @Override
    public void onStart(){

        currState = ScriptState.IDLE;
        refreshObjects();
        food = getTargetFood();
        log("Cooking: " + food);

    }

    @Override
    public int onLoop(){
        nextstate();        //transition to next state
        return execute(5);  //arg = % chance of antiban action occuring
    }

    private void refreshObjects(){
        player = getLocalPlayer();
        inventory = getInventory();
        banker = getNpcs().closest("Banker");
        gate = getGameObjects().closest(1567);
        door = getGameObjects().closest(1536);
        range = getGameObjects().closest(12269);


    }

    private int bank(){
        if(!getBank().isOpen()){
            getBank().openClosest();
        }else{
            getBank().depositAllItems();
            if(getBank().contains(food)){
                getBank().withdrawAll(food);
                getBank().close();
            }else{
                getBank().close();
                stop();
            }
        }
        return Calculations.random(200, 500);
    }

    private int cook(){
        if(!range.isOnScreen()){
            getCamera().rotateToEntity(range);
        }
        if(getWidgets().getWidget(307) != null && getWidgets().getWidget(307).isVisible()){
            getWidgets().getWidget(307).getChild(3).interact("Cook All");
        }else{
            inventory.get(food).useOn(range);
        }
        return Calculations.random(500, 2000);
    }

    private int walk(Area dest){
        if(dest == RANGE_AREA){
            if(!RANGE_AREA.contains(player.getTile())){
                if(gate != null && gate.hasAction("Open") && gate.isOnScreen()){
                    gate.interact("Open");
                }else if(gate != null && !gate.isOnScreen()){
                    if(gate.hasAction("Open")){
                        getCamera().rotateToEntity(gate);
                    }else if(Calculations.random(100) <= 60){
                        getCamera().rotateToEntity(gate);
                    }
                }else if(door != null && door.hasAction("Open") && door.isOnScreen()){
                    door.interact("Open");
                }else if(door != null && !door.isOnScreen()){
                    if(door.hasAction("Open")){
                        getCamera().rotateToEntity(door);
                    }else if(Calculations.random(100) <= 60){
                        getCamera().rotateToEntity(door);
                    }
                }else if(range != null && Calculations.distance(player.getTile(), range.getTile()) < 10){
                    if(range.isOnScreen()){
                        getWalking().walkOnScreen(dest.getRandomTile());
                    }else{
                        getWalking().walk(dest.getRandomTile());
                    }

                }else{
                    getWalking().walk(dest.getRandomTile());
                }
            }

        }else if(dest == BANK_AREA){
            if(!BANK_AREA.contains(player.getTile())){
                if(gate != null && gate.hasAction("Open") && gate.isOnScreen()){
                    if(RANGE_AREA.contains(player.getTile()) || YARD_AREA.contains(player.getTile())){
                        gate.interact("Open");
                    }
                }else if(gate != null && Calculations.distance(player.getTile(), gate.getTile()) <= 10 && !gate.isOnScreen()){
                    if(gate.hasAction("Open")){
                        getCamera().rotateToEntity(gate);
                    }else if(Calculations.random(100) <= 60){
                        getCamera().rotateToEntity(gate);
                    }
                }else{
                    if(banker != null && banker.exists()){

                    }else if(banker != null && Calculations.distance(player.getTile(), banker.getTile()) < 10){
                        if(banker.isOnScreen()){
                            getWalking().walkOnScreen(dest.getNearestTile(banker).getRandomizedTile());
                        }else{
                            getWalking().walk(dest.getNearestTile(banker).getRandomizedTile());
                        }
                    }else{
                        if(banker != null){
                            getWalking().walk(dest.getNearestTile(range).getRandomizedTile());
                        }else{
                            getWalking().walk(dest.getRandomTile());

                        }
                    }
                }
            }
        }
        return Calculations.random(700, 1500);
    }

    private int antiban(){
        return Calculations.random(5);
    }

    private String getTargetFood(){
        String targetFood = "";
        //inventory.all(i -> i != null)

        return targetFood;
    }
}