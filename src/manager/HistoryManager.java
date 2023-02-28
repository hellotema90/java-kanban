package manager;

import tasks.Task;
import java.util.List;

public interface HistoryManager {
    //добавление задачи
    void add(Task task);

    //удаление задачи
    void remove(int id);

    //возврат истории
    List<Task> getHistory();
}
