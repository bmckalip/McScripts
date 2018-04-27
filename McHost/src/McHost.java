import bmc.mchost.tasks.*;
import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import com.bmc.mclib.taskmanager.TaskList;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

@ScriptManifest(category = Category.MISC, author = "BMC", version=0.1, name="McHost")
public class McHost extends McScript{

    @Override
    public void initializeTasks() {
        LightTask lightTask = new LightTask(this);
        EnterHouseTask enterHouseTask = new EnterHouseTask(this, lightTask.getBurnTimer());
        ExitHouseTask exitHouseTask = new ExitHouseTask(this);
        ExchangeTask exchangeTask = new ExchangeTask(this);
        ChatTask chatTask = new ChatTask(this);

        Task[] tasks = {lightTask, enterHouseTask, exitHouseTask, exchangeTask, chatTask};
        setTasks(new TaskList(tasks));
    }
}
