package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

import java.util.Random;

public class DepositTask extends Task {
    public DepositTask(McScript s){
        super(s);
    }

    @Override
    public boolean validate() {
        return s.getBank().isOpen() && (!s.getInventory().isEmpty() || !s.getEquipment().isEmpty());
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        Random r = new Random();
        s.getBank().depositAllItems();
        s.getBank().depositAllEquipment();
        return r.nextInt(100) + 400;
    }

    @Override
    public String toString(){
        return "Depositing items";
    }
}
