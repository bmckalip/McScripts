package com.bmc.mclib.gui;

import javax.swing.*;
import java.awt.*;

import static org.dreambot.api.Client.getClient;

public interface McGUI{
    static JCheckBox createCheckbox(String text, boolean selected){
        JCheckBox checkbox = new JCheckBox();
        checkbox.setText(text);
        checkbox.setSelected(selected);
        return checkbox;
    }

    static JButton createButton(String text){
        JButton button = new JButton();
        button.setText(text);
        return button;
    }

    static JFrame createDefaultGUI(String title, int sizeX, int sizeY){
        JFrame gui = new JFrame();
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setLocationRelativeTo(getClient().getInstance().getCanvas());
        gui.setPreferredSize(new Dimension(sizeX, sizeY));
        gui.getContentPane().setLayout(new BorderLayout());
        return gui;
    }

    default JFrame createGUI(){ return null;}
    void setGUI();
    JFrame getGUI();
    void launchGUI();
}
