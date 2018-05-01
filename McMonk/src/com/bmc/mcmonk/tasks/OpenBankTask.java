package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.methods.container.impl.bank.BankLocation;

import java.util.Random;

public class OpenBankTask extends Task {
    public OpenBankTask(McScript s) {
        super(s);
    }

    @Override
    public boolean validate() {
        return !s.getInventory().isEmpty() && !s.getBank().isOpen() && s.getLocalPlayer().getZ() == 0;
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        Random r = new Random();
        s.getBank().open(BankLocation.EDGEVILLE);
        return r.nextInt(700) + 300;
    }

    private boolean isFull(){
        return false;
    }

    @Override
    public String toString(){
        return "Banking Robes";
    }

}
