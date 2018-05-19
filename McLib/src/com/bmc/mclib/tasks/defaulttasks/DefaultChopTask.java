package com.bmc.mclib.tasks.defaulttasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class DefaultChopTask extends Task{
    public DefaultChopTask(McScript script){super(script);}

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public int execute() {
        return 0;
    }

    public boolean containsAnyAxe(){
        return false;
    }
}
