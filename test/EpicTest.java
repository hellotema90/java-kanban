 import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.Status.*;


class EpicTest {
    private InMemoryTaskManager manager;

    @BeforeEach
    void createManager() {
        manager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", NEW);
        manager.createEpic(epic);
    }

    @Test
    void checkingEpicStatusByEmptySubtasksList() {
        assertEquals(manager.receiveByIdEpic(1).getStatus(), NEW);
    }

    @Test
    void checkingEpicStatusByNewSubtasksList() {
        manager.createSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", NEW, 1));
        manager.createSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", NEW, 1));
        assertEquals(manager.receiveByIdEpic(1).getStatus(), NEW);
    }

    @Test
    void checkingEpicStatusByInProgressSubtasksList() {
        manager.createSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", IN_PROGRESS, 1));
        manager.createSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", IN_PROGRESS, 1));
        assertEquals(manager.receiveByIdEpic(1).getStatus(), IN_PROGRESS);
    }

    @Test
    void checkingEpicStatusByDoneSubtasksList() {
        manager.createSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", DONE, 1));
        manager.createSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", DONE, 1));
        assertEquals(manager.receiveByIdEpic(1).getStatus(), DONE);
    }

    @Test
    void checkingEpicStatusByNewDoneSubtasksList() {
        manager.createSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", NEW, 1));
        manager.createSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", DONE, 1));
        assertEquals(manager.receiveByIdEpic(1).getStatus(), IN_PROGRESS);
    }



}