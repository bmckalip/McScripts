package com.bmc.mclib.utility;

import javax.swing.*;
import java.awt.*;

import static org.dreambot.api.Client.getClient;

public class McGUI{
    public static JCheckBox createCheckbox(String text, boolean selected){
        JCheckBox checkbox = new JCheckBox();
        checkbox.setText(text);
        checkbox.setSelected(selected);
        return checkbox;
    }

    public static JButton createButton(String text){
        JButton button = new JButton();
        button.setText(text);
        return button;
    }

    public static JFrame createDefaultGUI(String title, int sizeX, int sizeY){
        JFrame gui = new JFrame();
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setLocationRelativeTo(getClient().getInstance().getCanvas());
        gui.setPreferredSize(new Dimension(sizeX, sizeY));
        gui.getContentPane().setLayout(new BorderLayout());
        return gui;
    }
}
