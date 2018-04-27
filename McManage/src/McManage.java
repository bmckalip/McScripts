import org.dreambot.api.Client;
import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.core.Instance;

import java.awt.*;

/**
 * Created by Brian on 2/28/2018.
 */
@ScriptManifest(category = Category.MISC, name="McManage", author = "BMC", version=0.1)
public class McManage extends AbstractScript{
    @Override
    public int onLoop() {
        return 1000;
    }

    @Override
    public void onStart() {
        super.onStart();
        ThreadGroup tg = new ThreadGroup("new Thread group");
//        ScriptManager sm = new ScriptManager();
//        getClient().createNewContext();
//        this.registerMethodContext(getClient());

//        Integer newClient = 1;
//        Instance i = new Instance();
//        Client newClient = new Client(i);
        // + Client.getClient().getHost()
//        ScriptManager = new ScriptManager();
        log("CLIENT:" + getClient());
//        Client.getClient().setContext(this);
//        this.registerMethodContext(Instance.getInstance().getMethodContext().getClient());
//        getClient().createNewContext();
//        log();
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPaint(Graphics graphics) {
        super.onPaint(graphics);
    }
}
