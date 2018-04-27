package bmc.mcbury.task;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class CloseBankTask extends Task {
    private int BONES;

    public CloseBankTask(McScript s, int BONES) {
        super(s);
        this.BONES = BONES;
    }

    @Override
    public boolean validate() {
        return s.getBank().isOpen() && (s.getInventory().count(this.BONES) == 28 || !s.getBank().contains(this.BONES));
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        s.getBank().close();
        return 500;
    }

    @Override
    public String toString() {
        return "CloseBankTask";
    }
}