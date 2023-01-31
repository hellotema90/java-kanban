package manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import tasks.Status;
import tasks.Task;
import tasks.Subtask;
import tasks.Epic;

public class Manager {

    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();
    private int generatorId = 0;

    //счетчик для выдачи уникального id
    private int countGeneratorId() {
        return ++generatorId;
    }

    // 2.4 методы создания
    public void createTask(Task task) { //task
        taskMap.put(task.setId(countGeneratorId()), task);
    }

    public void createSubtask(Subtask subtask) { //subTask
        subtaskMap.put(subtask.setId(countGeneratorId()), subtask);
        Epic epic = epicMap.get(subtask.getEpicId());
        epic.getSubtaskId().add(subtask.getId());
        updateStatusEpic(epic.getId());
    }

    public void createEpic(Epic epic) { //epic
        epicMap.put(epic.setId(countGeneratorId()), epic);
    }

    //2.5 методы обновления задач
    public void updateTask(Task task) {
        taskMap.put(task.setId(countGeneratorId()), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskMap.put(subtask.getId(), subtask);
        Epic epic = epicMap.get(subtask.getEpicId());
        updateStatusEpic(epic.getId());
    }

    private void updateStatusEpic(int id) {
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

    //2.1 Получение списка всех задач
    public Collection<Task> receiveAllTask() {
        return taskMap.values();
    }

    public Collection<Subtask> receiveAllSubtask() {
        return subtaskMap.values();
    }

    public Collection<Epic> receiveAllEpic() {
        return epicMap.values();
    }

    //2.2 Удаление всех задач
    public void clearAllTask() {
        taskMap.clear();
    }

    public void clearAllSubtask() {
        subtaskMap.clear();
        for (Epic epic : receiveAllEpic()) {
            epic.setStatus(Status.NEW);
        }
    }

    public void clearAllEpic() {
        epicMap.clear();
    }

    //2.3 получение по идентификатору
    public Task receiveByIdTask(int id) {
        return taskMap.get(id);
    }

    public Subtask receiveByIdSubtask(int id) {
        return subtaskMap.get(id);
    }

    public Epic receiveByIdEpic(int id) {
        return epicMap.get(id);
    }

    //2.6 Удаление по идентификатору
    public void clearByIdTask(int id) {
        taskMap.remove(id);
    }

    public void clearByIdSubtask(int id) {
        Epic epics = epicMap.get(subtaskMap.get(id).getEpicId());
        epics.getSubtaskId().remove(id);
        subtaskMap.remove(id);
        updateStatusEpic(id);
    }

    public void clearByIdEpic(int id) {
        for (Integer subtasks : epicMap.get(id).getSubtaskId()) {
            subtaskMap.remove(subtasks);
        }
        epicMap.remove(id);
    }

    // 3.1 получение списка всех подзадач определённого эпика
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
}