package com.esprit.constuctify.project_ms;

import java.util.List;

public interface ITaskService {
    List<Task> retrieveAllTasks();

    Task addTask(Task task);

    Task updateTask(Task task);

    Task retrieveTask(Long id);

    void removeTask(Long id);
}
