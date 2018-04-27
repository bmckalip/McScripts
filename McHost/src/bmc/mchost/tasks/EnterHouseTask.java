package bmc.mchost.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.utilities.Timer;


public class EnterHouseTask extends Task {
    public EnterHouseTask(McScript s, Timer burnTimer){
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
