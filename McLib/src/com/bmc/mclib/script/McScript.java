package com.bmc.mclib.script;

import com.bmc.mclib.taskmanager.TaskList;
import com.bmc.mclib.taskmanager.TaskManager;
import com.bmc.mclib.tasks.Task;
import org.dreambot.api.script.AbstractScript;

public abstract class McScript extends AbstractScript implements TaskManager{
    private TaskList<Task> taskList;
    public abstract void initializeTasks();
    public abstract void createGUI();

    public boolean guiCompleted = false;
    public boolean hasGUI = false;
    public boolean isIdle;

    public void setTasks(TaskList tasks){
        this.taskList = tasks;
    }

    @Override
    public TaskList getTasks(){ return this.taskList;}

    @Override
    public void addTask(Task task){ taskList.addTask(task);}

    @Override
    public void removeTask(Task task){ taskList.removeTask(task);}

    @Override
    public void onStart() {
        super.onStart();
        log("Starting Script");
        this.createGUI();
        this.initializeTasks();
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