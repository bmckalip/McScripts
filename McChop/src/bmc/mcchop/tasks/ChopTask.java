package bmc.mcchop.tasks;

import com.bmc.mclib.tasks.defaulttasks.DefaultChopTask;
import com.bmc.mclib.script.McScript;

public class ChopTask extends DefaultChopTask{
    public ChopTask(McScript s){
        super(s);
    }

    @Override
    public boolean validate() {
        return this.containsAnyAxe();
    }

    @Override
    public int execute() {
        s.log("Chopping");
        return 1000;
    }
}
