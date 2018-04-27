import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;

@ScriptManifest(category = Category.MINING, name = "McMine", author = "Bmc", version = 1.0)

public class McMine extends AbstractScript{
    private enum ScriptState {IDLE, COPPER, TIN, SMELTING, DROPPING};
    private enum Item {COPPER, TIN, BRONZE, ALL};
    private Tile tinLoc, copperLoc;
    private GameObject tinRock, copperRock, furnace;
    private Inventory inv;

    private ScriptState state;

    @Override
    public void onStart(){
        tinRock = getGameObjects().closest(i -> i != null && i.getModelColors().equals(53));
        copperRock = getGameObjects().closest(i -> i != null && i.getModelColors().equals(4510));
        furnace = getGameObjects().closest(10082);
        inv = getInventory();
        tinLoc = new Tile(3075, 9503, 0);
        copperLoc = new Tile(3083, 9500, 0);

        state = ScriptState.IDLE;
        drop(Item.ALL);
    }

    @Override
    public int onLoop(){
        updateState();

        switch(state){
            case COPPER:
                mine(Item.COPPER);
                return Calculations.random(356, 592);

            case TIN:
                mine(Item.TIN);
                return Calculations.random(356, 592);

            case SMELTING:
                smelt(Item.BRONZE);
                return Calculations.random(356, 592);

            case DROPPING:
                drop(Item.BRONZE);
                return Calculations.random(356, 592);

            default:
                log("State error");
        }

        return Calculations.random(356, 592);
    }

    private void updateState(){
        inv = getInventory();
        if(state == ScriptState.IDLE){
            if(inv.count(436) < 13){
                state = ScriptState.COPPER;
                return;
            }

            if(inv.count(438) < 13){
                state = ScriptState.TIN;
                return;
            }
            state = ScriptState.SMELTING;
            return;
        }

        if(state == ScriptState.SMELTING){
            if(inv.count(2349) >= 13){
                state = ScriptState.DROPPING;
                return;
            }
        }

        if(state == ScriptState.DROPPING){
            if(!inv.contains(2349)){
                state = ScriptState.IDLE;
                return;
            }
        }
    }

    private void mine(Item ore){
        tinRock = getGameObjects().closest(i -> i != null && i.getName().equals("Rock") && i.getModelColors().equals(53));
        copperRock = getGameObjects().closest(i -> i != null && i.getName().equals("Rock") && i.getModelColors().equals(4510));
        GameObject oreToMine = null;

        if(ore == Item.TIN){
            getWalking().walk(tinLoc);
            sleep(Calculations.random(900, 1300));
            oreToMine = tinRock;

        }else if(ore == Item.COPPER){
            getWalking().walk(copperLoc);
            sleep(Calculations.random(900, 1300));
            oreToMine = copperRock;
        }else{
            log("Invalid Ore");
        }


        if(oreToMine != null && oreToMine.exists()){
            oreToMine.interact("Mine");
        }
    }

    private void drop(Item item){
        if(item == Item.ALL){
            inv.dropAllExcept(1265);
        }else if(item == Item.BRONZE){
            inv.dropAll(2349);
        }
    }

    private void smelt(Item item){
        if(item == Item.BRONZE){
            if(inv.contains(436) && inv.contains(438)){
                if(furnace != null && furnace.exists()){
                    inv.getRandom(436, 438).useOn(furnace);
                }
            }
        }
    }
}
