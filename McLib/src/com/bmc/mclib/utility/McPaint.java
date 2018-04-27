package com.bmc.mclib.utility;

import com.bmc.mclib.script.McScript;

public class McPaint {
    public static boolean shouldPaint(McScript s, boolean hasGUI, boolean guiCompleted){
        if(hasGUI && !guiCompleted) return false;
        return s.getClient().isLoggedIn() && !s.getRandomManager().isSolving();
    }
}
