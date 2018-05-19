package com.bmc.mclib.taskmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TaskList<Task> implements Iterable<Task>{

    private List<Task> tasks;

    public TaskList(){
        this.tasks = new ArrayList<Task>();
    }

    public TaskList(TaskList<Task> tl){
        this.tasks = tl.tasks;
    }

    public TaskList(List<Task> tasks){
        this.tasks = tasks;
    }

    public TaskList(Task[] tasks){
        this.tasks = Arrays.asList(tasks);
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void addTask(int index, Task task){ tasks.add(index, task);}

    public void removeTask(Task task){
        tasks.remove(task);
    }

    public int indexOf(Task task){ return tasks.indexOf(task);}

    @Override
    public Iterator<Task> iterator() {
        return this.tasks.iterator();
    }

    @Override
    public String toString() {
        String output = "";
        for(Task task : tasks) output.concat(task.toString() + ", ");
        return output.endsWith(", ") ? output.substring(0, output.length() - 2) : output;
    }
}
