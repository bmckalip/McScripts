package com.bmc.mclib.tasks;

import com.bmc.mclib.script.McScript;

public abstract class Task {
    protected int delay;
    public boolean enabled = true;
    public McScript s;
    public abstract boolean validate();
    public abstract int execute();
    public Task(McScript script){
        this.s = script;
    }
    public static String previousTask;

}
