package com.bmc.mclib.script;

import com.bmc.mclib.taskmanager.TaskList;
import com.bmc.mclib.taskmanager.TaskManager;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.script.AbstractScript;

public abstract class McScript extends AbstractScript implements TaskManager{
    private TaskList<Task> taskList;
    public abstract TaskList getTasks();
    public void createGUI() {};

    public boolean guiCompleted = false;
    public boolean hasGUI = false;
    public boolean isIdle;
    public long startTime;


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
        log("Script Started");
        this.createGUI();
        updateTasks(this.getTasks());
    }

    @Override
    public int onLoop() {
        if(guiCompleted || !hasGUI) {
            for (Task task : this.taskList) {
                if (task.enabled && task.validate()) {
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
}