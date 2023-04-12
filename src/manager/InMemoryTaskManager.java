package manager;

import java.time.Instant;
import java.util.*;
import java.util.stream.*;

import tasks.Status;
import tasks.Task;
import tasks.Subtask;
import tasks.Epic;

public class InMemoryTaskManager  implements TaskManager {

    protected final HashMap<Integer, Task> taskMap = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    protected final HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private int generatorId = 0;
    private final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime);
    protected Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);

    //счетчик для выдачи уникального id
    private int countGeneratorId() {
        return ++generatorId;
    }

    // 2.4 методы создания
    @Override
    public void createTask(Task task) { //task
        //addNewPrioritizedTask(task);
        taskMap.put(task.setId(countGeneratorId()), task);
    }

    @Override
    public void createSubtask(Subtask subtask) { //subTask
        //addNewPrioritizedTask(subtask);
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
        prioritizedTasks.clear();
    }

    @Override
    public void clearAllSubtask() {
        Subtask subtask = subtaskMap.get(generatorId);
        prioritizedTasks.remove(subtask);//
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
        prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), id));
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
        prioritizedTasks.removeIf(task -> task.getId() == id);
        historyManager.remove(id);
        taskMap.remove(id);
    }

    @Override
    public void clearByIdSubtask(int id) {
        Epic epics = epicMap.get(subtaskMap.get(id).getEpicId());
        epics.getSubtaskId().remove((Integer)id);
        //
        prioritizedTasks.remove(subtaskMap.get(id));//
        historyManager.remove(id);
        subtaskMap.remove(id);
        updateStatusEpic(epics.getId());
    }

    @Override
    public void clearByIdEpic(int id) {
        for (Integer subtasks : epicMap.get(id).getSubtaskId()) {
            prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), id));//
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
/*
    public void updateTimeEpic(Epic epic) {
        List<Subtask> subtasks = getListAllEpicSubtasks(epic);
        //List<Subtask> subtasks = getListAllEpicSubtasks(epic.getId());
        Instant startTime = subtasks.get(0).getStartTime();
        Instant endTime = subtasks.get(0).getEndTime();

        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime().isBefore(startTime)) startTime = subtask.getStartTime();
            if (subtask.getEndTime().isAfter(endTime)) endTime = subtask.getEndTime();
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        long duration = (endTime.toEpochMilli() - startTime.toEpochMilli());
        epic.setDuration(duration);
    }
*/


    private void addNewPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
        validateTaskPriority();
    }

    public boolean checkTime(Task task) {
        List<Task> tasks = List.copyOf(prioritizedTasks);
        int sizeTimeNull = 0;
        if (tasks.size() > 0) {
            for (Task taskSave : tasks) {
                if (taskSave.getStartTime() != null && taskSave.getEndTime() != null) {
                    if (task.getStartTime().isBefore(taskSave.getStartTime())
                            && task.getEndTime().isBefore(taskSave.getStartTime())) {
                        return true;
                    } else if (task.getStartTime().isAfter(taskSave.getEndTime())
                            && task.getEndTime().isAfter(taskSave.getEndTime())) {
                        return true;
                    }
                } else {
                    sizeTimeNull++;
                }

            }
            return sizeTimeNull == tasks.size();
        } else {
            return true;
        }
    }

    private void validateTaskPriority() {
        List<Task> tasks = getPrioritizedTasks();

        for (int i = 1; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            boolean taskHasIntersections = checkTime(task);

            if (taskHasIntersections) {
                throw new ManagerValidateException(
                        "Задачи #" + task.getId() + " и #" + tasks.get(i - 1) + "пересекаются");
            }
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();

    }




}
