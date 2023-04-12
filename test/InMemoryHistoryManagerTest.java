
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager manager;

    @BeforeEach
    void setManagerAndCreateTasks() {
        manager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Instant.now(), 0);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Instant.now(), 0);
        task1.setId(0);
        task1.setId(1);
        manager.add(task1);
        manager.add(task2);


        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW);
        epic1.setId(2);
        manager.add(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", Status.NEW, epic1.getId(), Instant.now(), 0);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", Status.NEW, epic1.getId(), Instant.now(), 0);
        Subtask subtask3 = new Subtask("Подзадача 3", "описание Подзадачи 3", Status.NEW, epic1.getId(), Instant.now(), 0);
        subtask1.setId(3);
        subtask2.setId(4);
        subtask3.setId(5);
        manager.add(subtask1);
        manager.add(subtask2);
        manager.add(subtask3);
    }

    //добавление в историю
    @Test
    public void taskToHistory() {
        assertEquals(6, manager.getHistory().size());
    }

    //пустая история
    @Test
    public void emptyList() {
        manager.remove(0);
        manager.remove(1);
        manager.remove(2);
        manager.remove(3);
        manager.remove(4);
        manager.remove(5);

        assertEquals(0, manager.getHistory().size());
    }

    //удаление задачи из истории, начало
    @Test
    public void removeTask() {
        manager.remove(0);
        assertEquals(5, manager.getHistory().size());
    }

    //удаление задачи из истории, середина
    @Test
    public void notRemoveTask() {
        manager.remove(3);
        assertEquals(5, manager.getHistory().size());
    }

    //удаление задачи из истории, конец
    @Test
    public void isEmpty() {
        manager.remove(5);
        assertEquals(5, manager.getHistory().size());
    }

    //Дублирование
    @Test
    void duplication() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Instant.now(), 0);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Instant.now(), 0);
        task1.setId(0);
        task1.setId(1);
        manager.add(task1);
        manager.add(task2);


        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW);
        epic1.setId(2);
        manager.add(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", Status.NEW, epic1.getId(), Instant.now(), 0);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", Status.NEW, epic1.getId(), Instant.now(), 0);
        Subtask subtask3 = new Subtask("Подзадача 3", "описание Подзадачи 3", Status.NEW, epic1.getId(), Instant.now(), 0);
        subtask1.setId(3);
        subtask2.setId(4);
        subtask3.setId(5);
        manager.add(subtask1);
        manager.add(subtask2);
        manager.add(subtask3);

        assertEquals(6,manager.getHistory().size());
    }
}