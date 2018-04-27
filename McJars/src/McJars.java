/*
TODO:
define areas, items
implement executionary methods
handle transition case where in puro-puro without ROD
determine proper run threshold and shed dist threshold
implement antiban
define shed door object
 */

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.magic.Spell;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import sun.management.counter.perf.PerfLongArrayCounter;

@ScriptManifest(category = Category.MONEYMAKING, name = "McJars", author = "Bmc", version = 1.0, description="Exchanges impling jars at Puro-Puro")

public class McJars extends AbstractScript{
    private final int delay = 300; //ms base loop delay
    private final int AB_likelihood = 0; //% chance of antiban triggering;
    private final int RUN_THRESHOLD = 30;
    private final int SHED_DISTANCE_THRESHOLD = 70; //maximum blocks player can be from the shed in order to run to it when not within a valid area

    private enum ScriptState {TRANSIT, BANKING, EXCHANGING, STOP};

    //areas and tiles
    private final Area
            ZANARIS = new Area(),
            LUMBRIGE = new Area(),
            PURO_PURO = new Area(),
            CW_BANK = new Area(),
            LUMBY_BANK = new Area(),
            ZANARIS_BANK = new Area(),
            LUMBRIGE_SWAMP = new Area();

    private final Tile SHED_DOOR = new Tile();

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

    //objects that will (should) always exist
    private Player player;
    private Inventory inv;
    private Equipment gear;

    //objects that could be null
    private Entity shopOwner;
    private GameObject cropPortal;

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
        //shopOwner = ..
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
               (inv.contains(ANY_STAMINA_DOSE) || isStaminaActive() || (getWalking().getRunEnergy() > RUN_THRESHOLD))
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
        }else if(LUMBRIGE_SWAMP.contains(player)){
            if((inv.count(FILLED_JAR) == 9) && gear.contains(ANY_ROD) && (gear.contains(DRAMEN_STAFF) || gear.contains(LUNAR_STAFF))){
                destination = PURO_PURO;
                return ScriptState.TRANSIT;
            }else{
                destination = getBank().getClosestBankLocation().getArea(1);
                return ScriptState.TRANSIT;
            }
        }else if(ZANARIS.contains(player)){
            if((inv.count(FILLED_JAR) == 9)){
                destination = PURO_PURO;
                return ScriptState.TRANSIT;
            }else{
                destination = ZANARIS_BANK;
                return ScriptState.TRANSIT;
            }
        }else if(PURO_PURO.contains(player)) {
            if (inv.contains(FILLED_JAR)) {
                return ScriptState.EXCHANGING;
            } else {
                if (gear.contains(ANY_ROD)) {
                    destination = CW_BANK;
                    return ScriptState.TRANSIT;
                } else {
                    destination = LUMBY_BANK;
                    return ScriptState.TRANSIT;
                    //TODO: if player can home port, do that, otherwise use portal and walk to closet bank
                }
            }
        }else{
            if(playerHasAllItems() && Calculations.distance(player.getTile(), SHED_DOOR) < SHED_DISTANCE_THRESHOLD){
                destination = PURO_PURO;
                return ScriptState.TRANSIT;
            }else {
                if(gear.contains(ANY_ROD)){
                    destination = CW_BANK;
                    return ScriptState.TRANSIT;
                }else{
                    //no bank should be accesible from puro puro, so this is a problem
                    //cast lumby home teleport getMagic()....
                    destination = getBank().getClosestBankLocation().getArea(1);
                    return ScriptState.TRANSIT;
                }
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

    private boolean isStaminaActive(){
        return getPlayerSettings().getConfig(638) != 0;
    }

    private void withdrawRequiredItems(){
        //provides no garuntees as to whether or not player is near a bank
        if(!getBank().isOpen()){
            getBank().open();
        }

        //if(!inv.contains(!getMagic().))
    }

    //EXECUTIONARY FUNCTIONS:
    private int transit(Area destination){
        if(!destination.contains(player)){
            if(destination == CW_BANK){
                //teleport to cw
            }else if(destination == PURO_PURO){
                if(CW_BANK.contains(player)){
                    //teleport to lumby
                }else if(LUMBRIGE.contains(player) || LUMBRIGE_SWAMP.contains(player) ||
                         Calculations.distance(player.getTile(), SHED_DOOR) < SHED_DISTANCE_THRESHOLD) {
                     getWalking().walk(SHED_DOOR);
                }else{
                    getWalking().walk(getBank().getClosestBankLocation().getArea(1).getCenter());
                }
            }else if(destination == LUMBY_BANK) {
                getWalking().walk(LUMBY_BANK.getCenter());
            }
        }

        return delay;
    }

    private int bank(){
        if(CW_BANK.contains(player) || LUMBY_BANK.contains(player) || ZANARIS_BANK.contains(player) ||
           (Calculations.distance(player.getTile(), getBank().getClosestBankLocation().getCenter()) < 15)
          ){
            withdrawRequiredItems();
        }
        return delay;
    }

    private int exchange(){
        return delay;
    }

    private int antiban(){
        return delay;
    }

}