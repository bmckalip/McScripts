package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class DepositTask extends Task {
    public DepositTask(McScript s){ super(s, "Depositing Items"); }

    @Override
    public boolean validate() {
        return s.getBank().isOpen() && (!s.getInventory().isEmpty() || !s.getEquipment().isEmpty());
    }

    @Override
    public void execute() {
        s.getBank().depositAllItems();
        s.getBank().depositAllEquipment();
        delay = r.nextInt(100) + 400;
    }
}
