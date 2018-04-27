package com.bmc.mclib.tasks.defaulttasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public abstract class DefaultBankTask extends Task {
    public DefaultBankTask(McScript s){
        super(s);
    }
    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public int execute() {
        return 0;
    }
}
