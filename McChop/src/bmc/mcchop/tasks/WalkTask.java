package bmc.mcchop.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class WalkTask extends Task{
    public WalkTask(McScript s){
        super(s);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public int execute() {
        s.log("Walking");
        return 1000;
    }
}
