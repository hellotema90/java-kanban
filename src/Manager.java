import java.util.ArrayList;
import java.util.HashMap;
public class Manager {
    private int idMap; //номер (id) задачи
    private ArrayList<Integer> listIdEpic = new ArrayList<>();// список id Epic`ов

    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();

    // 2.4 методы создания
    public void createTask(Object obj) { //task
        Task task = new Task();
        if (obj.getClass() == task.getClas()) {
            idMap++;
            task = (Task) obj;
            taskMap.put(idMap, task);
        }
    }

    public void createSubTask(Object obj) { //subTask
        SubTask subTask = new SubTask();
        if (obj.getClass() == subTask.getClas()) {
            idMap++;
            subTask = (SubTask) obj;
            if (listIdEpic.contains(subTask.idEpic)) {
                subTaskMap.put(idMap, subTask);
            } else {
                System.out.println("Epic с id " + subTask.idEpic + " не существует! " +
                        "Необходимо указать корректный id Epic`а, " + subTask.name + " не создан");
            }
        }
    }

    public void createEpic(Object obj) { //epic
        Epic epic = new Epic();
        if (obj.getClass() == epic.getClas()) {
            idMap++;
            epic = (Epic) obj;
            listIdEpic.add(idMap);
            epicMap.put(idMap, epic);
        }
    }

    //2.5 методы обновления задач
    public void updateTask(Object obj, int id, String status) {
        if (taskMap.containsKey(id)) {
            Task task = (Task) obj;
            task.status = status;
            taskMap.put(id, task);
        } else {
            System.out.println("Задача с ID - " + id + "не найдена");
        }
    }

    public void updateSubTask(Object obj, int id, String status) {
        if (taskMap.containsKey(id)) {
            SubTask subTask = (SubTask) obj;
            subTask.status = status;
            subTaskMap.put(id, subTask);
            if (status.equals("inProgress")) {
                epicMap.get(subTask.idEpic).status = "inProgress";
            } else if (status.equals("Done")) {
                boolean isCheck = true;
                for (Integer taskId : subTaskMap.keySet()) {
                    SubTask subTask1 = subTaskMap.get(taskId);
                    if (subTask1.idEpic == id) {
                        if (subTask.status != "Done") {
                            isCheck = false;
                            return;
                        }
                    }
                }
                if (isCheck) {
                    epicMap.get(subTask.idEpic).status = "Done";
                }
            }
        } else {
            System.out.println("Задача с ID - " + id + "не найдена");
        }
    }

    public void updateEpic(Object obj, int id, String status) {
        if (epicMap.containsKey(id)) {
            Epic epic = (Epic) obj;
            taskMap.put(id, epic);
        } else {
            System.out.println("Задача с ID - " + id + "не найдена");
        }
    }

    //2.1 Получение списка всех задач
    public void printAllTask() {
        for (Integer id : taskMap.keySet()) {
            System.out.println("ID - " + id + " " + taskMap.get(id));
        }
    }

    public void printAllSubTask() {
        for (Integer id : subTaskMap.keySet()) {
            System.out.println("ID - " + id + " " + subTaskMap.get(id));
        }
    }

    public void printAllEpic() {
        for (Integer id : epicMap.keySet()) {
            System.out.println("ID - " + id + " " + epicMap.get(id));
        }
    }

    //2.2 Удаление всех задач
    public void clearAllTask() {
        taskMap.clear();
        System.out.println("Все задачи удалены" + taskMap);
    }

    public void clearAllSubTask() {
        subTaskMap.clear();
        System.out.println("Все задачи удалены" + subTaskMap);
    }

    public void clearAllEpic() {
        epicMap.clear();
        listIdEpic.clear();
        System.out.println("Все задачи удалены" + epicMap);
    }

    //2.3 получение по идентификатору
    public void printByIdTask(int id) {
        if (taskMap.containsKey(id)) {
            System.out.println("Задача с ID  " + id + " это - " + taskMap.get(id));
        } else {
            System.out.println("Задача с ID " + id + "не найдена");
        }
    }

    public void printByIdSubTask(int id) {
        if (subTaskMap.containsKey(id)) {
            System.out.println("Задача с ID " + id + " это - " + subTaskMap.get(id));
        } else {
            System.out.println("Задача с ID " + id + "не найдена");
        }
    }

    public void printByIdEpic(int id) {
        if (epicMap.containsKey(id)) {
            System.out.println("Задача с ID " + id + " это - " + epicMap.get(id));
        } else {
            System.out.println("Задача с ID " + id + "не найдена");
        }
    }

    //2.6 Удаление по идентификатору
    public void clearByIdTask(int id) {
        if (taskMap.containsKey(id)) {
            System.out.println("Задача с ID - " + id + " удалена");
            taskMap.remove(id);
        } else {
            System.out.println("Задача с ID - " + id + "не найдена");
        }
    }

    public void clearByIdSubTask(int id) {
        if (subTaskMap.containsKey(id)) {
            System.out.println("Задача с ID - " + id + " удалена");
            subTaskMap.remove(id);
        } else {
            System.out.println("Задача с ID - " + id + "не найдена");
        }
    }

    public void clearByIdEpic(int id) {
        if (epicMap.containsKey(id)) {
            System.out.println("Задача с ID - " + id + " удалена");
            epicMap.remove(id);
            System.out.println(listIdEpic);
            listIdEpic.remove((Integer) id);
            System.out.println(listIdEpic);
            ArrayList<Integer> listIdSubTask = new ArrayList<>();
            for (Integer taskId : subTaskMap.keySet()) {
                if (subTaskMap.get(taskId).idEpic == id) {
                    System.out.println("Задача с ID - " + id + " Epic-задача имеет подзадачу с ID - " + taskId + " которая тоже подлежит удалению");
                    listIdSubTask.add(taskId);
                }
            }
            for (int i = 0; i < listIdSubTask.size(); i++) {
                subTaskMap.remove(listIdSubTask.get(i));
            }
            subTaskMap.remove(id);
        } else {
            System.out.println("Задача с ID - " + id + "не найдена");
        }
    }

    // 3.1 получение списка всех подзадач определённого эпика
    public void printingEpicSubtask(int id) {
        if (listIdEpic.contains(id)) {
            System.out.println("Epic с ID - " + id);
            for (Integer taskId : subTaskMap.keySet()) {
                if (subTaskMap.get(taskId).idEpic == id) {
                    System.out.println(" имеет SubTask с ID - " + taskId);
                }
            }
        } else {
            System.out.println("Epic с ID - " + id + "не существует");
        }
    }
}