import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.KVServer;
import http.adapters.InstantAdapter;
import manager.HTTPTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void shouldUpdateAndDeleteTasks() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Instant.now(), 0);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Instant.now(), 0);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.receiveByIdTask(task1.getId());
        manager.receiveByIdTask(task2.getId());
        assertEquals(2, manager.getHistory().size());
        task1.setId(3);
        manager.updateTask(task1);
        assertEquals(task1.getId(), 3);
        manager.clearByIdTask(2);
        assertEquals(1, manager.getHistory().size());
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
    public void shouldUpdateAndDeleteEpics() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW, Instant.now(), 0);
        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", Status.NEW, Instant.now(), 0);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.receiveByIdEpic(epic1.getId());
        manager.receiveByIdEpic(epic2.getId());
        assertEquals(2, manager.getHistory().size());
        epic1.setId(3);
        manager.updateStatusEpic(1);
        assertEquals(epic1.getId(), 3);
        manager.clearByIdEpic(2);
        assertEquals(1, manager.getHistory().size());
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

    @Test
    public void shouldUpdateAndDeleteSubtasks() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW, Instant.now(), 0);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", Status.NEW, epic1.getId(), Instant.now(), 0);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", Status.NEW, epic1.getId(), Instant.now(), 0);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.receiveByIdSubtask(subtask1.getId());
        manager.receiveByIdSubtask(subtask2.getId());
        assertEquals(2, manager.getHistory().size());
        subtask1.setId(3);
        manager.updateSubtask(subtask1);
        assertEquals(subtask1.getId(), 3);
        manager.clearByIdSubtask(2);
        assertEquals(1, manager.getHistory().size());
    }
}