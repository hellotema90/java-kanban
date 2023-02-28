package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tasks.Status;
import tasks.Task;
import tasks.Subtask;
import tasks.Epic;

public class InMemoryTaskManager  implements TaskManager {

    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int generatorId = 0;

    //счетчик для выдачи уникального id
    private int countGeneratorId() {
        return ++generatorId;
    }

    // 2.4 методы создания
    @Override
    public void createTask(Task task) { //task
        taskMap.put(task.setId(countGeneratorId()), task);
    }

    @Override
    public void createSubtask(Subtask subtask) { //subTask
        subtaskMap.put(subtask.setId(countGeneratorId()), subtask);
        Epic epic = epicMap.get(subtask.getEpicId());
        epic.getSubtaskId().add(subtask.getId());
        updateStatusEpic(epic.getId());
    }

    @Override
    public void createEpic(Epic epic) { //epic
        epicMap.put(epic.setId(countGeneratorId()), epic);
    }

    //2.5 методы обновления задач
    @Override
    public void updateTask(Task task) {
        taskMap.put(task.setId(countGeneratorId()), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskMap.put(subtask.getId(), subtask);
        Epic epic = epicMap.get(subtask.getEpicId());
        updateStatusEpic(epic.getId());
    }

    @Override
    public void updateStatusEpic(int id) {
        Epic epic = epicMap.get(id);
        int countDone = 0;
        int countNew = 0;

        for (int subtask : epic.getSubtaskId()) {
            if (subtaskMap.get(subtask).getStatus() == Status.DONE) {
                countDone++;
            } else if (subtaskMap.get(subtask).getStatus() == Status.NEW)
                countNew++;
        }
        if (epic.getSubtaskId().isEmpty() || countNew == epic.getSubtaskId().size()) {
            epic.setStatus(Status.NEW);
        } else if (countDone == epic.getSubtaskId().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    //2.1 Получение списка всех задач
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicMap.values());
    }

    //2.2 Удаление всех задач
    @Override
    public void clearAllTask() {
        taskMap.clear();
    }

    @Override
    public void clearAllSubtask() {
        subtaskMap.clear();
        for (Epic epic : getAllEpic()) {
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void clearAllEpic() {
        epicMap.clear();
    }

    //2.3 получение по идентификатору
    @Override
    public Task receiveByIdTask(int id) {
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Subtask receiveByIdSubtask(int id) {
        historyManager.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public Epic receiveByIdEpic(int id) {
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    //2.6 Удаление по идентификатору
    @Override
    public void clearByIdTask(int id) {
        historyManager.remove(id);
        taskMap.remove(id);
    }

    @Override
    public void clearByIdSubtask(int id) {
        Epic epics = epicMap.get(subtaskMap.get(id).getEpicId());
        epics.getSubtaskId().remove(id);
        historyManager.remove(id);
        subtaskMap.remove(id);
        updateStatusEpic(id);
    }

    @Override
    public void clearByIdEpic(int id) {
        for (Integer subtasks : epicMap.get(id).getSubtaskId()) {
            subtaskMap.remove(subtasks);
        }
        historyManager.remove(id);
        epicMap.remove(id);
    }

    // 3.1 получение списка всех подзадач определённого эпика
    @Override
    public List<Subtask> getListAllEpicSubtasks(Epic epic) {
        List<Integer> subtaskId = new ArrayList<>();
        for (int subtask : epic.getSubtaskId()) {
            subtaskMap.get(subtask);
            subtaskId.add(subtask);
        }
        List<Subtask> subtasks = new ArrayList<>();
        for (Integer integer : subtaskId) {
            receiveByIdSubtask(integer);
            subtasks.add(receiveByIdSubtask(integer));
        }
        return subtasks;
    }

    // последние просмотренные пользователем задачи
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
