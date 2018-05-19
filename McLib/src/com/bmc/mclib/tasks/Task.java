package com.bmc.mclib.tasks;

import com.bmc.mclib.script.McScript;

import java.util.Random;

public abstract class Task {
    public boolean enabled = true;
    public int delay = 0;
    public static Task state;
    public McScript s;
    public Random r = new Random();

    protected Task(McScript script){
        this.s = script;
        refreshObjects();
    }

    public final int doExecute(){
        prologue();
        execute();
        return delay;
    }

    private final void prologue(){
        state = this;
        refreshObjects();
    }

    public void refreshObjects(){}

    protected abstract void execute();
    public abstract boolean validate();
}
