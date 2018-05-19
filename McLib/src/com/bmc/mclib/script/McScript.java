package com.bmc.mclib.script;

import com.bmc.mclib.gui.McGUI;
import com.bmc.mclib.taskmanager.TaskList;
import com.bmc.mclib.taskmanager.TaskManager;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.script.AbstractScript;

import javax.swing.*;

public abstract class McScript extends AbstractScript implements TaskManager, McGUI{
    private TaskList<Task> taskList;
    JFrame gui;
    public boolean guiCompleted = false;

    public boolean isIdle;
    public long startTime;

    public abstract TaskList getTasks();

    public void updateTasks(TaskList tasks){
        this.taskList = tasks;
    }

    @Override
    public void addTask(Task task){ taskList.addTask(task);}

    @Override
    public void removeTask(Task task){ taskList.removeTask(task);}

    @Override
    public void onStart() {
        super.onStart();
        startTime = System.currentTimeMillis();
        gui = this.createGUI();
        if (gui != null) this.launchGUI();
        updateTasks(this.getTasks());
    }

    @Override
    public int onLoop() {
        if(guiCompleted || gui == null) {
            for (Task task : this.taskList) {
                if (task.enabled && task.doValidate()) {
                    isIdle = false;
                    if (Task.state != task) {
                        log("Executing Task: " + task.toString());
                    }
                    return task.doExecute();
                }
                isIdle = true;
            }
        }
        return 0;
    }

    @Override
    public void onExit(){
        if(gui != null) gui.dispose();
    }

    //GUI
    @Override
    public final void setGUI(){
        gui = createGUI();
    }

    @Override
    public final JFrame getGUI(){
        return gui;
    }

    @Override
    public final void launchGUI(){
        gui.setVisible(true);
    }
}