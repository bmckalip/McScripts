package bmc.mchost.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

/**
 * Created by Brian on 3/2/2018.
 */
public class ExitHouseTask extends Task {
    public ExitHouseTask(McScript s) {
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
