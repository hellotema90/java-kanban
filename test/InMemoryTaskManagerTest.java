import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @Override
    @BeforeEach
    void setManager() {
        manager = new InMemoryTaskManager();
    }
}