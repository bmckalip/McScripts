package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.methods.container.impl.bank.BankLocation;

import java.util.Random;

public class OpenBankTask extends Task {
    public OpenBankTask(McScript s) {
        super(s, "Banking Robes");
    }

    @Override
    public boolean validate() {
        return !s.getInventory().isEmpty() && !s.getBank().isOpen() && s.getLocalPlayer().getZ() == 0;
    }

    @Override
    public void execute() {
        Random r = new Random();
        s.getBank().open(BankLocation.EDGEVILLE);
        delay = r.nextInt(700) + 300;
    }
}
