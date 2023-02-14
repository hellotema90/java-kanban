package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;


public interface TaskManager {

    // 2.4 методы создания
    void createTask(Task task);

    void createSubtask(Subtask subtask);

    void createEpic(Epic epic);

    //2.5 методы обновления задач
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateStatusEpic(int id);

    //2.1 Получение списка всех задач
    ArrayList<Task> getAllTask();

    ArrayList<Subtask> getAllSubtask();

    ArrayList<Epic> getAllEpic();

    //2.2 Удаление всех задач
    void clearAllTask();

    void clearAllSubtask();

    void clearAllEpic();

    //2.3 получение по идентификатору
    Task receiveByIdTask(int id);

    Subtask receiveByIdSubtask(int id);

    Epic receiveByIdEpic(int id);

    //2.6 Удаление по идентификатору
    void clearByIdTask(int id);

    void clearByIdSubtask(int id);

    void clearByIdEpic(int id);

    // 3.1 получение списка всех подзадач определённого эпика
    List<Subtask> getListAllEpicSubtasks(Epic epic);

    // последние просмотренные пользователем задачи
    List<Task>  getHistory();
}