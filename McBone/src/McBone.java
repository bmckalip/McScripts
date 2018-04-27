import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.world.*;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;

@ScriptManifest(category = Category.PRAYER, name = "McBone", author = "Bmc", version = 1.0)
public class McBone extends AbstractScript{

    private enum scriptState { Gather, Bury, Walk, AntiBan}
    private int[] worlds = {1, 8, 16, 26, 35, 82, 83, 84, 93, 94};
    private scriptState state;
    private String bones;
    private int bonesBuried;
    Area goblinArea = new Area(3264, 3254, 3245, 3232, 0);
    Area goblinHut = new Area(3248, 3248, 3243, 3244, 0);
    Area cowField = new Area(3265, 3255, 3253, 3298, 0);
    Area lootArea;

    Tile doorLoc = new Tile(3246, 3244);
    private double speedMultiplier;
    private double idleTime;
    private scriptState prev;
    private GameObject door;
    private GroundItem bone;

    @Override
    public void onStart(){
        log("Script started");
        state = scriptState.Gather;
        bones = "Bones";
        bonesBuried = 0;
        speedMultiplier = 1;
        getWalking().setRunThreshold(30);
        idleTime = 0;
        prev = state;
        door = getGameObjects().getTopObjectOnTile(doorLoc);
        lootArea = cowField;
    }

    @Override
    public int onLoop(){
        if(true){
//            log(state.toString());
        }

        if(!lootArea.contains(getLocalPlayer())){
            state = scriptState.Walk;
        }

        if(getWalking().isRunEnabled()){
            speedMultiplier = 2;
        }else{
            speedMultiplier = 1.2;
            if(getWalking().getRunEnergy() >= getWalking().getRunThreshold()){
                getWalking().toggleRun();
//                log(Integer.toString(getWalking().getRunThreshold()));
            }
        }

        if(Calculations.random(50) == 0){
            state = scriptState.AntiBan;
        }

        switch(state){
            case Gather:
                gatherBone();
                break;
            case Bury:
                buryBones(Calculations.random(10, 20));
                break;
            case Walk:
                if(!lootArea.contains(getLocalPlayer()))
                    walkToArea(lootArea);
                else{
                    state = scriptState.Gather;
                }
            case AntiBan:
                antiBan();
                break;
            default:
                log("state error");
                break;
        }


        prev = state;
        return Calculations.random(347, 529);
    }

    private void gatherBone(){

        bone = getGroundItems().closest(i -> i != null && i.exists() && i.getName().contains(bones) && lootArea.contains(i));

        //if we're loting lummy goblins, handle the hut door correctly:
        if(lootArea == goblinArea){
            //if the door is closed and I either need to leave or enter, open the door
            if(door != null && door.getOrientation() == 8){
                log("doorClosed");
                if(goblinHut.contains(bone) ^ goblinHut.contains(getLocalPlayer())){
                    log("opening door");
                    door.interact("Open");
                    //if far away, sleep longer to avoid repeat clicks
                    sleep(Calculations.random((int)(door.distance() * 750 / speedMultiplier) + 430, (int)(door.distance() * 750 / speedMultiplier) + 532));
                }
            }
        }

        if(getInventory().emptySlotCount() > Calculations.random(0, 6)){
            if(bone == null) {
                int idleFor = Calculations.random(1067, 4978);
                idleTime += idleFor;
                log("No bones, idling for: " + Double.toString(idleTime / 1000) + " seconds");
                walkToArea(lootArea.getRandomTile().getArea(1));
                sleep(idleFor);

                //if idle for more than specified time, hop worlds
                if(idleTime / 1000 >= Calculations.random(25, 30)){
                    World newWorld = getWorlds().getRandomWorld(i -> i.isF2P() && !i.equals(getClient().getCurrentWorld()) && i.getMinimumLevel() < 250);
                    getWorldHopper().hopWorld(newWorld);
                    log("hopping to world: " + newWorld.toString());
                    idleTime = 0.0;
                }
                return;
            }else{
                idleTime = 0;
            }
            if(!bone.exists()){
                log("bone does not exist at location: " + bone.getTile().toString());
                return;
            };
            if(!lootArea.contains(bone)){
                log("bone is not in region: " + bone.getTile().toString());
                return;
            };
            if(!bone.isOnScreen()){
                log("locating off-screen bone: " + bone.getTile().toString());
                getCamera().mouseRotateToEntity(bone);
                sleep(Calculations.random(103, 209));
                walkToArea(bone.getSurroundingArea(1));
            }else{
                bone.interact("Take");
            }

            //if far away, sleep longer to avoid repeat clicks
            if(bone != null){
                int sleepFor = Calculations.random((int)(bone.distance() * 750 / speedMultiplier) + 330, (int)(bone.distance() * 750 / speedMultiplier) + 532);
                sleep(sleepFor);
            }
        }else{
            state = scriptState.Bury;
        }
    }

    private void buryBones(int count){
        Inventory inv = getInventory();
        int prevAmt = inv.count(bones);
        int currAmt = prevAmt;
        for(int i = 0; i < count * 4; i++){
            if(inv.contains(bones)){
                prevAmt = inv.count(bones);
                inv.interact(bones, "bury");
                sleep(Calculations.random(75, 250));
                currAmt = inv.count(bones);
                if(currAmt < prevAmt){
                    bonesBuried += prevAmt - currAmt;
                    prevAmt = currAmt;
                }
            }
        }
        state = scriptState.Gather;
        log(Integer.toString(bonesBuried) + " bones buried.");
    }

    private void antiBan(){
        log("performing antiban");
        sleep(Calculations.random(10000));

//        getGameObjects().closest("Cow").interact("Examine");

//        int rand = Calculations.random(5);
//        switch(rand){
//            case 0:
////                getGameObjects().all().
//                break;
//            case 1:
//                break;
//            case 2:
//                break;
//            case 3:
//                break;
//            case 4:
//                break;
//            default:
//                log("performing antiban: sleep");
//                sleep(Calculations.random(10000));
//        }
        state = scriptState.Gather;
    }

    private void walkToArea(Area area){
        Tile loc = area.getRandomTile();
        if(lootArea.contains(loc)){
            getWalking().walk(loc);
            sleep(Calculations.random(1278, 2992));
        }
    }
}
