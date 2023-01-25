public class Main {
    public static void main(String[] args){

        Manager manager = new Manager();

        //2.4 создание задач
        manager.createTask(new Task("Задача 1", "Описание задачи 1"));
        manager.createTask(new Task("Задача 2", "Описание задачи 2"));

        manager.createEpic(new Epic("Эпик 1", "описание Эпика 1"));
        manager.createSubTask(new SubTask("Подзадача 1", "описание Подзадачи 1", 3));
        manager.createSubTask(new SubTask("Подзадача 2", "описание Подзадачи 2", 3));

        manager.createEpic(new Epic("Эпик 2", "описание Эпика 2"));
        manager.createSubTask(new SubTask("Подзадача 3", "описание Подзадачи 3", 6));

        manager.updateTask(new Task("Обновленный Task", "описание обновленного Task"), 1,
                "in_Progress");
        manager.updateSubTask(new SubTask("Обновленный subTask", "описание обновленного subTask", 6),
                7, "Done");

        //2.1 получение списка всех задач
        manager.printAllTask();
        manager.printAllSubTask();
        manager.printAllEpic();
        //2.2 Удаление всех задач
        manager.clearAllTask();
        manager.clearAllSubTask();
        manager.clearAllEpic();
        //2.3 получение по идентификатору
        manager.printByIdTask(4);
        manager.printByIdSubTask(4);
        manager.printByIdEpic(4);
        //3.1 получение списка всех подзадач определённого эпика
        manager.printingEpicSubtask(3);
        //2.6 удаление по идентификатору
        manager.clearByIdTask(2);
        manager.clearByIdSubTask(3);
        manager.clearByIdEpic(3);
        //2.1 получение списка всех задач
        manager.printAllTask();
        manager.printAllSubTask();
        manager.printAllEpic();
        //2.2 удаление всех задач
        manager.clearAllTask();
        manager.clearAllSubTask();
        manager.clearAllEpic();
    }
}