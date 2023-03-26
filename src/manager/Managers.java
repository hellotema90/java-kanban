package manager;

import java.io.File;

public class Managers {
    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager(); // new FileBackedTasksManager
    }

    public static FileBackedTasksManager getDefault(){
        return new FileBackedTasksManager(new File(String.valueOf("taskdata.csv")));
    }
}
