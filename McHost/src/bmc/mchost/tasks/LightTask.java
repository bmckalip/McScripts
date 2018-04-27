package bmc.mchost.tasks;

import com.bmc.mclib.constants.McHouse;
import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.utilities.Timer;

import static com.bmc.mclib.constants.McItems.MARRENTILL_ITEM;
import static com.bmc.mclib.constants.McObjects.LIT_BURNER;
import static com.bmc.mclib.constants.McObjects.UNLIT_BURNER;

public class LightTask extends Task {
    public final int burnLength = 1000 * 60 * 2;

    private Timer burnTimer = new Timer(0);

    public LightTask(McScript s) { super(s); }

    public Timer getBurnTimer(){ return this.burnTimer;}

    private int getNumLitBurners(){return s.getGameObjects().all(LIT_BURNER).size();}

    private int getNumUnlitBurners(){return s.getGameObjects().all(UNLIT_BURNER).size();}

    private boolean areBurnersToLight(){ return this.getNumLitBurners() < 2 && this.getNumUnlitBurners() > 0;}

    private boolean hasFiremakingLevel(){ return s.getSkills().getRealLevel(Skill.FIREMAKING) >= 30;}

    private boolean hasEnoughMarrentill() { return s.getInventory().count(item -> item != null && item.getID() == MARRENTILL_ITEM) >= this.getNumUnlitBurners();}

    private boolean burnerIsOnScreen() {
        return s.getGameObjects().closest(UNLIT_BURNER) != null && s.getGameObjects().closest(UNLIT_BURNER).isOnScreen() ||
               s.getGameObjects().closest(LIT_BURNER) != null && s.getGameObjects().closest(LIT_BURNER).isOnScreen();
    }

    private  void startTimer(){ this.burnTimer.setRunTime(this.burnLength);}

    private int calculateDelay(){ return 1000;}

    @Override
    public boolean validate() {
        boolean result = this.burnTimer.finished() &&
                McHouse.contains(s.getLocalPlayer()) &&
                this.areBurnersToLight() &&
                this.hasEnoughMarrentill() &&
                this.hasFiremakingLevel();
        return result;
    }

    @Override
    public int execute() {
        if(walkTask.validate()) {
            return walkTask.execute();
        }else {
            int startNumLitBurners = this.getNumLitBurners();
            s.getGameObjects().closest(UNLIT_BURNER).interact("Light");
            s.sleepUntil(() -> this.getNumLitBurners() > startNumLitBurners || !s.getLocalPlayer().isAnimating(), (2000));

            if (startNumLitBurners == 0 && this.getNumLitBurners() == 1) {
                this.startTimer();
            }
            return calculateDelay();
        }
    }

    private Task walkTask = new Walk(s);

    private class Walk extends LightTask {
        public Walk(McScript script) {
            super(script);
        }

        @Override
        public boolean validate() {
            return !burnerIsOnScreen();
            //getNumUnlitBurners() >= 1 &&
        }

        @Override
        public int execute() {
            Tile burnerLocation = s.getGameObjects().closest(UNLIT_BURNER).getTile().getRandomizedTile(1);
            s.getWalking().walk(burnerLocation);
            return 800;
        }
    }
}
