package bmc.mcbury.task;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class BuryTask extends Task {
    private int BONES;

    public BuryTask(McScript s, int BONES) {
        super(s);
        this.BONES = BONES;
    }

    @Override
    public boolean validate() {
        return !s.getBank().isOpen() && s.getInventory().contains(this.BONES);
    }

    @Override
    public void execute() {
        s.getInventory().get(this.BONES).interact("Bury");
        delay = r.nextInt(100) + 100;
    }

    @Override
    public String toString() {
        return "BuryTask";
    }
}
