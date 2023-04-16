import static org.junit.jupiter.api.Assertions.*;
import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;



import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {
    @Override
    @BeforeEach
    void setManager() {
        manager = new HTTPTaskManager();
    }


}