import bmc.mcchop.tasks.*;
import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import com.bmc.mclib.taskmanager.TaskList;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

@ScriptManifest(category = Category.WOODCUTTING, author = "BMC", version=0.1, name="McChop")
public final class McChop extends McScript{
    @Override
    public TaskList initializeTasks() {
        Task[] tasks = {new BankTask(this), new ChopTask(this), new WalkTask(this)};
        return new TaskList(tasks);
    }
}
