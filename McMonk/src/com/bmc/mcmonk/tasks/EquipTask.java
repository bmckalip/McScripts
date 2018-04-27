package com.bmc.mcmonk.tasks;

import com.bmc.mclib.script.McScript;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.methods.tabs.Tab;

import java.util.Random;

import static com.bmc.mclib.constants.McItems.ROBE_BOTTOM;
import static com.bmc.mclib.constants.McItems.ROBE_TOP;

public class EquipTask extends Task {
    private int itemToEquip;

    public EquipTask(McScript s, int item){
        super(s);

        if(item == ROBE_BOTTOM || item == ROBE_TOP){
            itemToEquip = item;
        }else {
            s.log("Invalid Item");
        }

    }

    @Override
    public boolean validate() { return !s.getEquipment().contains(itemToEquip) && s.getInventory().contains(itemToEquip);}

    @Override
    public int execute(){
        Task.previousTask = toString();
        if(!s.getTabs().isOpen(Tab.INVENTORY)) s.getTabs().open(Tab.INVENTORY);
        if(s.getInventory().contains(itemToEquip)) s.getInventory().get(itemToEquip).interact("Wear");
        Random r = new Random();
        return r.nextInt(200) + 100;
    }

    @Override
    public String toString(){
        return "Equipping Robes";
    }
}
