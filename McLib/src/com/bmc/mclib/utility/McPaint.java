package com.bmc.mclib.utility;

import com.bmc.mclib.script.McScript;

import javax.swing.*;

public class McPaint {
    public static boolean shouldPaint(McScript s, JFrame gui, boolean guiCompleted){
        if(gui != null && !guiCompleted) return false;
        return s.getClient().isLoggedIn() && !s.getRandomManager().isSolving();
    }
}
