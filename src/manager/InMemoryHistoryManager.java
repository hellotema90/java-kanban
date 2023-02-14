package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_TASKS_LIMIT = 10;
    private final LinkedList<Task> historyTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyTasks.size() >= HISTORY_TASKS_LIMIT) {
                historyTasks.removeFirst();
                historyTasks.add(task);
            } else {
                historyTasks.add(task);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }
}

