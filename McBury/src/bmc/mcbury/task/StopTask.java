package bmc.mcbury.task;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class StopTask extends Task {
    private int BONES;

    public StopTask(McScript s, int BONES) {
        super(s);
        this.BONES = BONES;
    }

    @Override
    public boolean validate() {
        return s.getBank().isOpen() && !s.getBank().contains(this.BONES) && !s.getInventory().contains(this.BONES);
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        s.getBank().close();
        s.sleepUntil(() -> !s.getBank().isOpen(), 3000);
        s.stop();
        return 0;
    }

    @Override
    public String toString() {
        return "StopTask";
    }
}
