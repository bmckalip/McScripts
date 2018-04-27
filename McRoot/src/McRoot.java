//TODO:

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.Player;

@ScriptManifest(category = Category.HUNTING, name = "McRoot", author = "Bmc", version = 1.0, description="Feeds oggleroots to rabbits in vinesweeper for hunter xp")

public class McRoot extends AbstractScript{
    private final int delay = 300; //ms base loop delay
    private final int AB_likelihood = 20; //% chance of antiban triggering;
    private final int RUN_THRESHOLD = 50;

    private enum ScriptState {FEEDING, EXCHANGING, STOP, TRANSIT, BANKING};

    //areas
    private final Area
            ZANARIS = new Area(),
            LUMBRIGE = new Area(),
            PURO_PURO = new Area(),
            CW_BANK = new Area(),
            LUMBY_BANK = new Area(),
            ZANARIS_BANK = new Area();

    //inventory/equipment item id's
    private final int
            FILLED_JAR = 0,
            EMPTY_JAR = 0,
            LAW_RUNE = 0,
            AIR_RUNE = 0,
            EARTH_RUNE = 0,
            DRAMEN_STAFF = 0,
            LUNAR_STAFF = 0;

    private final String ANY_STAMINA_DOSE = "Stamina Potion";
    private final String ANY_ROD = "Ring Of Dueling";

    private Player player;
    private Inventory inv;
    private Equipment gear;

    private Area destination = null;
    private ScriptState currState = null;

    @Override
    public void onStart(){
        updateObjects();
        log("McJars started: ");
    }

    @Override
    public int onLoop(){
        updateObjects();
        return execute();
    }

    private void updateObjects(){
        player = getLocalPlayer();
        inv = getInventory();
        gear = getEquipment();
    }

    private int execute(){
        //AB_likelihood % chance of triggering an antiban action regardless of prevState
        if(Calculations.random(100) <= AB_likelihood) {
            return antiban();
        }

        switch(transition()){
            case TRANSIT:
                return transit(destination);
            case BANKING:
                return bank();
            case EXCHANGING:
                return exchange();
            case STOP:
                //TODO: call stop method here
            default:
                log("Executing on invalid state: " + currState);
                return delay;
        }
    }

    private ScriptState transition(){
        //next state depends on inv, gear, run energy, and location
        if(CW_BANK.contains(player)){
            if(playerHasAllItems()){
                destination = PURO_PURO;
                return ScriptState.TRANSIT;
            }else if(!bankHasRequiredItems()){
                return ScriptState.STOP;
            }else{
                return ScriptState.BANKING;
            }
        }else if(LUMBRIGE.contains(player)){
            if((gear.contains(DRAMEN_STAFF) || gear.contains(LUNAR_STAFF)) &&
                    (inv.count(FILLED_JAR) == 9) &&
                    (inv.contains(ANY_STAMINA_DOSE) || (getWalking().getRunEnergy() > RUN_THRESHOLD))
                    ){
                destination = PURO_PURO;
                return ScriptState.TRANSIT;
            }else{
                if (gear.contains(ANY_ROD)) {
                    destination = CW_BANK;
                    return ScriptState.TRANSIT;
                } else {
                    destination = LUMBY_BANK;
                    return ScriptState.TRANSIT;
                }
            }
        }else if(ZANARIS.contains(player)){
            if((inv.count(FILLED_JAR) == 9)){
                destination = PURO_PURO;
                return ScriptState.TRANSIT;
            }else{
                destination = ZANARIS_BANK;
                return ScriptState.TRANSIT;
            }
        }else if(PURO_PURO.contains(player)){
            if(inv.contains(FILLED_JAR)){
                return ScriptState.EXCHANGING;
            }else{
                if(gear.contains(ANY_ROD)){
                    destination = CW_BANK;
                    return ScriptState.TRANSIT;
                }else{
                    destination = LUMBY_BANK;
                    return ScriptState.TRANSIT;
                    //TODO: if player can home port, do that, otherwise use portal and walk to closet bank
                }
            }
        }else{
            //expand this later, go to bank for now
            if(gear.contains(ANY_ROD)){
                destination = CW_BANK;
                return ScriptState.TRANSIT;
            }else{
                //no bank should be accesible from puro puro, so this is a problem
                destination = getBank().getClosestBankLocation().getArea(1);
                return ScriptState.TRANSIT;
            }
        }
    }

    //HELPER FUNCTIONS
    private boolean playerHasAllItems(){
        if ((gear.contains(DRAMEN_STAFF) || gear.contains(LUNAR_STAFF)) &&
                (gear.contains(i -> i.getName().equals(ANY_ROD))) &&
                (inv.count(FILLED_JAR) == 9) &&
                (inv.count(LAW_RUNE) == 1) &&
                (inv.count(EARTH_RUNE) == 1) &&
                (inv.count(AIR_RUNE) == 3) &&
                (inv.count(i -> i.getName().equals(ANY_STAMINA_DOSE)) == 1)
                ){
            return true;
        }else{
            return false;
        }
    }

    private boolean bankHasRequiredItems(){
        Bank bank = getBank();
        if ((bank.count(FILLED_JAR) >= 9) &&
                (bank.count(AIR_RUNE) >= 3) &&
                (bank.containsAll(LAW_RUNE, EARTH_RUNE)) &&
                (bank.contains(i -> i.getName().equals(ANY_STAMINA_DOSE))) &&
                (bank.contains(i -> i.getName().equals(ANY_ROD)))
                ){
            return true;
        }else{
            return false;
        }
    }

    //EXECUTIONARY FUNCTIONS:
    private int transit(Area destination){
        return delay;
    }

    private int bank(){
        return delay;
    }

    private int exchange(){
        return delay;
    }

    private int antiban(){
        return delay;
    }

}