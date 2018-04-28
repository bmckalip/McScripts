package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.wrappers.interactive.NPC;

public class GainMonasteryAccess extends Task {
    private NPC abbot;
    private String continueMsg = "Click here to continue";

    public GainMonasteryAccess(McScript s) {
        super(s);
    }

    @Override
    public boolean validate() {
        return s.getDialogues().inDialogue();
    }

    @Override
    public int execute() {
        Task.previousTask = toString();
        if(s.getDialogues().canContinue()) {
            s.getDialogues().spaceToContinue();
        }else if(s.getDialogues().canEnterInput()){
            s.getDialogues().chooseOption(1);
        }
        return 200;
    }

    @Override
    public String toString() {
        return "Talking to Monk";
    }
}
