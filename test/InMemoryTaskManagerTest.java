import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @Override
    @BeforeEach
    void setManager() {
        manager = new InMemoryTaskManager();
    }
    @Test
    public void WhenInstantiatingTheInMemoryTaskManagerClassItReturnsEmptyListsThenTheTestIsConsideredSuccessful(){
        assertEquals(0, manager.getAllTask().size());
        assertEquals(0, manager.getAllEpic().size());
        assertEquals(0, manager.getAllSubtask().size());
    }
}
