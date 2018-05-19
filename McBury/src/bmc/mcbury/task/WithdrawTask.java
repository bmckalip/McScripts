package bmc.mcbury.task;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class WithdrawTask extends Task {
    private int BONES;

    public WithdrawTask(McScript s, int BONES) {
        super(s, "WithdrawTask");
        this.BONES = BONES;
    }

    @Override
    public boolean validate() {
        return s.getBank().isOpen() && s.getBank().contains(this.BONES) && (s.getInventory().count(this.BONES) < 28);
    }

    @Override
    public void  execute() {
        s.getBank().withdrawAll(this.BONES);
        delay = 500;
    }
}
