import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    Path path = Path.of("taskdata.csv");
    File file = new File(String.valueOf(path));

    @Override
    @BeforeEach
    void setManager() {
        manager = new FileBackedTasksManager(file);
    }

    @Test
    void loadManagerEmptyFile() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("1.csv"));
        assertEquals(0, fileBackedTasksManager.getAllTask().size());
        assertEquals(0, fileBackedTasksManager.getAllEpic().size());
        assertEquals(0, fileBackedTasksManager.getAllSubtask().size());
    }

    @Test
    void saveAndLoad() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("2.csv"));
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Instant.now(), 0);
        fileBackedTasksManager.createTask(task1);
        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW, Instant.now(), 0);
        fileBackedTasksManager.createEpic(epic1);
        assertEquals(List.of(task1), fileBackedTasksManager.getAllTask());
        assertEquals(List.of(epic1), fileBackedTasksManager.getAllEpic());
    }



    @Test
    void loadHistoryEmptyHistory() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("3.csv"));
        assertEquals(0, fileBackedTasksManager.getHistory().size());
    }
}