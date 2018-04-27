package bmc.mcbury.task;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class OpenBankTask extends Task {
    private int BONES;

    public OpenBankTask(McScript s, int BONES) {
        super(s);
        this.BONES = BONES;
    }

    @Override
    public boolean validate() {
        return !s.getBank().isOpen() && !s.getInventory().contains(this.BONES);
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        s.getBank().openClosest();
        return 500;
    }

    @Override
    public String toString() {
        return "OpenBankTask";
    }
}
