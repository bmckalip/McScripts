package com.bmc.mclib.taskmanager;

import com.bmc.mclib.tasks.Task;

public interface TaskManager {
    void addTask(Task task);
    void removeTask(Task task);
    TaskList getTasks();
}
