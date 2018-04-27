package bmc.mcbury.task;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

import java.util.Random;

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
    public int execute() {
        Task.previousTask = toString();
        s.getInventory().get(this.BONES).interact("Bury");
        return new Random().nextInt(100) + 100;
    }

    @Override
    public String toString() {
        return "BuryTask";
    }
}
