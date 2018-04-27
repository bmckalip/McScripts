//import org.dreambot.api.Client;
//import org.dreambot.api.methods.Calculations;
//import org.dreambot.api.methods.container.impl.Inventory;
//import org.dreambot.api.methods.map.Area;
//
//import org.dreambot.api.script.AbstractScript;
//import org.dreambot.api.script.Category;
//import org.dreambot.api.script.ScriptManifest;
//import org.dreambot.api.script.listener.MessageListener;
//import org.dreambot.api.wrappers.interactive.Entity;
//import org.dreambot.api.wrappers.interactive.GameObject;
//import org.dreambot.api.wrappers.interactive.Player;
//import org.dreambot.api.wrappers.items.Item;
//import org.dreambot.api.wrappers.widgets.message.Message;
//
//
//import java.util.ArrayList;
//
//public class GameState extends McMonk{
//    public static ArrayList<String> stateTypes = new ArrayList<String>();
//    public static ArrayList<Item> items = new ArrayList<Item>();
//    public static ArrayList<Entity> objects = new ArrayList<>();
//    public static Inventory inventory;
//    public static Player player;
//    public static String type;
//
//
//    public GameState(String type){
//        this.type = type;
//        this.player = player;
//        this.inventory = getInventory();
//
//        addType(type);
//        refreshObjects();
//    }
//
//    private void refreshObjects(){
//
//    }
//
//    private void addType(String type){
//        if(!stateTypes.contains(type)){
//            stateTypes.add(type);
//        }
//    }
//
//    private void removeType(String type){
//        if(stateTypes.contains(type)){
//            stateTypes.remove(type);
//        }
//    }
//}
