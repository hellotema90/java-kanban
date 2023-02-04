import manager.Manager;
import tasks.Status;
import tasks.Task;
import tasks.Subtask;
import tasks.Epic;
public class Main {
    public static void main(String[] args){

        Manager manager = new Manager();
        //2.4 создание задач
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        manager.createTask(task1);
        manager.createTask(task2);


        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW);
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", Status.NEW, 1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", Status.NEW);
        manager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "описание Подзадачи 3", Status.NEW, 2);
        manager.createSubtask(subtask3);

        manager.updateTask(task1);
        manager.updateSubtask(subtask1);

        //2.1 получение списка всех задач
        manager.getAllTask();
        manager.getAllSubtask();
        manager.getAllEpic();
        //2.2 Удаление всех задач
        manager.clearAllTask();
        manager.clearAllSubtask();
        manager.clearAllEpic();
        //2.3 получение по идентификатору
        manager.receiveByIdTask(1);
        manager.receiveByIdSubtask(1);
        manager.receiveByIdEpic(1);
        //3.1 получение списка всех подзадач определённого эпика
        manager.getListAllEpicSubtasks(epic1);
        //2.6 удаление по идентификатору
        manager.clearByIdTask(1);
        manager.clearByIdSubtask(1);
        manager.clearByIdEpic(1);
        //2.1 получение списка всех задач
        System.out.println("получение списка всех задач");
        manager.getAllTask();
        manager.getAllSubtask();
        manager.getAllEpic();
        //2.2 удаление всех задач
        manager.clearAllTask();
        manager.clearAllSubtask();
        manager.clearAllEpic();
    }
}
