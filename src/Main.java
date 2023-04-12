import manager.Managers;
import manager.TaskManager;
import tasks.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getInMemoryTaskManager();
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);


        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW);
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "описание Подзадачи 3", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", Status.NEW);
        taskManager.createEpic(epic2);



        taskManager.receiveByIdEpic(3);
        taskManager.receiveByIdEpic(7);
        taskManager.receiveByIdEpic(3);
        taskManager.receiveByIdEpic(7);
        taskManager.receiveByIdEpic(3);
        taskManager.receiveByIdEpic(7);
        taskManager.receiveByIdEpic(3);
        taskManager.receiveByIdEpic(7);
        taskManager.receiveByIdEpic(3);



        System.out.println(taskManager.getHistory());
    }
}