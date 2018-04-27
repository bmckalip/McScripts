package bmc.mcchop.tasks;

import com.bmc.mclib.tasks.defaulttasks.DefaultBankTask;
import com.bmc.mclib.script.McScript;

public class BankTask extends DefaultBankTask {
    public BankTask(McScript s){
        super(s);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public int execute() {
        s.log("Banking");
        return 1000;
    }
}
