import static org.junit.jupiter.api.Assertions.*;

import http.KVServer;
import http.KVTaskClient;
import manager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;



import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

class HTTPTaskManagerTest<T extends TaskManagerTest<HTTPTaskManager>> {
    private KVServer server;
    private TaskManager manager;

    @BeforeEach
    public void createManager() {
        try {
            server = new KVServer();
            server.start();
            manager = Managers.getDefault();
        } catch (IOException e) {
            System.out.println("Ошибка при создании менеджера");
        }
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void shouldLoadTasks() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Instant.now(), 0);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Instant.now(), 0);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.receiveByIdTask(task1.getId());
        manager.receiveByIdTask(task2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllTask(), list);
    }

    @Test
    public void shouldLoadEpics() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW, Instant.now(), 0);
        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", Status.NEW, Instant.now(), 0);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.receiveByIdEpic(epic1.getId());
        manager.receiveByIdEpic(epic2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllEpic(), list);
    }

    @Test
    public void shouldLoadSubtasks() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW, Instant.now(), 0);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", Status.NEW, epic1.getId()
                , Instant.now(), 0);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", Status.NEW, epic1.getId(),
                Instant.now(), 0);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.receiveByIdSubtask(subtask1.getId());
        manager.receiveByIdSubtask(subtask2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getAllSubtask(), list);
    }

}