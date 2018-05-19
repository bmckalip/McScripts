package bmc.mcbury.task;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class StopTask extends Task {
    private int BONES;

    public StopTask(McScript s, int BONES) {
        super(s, "StopTask");
        this.BONES = BONES;
    }

    @Override
    public boolean validate() {
        return s.getBank().isOpen() && !s.getBank().contains(this.BONES) && !s.getInventory().contains(this.BONES);
    }

    @Override
    public void execute() {
        s.getBank().close();
        s.sleepUntil(() -> !s.getBank().isOpen(), 3000);
        s.stop();
    }
}
