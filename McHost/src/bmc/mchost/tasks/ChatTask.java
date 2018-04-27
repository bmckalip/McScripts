package bmc.mchost.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;

public class ChatTask extends Task {
    public ChatTask(McScript s) {
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
