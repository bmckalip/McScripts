import bmc.mcbury.task.*;
import com.bmc.mclib.script.McScript;
import com.bmc.mclib.taskmanager.TaskList;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

@ScriptManifest(category = Category.PRAYER, author = "BMC", version = 0.1, name = "McBury")
public class McBury extends McScript {
    public int BIG_BONES = 532;
    public boolean stop = false;

    @Override
    public void initializeTasks() {
        BuryTask buryTask = new BuryTask(this, BIG_BONES);
        OpenBankTask openBankTask = new OpenBankTask(this, BIG_BONES);
        StopTask stopTask = new StopTask(this, BIG_BONES);
        WithdrawTask withdrawTask = new WithdrawTask(this, BIG_BONES);
        CloseBankTask closeBankTask = new CloseBankTask(this, BIG_BONES);

        Task[] tasks = {buryTask, openBankTask, stopTask, withdrawTask, closeBankTask};
        setTasks(new TaskList(tasks));
    }

    @Override
    public void createGUI() {

    }
}
