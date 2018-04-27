package mcgrain;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.map.Area;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.MessageListener;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(category = Category.MONEYMAKING, name = "McGrain", author = "Bmc", version = 1.0, description="grinds grain into flour at draynor")

public class McGrain extends AbstractScript implements MessageListener{
    private enum ScriptState {ANTIBAN, TRANSIT, BANKING, GRINDING, COLLECTING, STOP};

    private String POT_OF_FLOUR = "Pot of flour";
    private String POT = "Pot";
    private String GRAIN = "Grain";

    private Area bank, windmill_1, windmill_2, windmill_3;

    private ScriptState state;
    private Inventory inv;
    private Area destination = null;
    private Entity player;

    private GameObject hopper, controls, bin, ladder, door;

    private int count, prev;
    private Message gameMessage = null;

    @Override
    public void onStart(){
        log("Started");

        bank = new Area(3097, 3246, 3092, 3240, 0);
        windmill_1 = new Area(3168, 3305, 3165, 3307, 0);
        windmill_2 = new Area(3164, 3305, 3167, 3308, 1);
        windmill_3 = new Area(3164, 3305, 3168, 3308, 2);

        state = ScriptState.COLLECTING;

        update();

        prev = inv.count(GRAIN);

    }

    @Override
    public int onLoop(){
        ScriptState temp = state;
        update();
        if(state != temp) log("State:" + state.toString());
        return execute();
    }

    public void onPaint(){

    }

    private void update(){
        inv = getInventory();
        player = getLocalPlayer();
        count = inv.count(GRAIN);
        hopper = getGameObjects().closest(24961);
        controls = getGameObjects().closest(24964);
        bin = getGameObjects().closest(1781);
        door = getGameObjects().closest("Large Door");
        ladder = getGameObjects().closest("Ladder");

        switch(state){
            case BANKING:
                if(!bank.contains(player)){
                    destination = bank;
                    state = ScriptState.TRANSIT;
                }

                if(inv.count(POT) == 14 && inv.count(GRAIN) == 14){
                    destination = windmill_3;
                    state = ScriptState.TRANSIT;
                }
                break;

            case TRANSIT:
//                log(destination.getCenter().getTile().toString());
                if(windmill_3.contains(player) && destination == windmill_3){
                    state = ScriptState.GRINDING;
                    getCamera().mouseRotateToEntity(hopper);
                }else if(bank.contains(player) && destination == bank){
                    state = ScriptState.BANKING;
                }else if(windmill_1.contains(player) && destination == windmill_1){
                    state = ScriptState.COLLECTING;
                }
                break;

            case GRINDING:
                if(inv.count(GRAIN) == 0){
                    destination = windmill_1;
                    state = ScriptState.TRANSIT;
                }
                break;

            case COLLECTING:
                if(bin == null){
                    destination = windmill_1;
                    state = ScriptState.TRANSIT;
                }else if(inv.count(POT) == 0 || !bin.hasAction("Collect")){
                    state = ScriptState.TRANSIT;
                    destination = bank;
                }

                if(inv.count(GRAIN) > 0 && !bank.contains(player)){
                    state = ScriptState.TRANSIT;
                    destination = windmill_3;
                }
                break;
        }

        if(state == ScriptState.ANTIBAN){
            state = ScriptState.BANKING;
        }


    }

    private int  execute(){
        antiban(10);
        switch(state){
            case TRANSIT:
                return walk(destination);
            case BANKING:
                return bank();
            case GRINDING:
                return grind();
            case COLLECTING:
                return collect();
            case STOP:
                //exit script here
            default:
                log("invalid state");
                return Calculations.random(300, 500);
        }
    }

    private int bank(){
        if(bank.contains(getLocalPlayer())){
            if(!getBank().isOpen()) {
                getBank().open();
            }

            if(getBank().isOpen()){
                if(getBank().count(1931) == 14 && getBank().count(1947) == 14){
                    getBank().depositAll(i -> i != null && i.getID() == 1933);
                    getBank().withdraw(i-> i != null && i.getID() == 1947, 14 - inv.count(1947));
                    getBank().withdraw(i-> i != null && i.getID() == 1931, 14 - inv.count(1931));
                    getBank().close();
                }else{
                    getBank().close();
                    sleep(Calculations.random(900, 2000));
                    stop();
                }
            }
        }
        return Calculations.random(400, 900);
    }

