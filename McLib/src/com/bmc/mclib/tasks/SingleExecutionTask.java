package com.bmc.mclib.tasks;

import com.bmc.mclib.script.McScript;

public class SingleExecutionTask extends Task {
    public SingleExecutionTask(McScript s) {
        super(s);
    }

    @Override
    public boolean validate() {
        this.removeTask();
        return false;
    }

    @Override
    public int execute() {
        this.removeTask();
        return 0;
    }

    private void removeTask() {
        s.getTasks().removeTask(this);
    }
}
