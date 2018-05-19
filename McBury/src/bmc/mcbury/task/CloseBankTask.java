package bmc.mcbury.task;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class CloseBankTask extends Task {
    private int BONES;

    public CloseBankTask(McScript s, int BONES) {
        super(s, "CloseBankTask");
        this.BONES = BONES;
    }

    @Override
    public boolean validate() {
        return s.getBank().isOpen() && (s.getInventory().count(this.BONES) == 28 || !s.getBank().contains(this.BONES));
    }

    @Override
    public void execute() {
        s.getBank().close();
        delay = 500;
    }
}
