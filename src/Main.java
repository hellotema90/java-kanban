import manager.Managers;
import manager.TaskManager;
import tasks.Status;
import tasks.Task;
import tasks.Subtask;
import tasks.Epic;

import java.util.List;

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
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", Status.NEW);
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "описание Подзадачи 3", Status.NEW, epic2.getId());
        taskManager.createSubtask(subtask3);


        taskManager.receiveByIdTask(1);
        taskManager.receiveByIdEpic(3);
        taskManager.receiveByIdTask(2);
        taskManager.receiveByIdTask(6);
        taskManager.receiveByIdSubtask(1);
        taskManager.receiveByIdSubtask(3);
        taskManager.receiveByIdTask(1);
        taskManager.receiveByIdTask(1);
        taskManager.receiveByIdTask(6);
        taskManager.receiveByIdTask(9);

        List<Task> history = taskManager.getHistory();
        System.out.println(history);
    }
}


/*
        TaskManager taskManager = new TaskManager();
        //2.4 создание задач
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);


        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW);
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", Status.NEW, 1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", Status.NEW);
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "описание Подзадачи 3", Status.NEW, 2);
        taskManager.createSubtask(subtask3);

        taskManager.updateTask(task1);
        taskManager.updateSubtask(subtask1);

        //2.1 получение списка всех задач
        taskManager.getAllTask();
        taskManager.getAllSubtask();
        taskManager.getAllEpic();
        //2.2 Удаление всех задач
        taskManager.clearAllTask();
        taskManager.clearAllSubtask();
        taskManager.clearAllEpic();
        //2.3 получение по идентификатору
        taskManager.receiveByIdTask(1);
        taskManager.receiveByIdSubtask(1);
        taskManager.receiveByIdEpic(1);
        //3.1 получение списка всех подзадач определённого эпика
        taskManager.getListAllEpicSubtasks(epic1);
        //2.6 удаление по идентификатору
        taskManager.clearByIdTask(1);
        taskManager.clearByIdSubtask(1);
        taskManager.clearByIdEpic(1);
        //2.1 получение списка всех задач
        System.out.println("получение списка всех задач");
        taskManager.getAllTask();
        taskManager.getAllSubtask();
        taskManager.getAllEpic();
        //2.2 удаление всех задач
        taskManager.clearAllTask();
        taskManager.clearAllSubtask();
        taskManager.clearAllEpic();
    }
}
*/