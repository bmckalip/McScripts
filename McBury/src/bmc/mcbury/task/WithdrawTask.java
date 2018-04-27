package bmc.mcbury.task;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class WithdrawTask extends Task {
    private int BONES;

    public WithdrawTask(McScript s, int BONES) {
        super(s);
        this.BONES = BONES;
    }

    @Override
    public boolean validate() {
        return s.getBank().isOpen() && s.getBank().contains(this.BONES) && (s.getInventory().count(this.BONES) < 28);
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        s.getBank().withdrawAll(this.BONES);
        return 500;
    }

    @Override
    public String toString() {
        return "WithdrawTask";
    }
}
