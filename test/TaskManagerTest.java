import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.NEW;
import static tasks.Status.IN_PROGRESS;
import static tasks.Status.DONE;
import java.time.Instant;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    @BeforeEach
    abstract void setManager();

    //методы создания
    @Test
    void createTaskTest() {
        assertEquals(0, manager.getAllTask().size());
        Task task1 = new Task("Задача 1", "Описание задачи 1", NEW);
        manager.createTask(task1);
        assertEquals(task1, manager.receiveByIdTask(1));
        Task task2 = new Task("Задача 2", "Описание задачи 2", IN_PROGRESS);
        manager.createTask(task2);
        assertEquals(task2, manager.receiveByIdTask(2));
        Task task3 = new Task("Задача 3", "Описание задачи 3", DONE);
        manager.createTask(task3);
        assertEquals(task2, manager.receiveByIdTask(2));
    }

    @Test
    void createSubtaskTest() {
        assertEquals(0, manager.getAllSubtask().size());
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", DONE, 1);
        manager.createSubtask(subtask1);
        assertEquals(subtask1, manager.receiveByIdSubtask(2));
        assertEquals(DONE, manager.receiveByIdEpic(1).getStatus());
    }

    @Test
    void createEpicTest() {
        assertEquals(0, manager.getAllEpic().size());
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        assertEquals(epic1, manager.receiveByIdEpic(1));
        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", IN_PROGRESS);
        manager.createEpic(epic2);
        assertEquals(epic2, manager.receiveByIdEpic(2));
        Epic epic3 = new Epic("Эпик 3", "описание Эпика 3", DONE);
        manager.createEpic(epic3);
        assertEquals(epic2, manager.receiveByIdEpic(2));
    }

    //методы обновления задач
    @Test
    void updateTaskTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", NEW);
        manager.createTask(task1);
        task1.setName("Задача 2");
        task1.setDescription("Описание задачи 2");
        task1.setStatus(DONE);
        manager.updateTask(task1);
        assertEquals(task1, manager.receiveByIdTask(1));
    }

    @Test
    void updateSubtaskTest() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", Status.NEW, 1);
        manager.createSubtask(subtask1);
        assertEquals(subtask1, manager.receiveByIdSubtask(2));
        subtask1.setStatus(DONE);
        manager.updateSubtask(subtask1);
        assertEquals(subtask1, manager.receiveByIdSubtask(2));
    }

    @Test
    void updateStatusEpicTest() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        epic1.setName("Эпик 2");
        epic1.setDescription("описание Эпика 2");
        epic1.setStatus(DONE);
        manager.updateStatusEpic(1);
        assertEquals(epic1, manager.receiveByIdEpic(1));
    }

    //Получение списка всех задач
    @Test
    void getAllTaskTest() {
        assertEquals(0, manager.getAllTask().size());
        Task task1 = new Task("Задача 1", "Описание задачи 1", IN_PROGRESS);
        manager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", DONE);
        manager.createTask(task2);
        Task[] taskArrayList1 = {task1, task2};
        Task[] taskArrayList2 = manager.getAllTask().toArray(new Task[0]);
        assertArrayEquals(taskArrayList1, taskArrayList2);
    }

    @Test
    void getAllSubtaskTest() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", IN_PROGRESS, 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", DONE, 1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        Subtask[] subtaskArrayList1 = {subtask1, subtask2};
        Subtask[] subtaskArrayList2 = manager.getAllSubtask().toArray(new Subtask[0]);
        assertArrayEquals(subtaskArrayList1, subtaskArrayList2);
    }

    @Test
    void getAllEpicTest() {
        assertEquals(0, manager.getAllEpic().size());
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", DONE);
        manager.createEpic(epic2);
        Epic[] epicArrayList1 = {epic1, epic2};
        Epic[] epicArrayList2 = manager.getAllEpic().toArray(new Epic[0]);
        assertArrayEquals(epicArrayList1, epicArrayList2);
    }

    //Удаление всех задач
    @Test
    void clearAllTaskTest() {
        manager.clearAllTask();
        Task task1 = new Task("Задача 1", "Описание задачи 1", NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", IN_PROGRESS);
        Task task3 = new Task("Задача 3", "Описание задачи 3", DONE);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.receiveByIdTask(1);
        manager.receiveByIdTask(2);
        assertEquals(2, manager.getHistory().size());
        manager.clearAllTask();
        assertEquals(0, manager.getAllTask().size());
    }

    @Test
    void clearAllSubtaskTest() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", NEW);
        manager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", NEW, 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", IN_PROGRESS, 2);
        Subtask subtask3 = new Subtask("Подзадача 3", "описание Подзадачи 3", DONE, 2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        assertEquals(3, manager.getAllSubtask().size());
        manager.clearAllSubtask();
        assertEquals(0, manager.getAllSubtask().size());
    }

    @Test
    void clearAllEpicTest() {
        manager.clearAllEpic();
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", DONE);
        manager.createEpic(epic2);
        Epic epic3 = new Epic("Эпик 3", "описание Эпика 3", IN_PROGRESS);
        manager.createEpic(epic3);
        manager.receiveByIdEpic(1);
        manager.receiveByIdEpic(2);
        assertEquals(2, manager.getHistory().size());
        manager.clearAllEpic();
        assertEquals(0, manager.getAllEpic().size());
    }

    //получение по идентификатору
    @Test
    void receiveByIdTaskTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", IN_PROGRESS);
        Task task2 = new Task("Задача 2", "Описание задачи 2", DONE);
        manager.createTask(task1);
        manager.createTask(task2);
        assertEquals(task1, manager.receiveByIdTask(1));
        assertEquals(task2, manager.receiveByIdTask(2));
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    void receiveByIdSubtaskTest() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", IN_PROGRESS, 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", DONE, 1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        assertEquals(subtask1, manager.receiveByIdSubtask(2));
        assertEquals(subtask2, manager.receiveByIdSubtask(3));
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    void receiveByIdEpicTest() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", IN_PROGRESS);
        manager.createEpic(epic1);
        assertEquals(epic1, manager.receiveByIdEpic(1));
        assertEquals(1, manager.getHistory().size());
    }

    //Удаление по идентификатору
    @Test
    void clearByIdTaskTest() {
        manager.clearByIdTask(1);
        Task task1 = new Task("Задача 1", "Описание задачи 1", IN_PROGRESS);
        manager.createTask(task1);
        manager.clearByIdTask(1);
        assertEquals(0, manager.getAllTask().size());
        Task task2 = new Task("Задача 1", "Описание задачи 1", NEW);
        manager.createTask(task2);
        Task task3 = new Task("Задача 2", "Описание задачи 2", DONE);
        manager.createTask(task3);
        assertEquals(2, manager.getAllTask().size());
        manager.receiveByIdTask(2);
        manager.receiveByIdTask(3);
        assertEquals(2, manager.getHistory().size());
        manager.clearByIdTask(2);
        assertEquals(1, manager.getAllTask().size());
        manager.clearByIdTask(3);
        assertEquals(0, manager.getAllTask().size());
        assertEquals(0, manager.getHistory().size());
    }

    @Test
    void clearByIdSubtaskTest() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", IN_PROGRESS, 1);
        manager.createSubtask(subtask1);
        manager.clearByIdSubtask(2);
        assertEquals(0, manager.getAllSubtask().size());
        Subtask subtask2 = new Subtask("Подзадача 1", "описание Подзадачи 1", NEW, 1);
        manager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 2", "описание Подзадачи 2", DONE, 1);
        manager.createSubtask(subtask3);
        assertEquals(2, manager.getAllSubtask().size());
        manager.receiveByIdSubtask(3);
        manager.receiveByIdSubtask(4);
        assertEquals(2, manager.getHistory().size());
        manager.clearByIdSubtask(3);
        assertEquals(1, manager.getAllSubtask().size());
        manager.clearByIdSubtask(4);
        assertEquals(0, manager.getAllSubtask().size());
        assertEquals(0, manager.getHistory().size());
    }

    @Test
    void clearByIdEpicTest() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        manager.clearByIdEpic(1);
        assertEquals(0, manager.getAllEpic().size());
        Epic epic2 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", NEW, 2);
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", NEW, 2);
        manager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 3", "описание Подзадачи 3", NEW, 2);
        manager.createSubtask(subtask3);
        Epic epic3 = new Epic("Эпик 2", "описание Эпика 2", DONE);
        manager.createEpic(epic3);
        assertEquals(2, manager.getAllEpic().size());
        assertEquals(3, manager.getAllSubtask().size());
        manager.receiveByIdEpic(2);
        manager.receiveByIdEpic(6);
        assertEquals(2, manager.getHistory().size());
        manager.clearByIdEpic(2);
        assertEquals(1, manager.getHistory().size());
        assertEquals(0, manager.getAllSubtask().size());
        assertEquals(1, manager.getAllEpic().size());
        manager.clearByIdEpic(6);
        assertEquals(0, manager.getAllEpic().size());
        assertEquals(0, manager.getHistory().size());
    }

    // последние просмотренные пользователем задачи
    @Test
    void getHistoryTest() {
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", NEW);
        manager.createEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", NEW);
        manager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", IN_PROGRESS, 1);
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", DONE, 1);
        manager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 3", "описание Подзадачи 3", NEW, 2);
        manager.createSubtask(subtask3);
        Subtask subtask4 = new Subtask("Подзадача 4", "описание Подзадачи 4", DONE, 2);
        manager.createSubtask(subtask4);
        manager.receiveByIdSubtask(5);
        manager.receiveByIdSubtask(6);
        assertEquals(subtask3, manager.getHistory().get(0));
        assertEquals(subtask4, manager.getHistory().get(1));
        assertEquals(2, manager.getHistory().size());
    }
    @Test
    void shouldHaveNewStatusWhenEpicHasNoSubtasks() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", NEW);
        manager.createEpic(epic);
        assertEquals(manager.receiveByIdEpic(1).getStatus(), NEW);
    }

    @Test
    void shouldHaveNewStatusWhenEpicHasNewStatusSubtasks() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", NEW);
        manager.createEpic(epic);
        manager.createSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", NEW, 1));
        manager.createSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", NEW, 1));
        assertEquals(manager.receiveByIdEpic(1).getStatus(), NEW);
    }

    @Test
    void shouldHaveNewStatusWhenEpicHasInProgressStatusSubtasks() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", NEW);
        manager.createEpic(epic);
        manager.createSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", IN_PROGRESS, 1));
        manager.createSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", IN_PROGRESS, 1));
        assertEquals(manager.receiveByIdEpic(1).getStatus(), IN_PROGRESS);
    }

    @Test
    void shouldHaveNewStatusWhenEpicHasDoneStatusSubtasks() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", NEW);
        manager.createEpic(epic);
        manager.createSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", DONE, 1));
        manager.createSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", DONE, 1));
        assertEquals(manager.receiveByIdEpic(1).getStatus(), DONE);
    }

    @Test
    void shouldHaveNewStatusWhenEpicHasNewAndDoneStatusSubtasks() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", NEW);
        manager.createEpic(epic);
        manager.createSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", NEW, 1));
        manager.createSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", DONE, 1));
        assertEquals(manager.receiveByIdEpic(1).getStatus(), IN_PROGRESS);
    }
}