    private int walk(Area destination){
        boolean obstacle = false;
        if(destination == null) return 0;
//        log(destination.getCenter().toString());
        if(!destination.contains(getLocalPlayer())){

            if(windmill_1.contains(player.getTile()) || windmill_2.contains(player.getTile()) || windmill_3.contains(player.getTile())){
                // log("in mill");
                if(ladder != null && ladder.isOnScreen()){
                    if(getLocalPlayer().distance(ladder) > 3){
                        getCamera().mouseRotateToEntity(ladder);
                    }
                }

                if(destination == windmill_1){
                    //log("null");
                    if(ladder != null && ladder.hasAction("Climb-down")){
                        ladder.interact("Climb-down");
                        obstacle = true;
                    }
                }else if(destination == windmill_3){
                    //log(ladder.toString());
                    //log(ladder.getActions().toString());
                    if(ladder != null && ladder.hasAction("Climb-up")){
                        ladder.interact("Climb-up");
                        obstacle = true;
                    }
                }
                if(door != null && destination == bank){
                    if(door.hasAction("Open")){
                        door.interact("Open");
                        obstacle = true;
                    }
                }
            }else if(door != null && destination == windmill_3){
                if(door.isOnScreen()){
                    getCamera().mouseRotateToEntity(door);
                }
                if(door.hasAction("Open")){
                    door.interact("Open");
                    obstacle = true;
                }
            }

            if(!obstacle){
                if(getWalking().getDestination() == null || getWalking().getDestination().distance(player.getTile()) < Calculations.random(5, 10)){
                    getWalking().walk(destination.getRandomTile());
                }
            }
        }
        return Calculations.random(200, 2000);
    }

    private int grind(){
        //todo: need to dd running multiplier here to get timeing right or use sleepuntil


        if(hopper != null && controls != null){
            if(inv.contains(GRAIN)){
                if(count < prev || (gameMessage != null && gameMessage.getMessage().equalsIgnoreCase("There is already grain in the hopper."))){
                    if(count == prev) gameMessage = null;
                    if(controls.hasAction("Operate")){
                        while(getLocalPlayer().isAnimating() || getLocalPlayer().isMoving()){
                            sleep(200);
                        }
                        sleep(500);

                        if(controls.interact("Operate")){
                            prev = count;
                        }
                    }else{
                        getWalking().walk(hopper);
                    }
                }else{
                    if(!getLocalPlayer().isAnimating() && !getLocalPlayer().isMoving()){
                        inv.get(1947).useOn(hopper);
                        sleep(700);

                    }
                }
            }
        }
        return Calculations.random(700, 900);
    }

    private int collect(){

        if(inv.contains(POT)){
            if(bin != null){
                bin.interact("Empty");
            }
        }
        return Calculations.random(10, 30);
    }

    private void cameraAdjustment(int likelihood){
        if(Calculations.random(100) <= likelihood){
            if(Calculations.random(-1, 2) == 0){
                //log("Antiban Camera: generic");
                getCamera().rotateToPitch(Calculations.random(100));
            }else{
                //log("Antiban Camera: specialized");
                switch(state){
                    case TRANSIT:
                        if(Calculations.random(2) == 1){
                            getCamera().mouseRotateToEntity(getGameObjects().closest(i -> i != null && i.isOnScreen()));
                        }else{
                            getCamera().rotateToEntity(getGameObjects().closest(i -> i != null && i.isOnScreen()));
                        }
                        break;
                    case COLLECTING:
                        if(Calculations.random(2) == 1){
                            getCamera().mouseRotateToEntity(getGameObjects().closest(i -> i != null && i.isOnScreen() && i.getName().equals("Flour Bin")));
                        }else{
                            getCamera().rotateToEntity(getGameObjects().closest(i -> i != null && i.isOnScreen() && i.getName().equals("Flour Bin")));
                        }
                        break;
                    case GRINDING:
                        if(Calculations.random(2) == 1){
                            getCamera().mouseRotateToEntity(getGameObjects().closest(i -> i != null && i.isOnScreen() && i.getName().equals("Hopper Controls")));
                        }else{
                            getCamera().rotateToEntity(getGameObjects().closest(i -> i != null && i.isOnScreen() && i.getName().equals("Hopper Controls")));
                        }
                        break;
                    case BANKING:
                        break;
                }
            }
        }
    }

    private void mouseAdjustment(int likelihood){
        if(Calculations.random(100) <= likelihood){
            //log("Antiban: mouse");
            getMouse().move(getGameObjects().all(i -> i != null && i.isOnScreen()).get(0));
        }
    }

    private void examine(int likelihood){
        //log("antiban: examine");
        GameObject obj = getGameObjects().all(i -> i != null && i.isOnScreen()).get(0);
//        obj.interact("Examine");

    }

    private void antiban(int likelihood){
        switch(state){
            case TRANSIT:
                mouseAdjustment(likelihood);
                cameraAdjustment(likelihood);
//                examine(likelihood / 3);
                break;
            case COLLECTING:
                break;
            case GRINDING:
//                mouseAdjustment(likelihood);
//                cameraAdjustment(likelihood);
                break;
            case BANKING:
                mouseAdjustment(likelihood);
//                cameraAdjustment(likelihood);
//                examine(likelihood / 3);
                break;
        }
    }

    @Override
    public void onGameMessage(Message message){
        gameMessage = message;
    }

    @Override
    public void onPlayerMessage(Message message){}

    @Override
    public void onPrivateInMessage(Message message){}

    @Override
    public void onPrivateOutMessage(Message message){}

    @Override
    public void onTradeMessage(Message message){}

}