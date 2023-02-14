package manager;

import tasks.Task;
import java.util.List;

public interface HistoryManager {
    //добавление задачи
    void add(Task task);

    //возврат истории
    List<Task> getHistory();
}
