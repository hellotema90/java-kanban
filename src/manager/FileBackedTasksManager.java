package manager;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    private static final String HEADER_CSV_FILE = "id,type,name,status,description,epic\n";


    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) {
        Path path = Path.of("taskdata.csv");
        File file = new File(String.valueOf(path));


        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.createTask(task2);


        Epic epic1 = new Epic("Эпик 1", "описание Эпика 1", Status.NEW);
        fileBackedTasksManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "описание Подзадачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "описание Подзадачи 2", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "описание Подзадачи 3", Status.NEW, epic1.getId());
        fileBackedTasksManager.createSubtask(subtask1);
        fileBackedTasksManager.createSubtask(subtask2);
        fileBackedTasksManager.createSubtask(subtask3);

        Epic epic2 = new Epic("Эпик 2", "описание Эпика 2", Status.NEW);
        fileBackedTasksManager.createEpic(epic2);

        fileBackedTasksManager.receiveByIdEpic(7);
        fileBackedTasksManager.receiveByIdEpic(3);
        fileBackedTasksManager.receiveByIdEpic(7);
        fileBackedTasksManager.receiveByIdEpic(3);
        fileBackedTasksManager.receiveByIdEpic(7);
        fileBackedTasksManager.receiveByIdEpic(3);
        fileBackedTasksManager.receiveByIdEpic(7);
        fileBackedTasksManager.receiveByIdEpic(3);

        FileBackedTasksManager fileBackedTasksManager2 = new FileBackedTasksManager(file);
        fileBackedTasksManager2.loadFromFile(file);
        System.out.println("Задачи");
        System.out.println(fileBackedTasksManager2.getAllTask());
        System.out.println("Эпики");
        System.out.println(fileBackedTasksManager2.getAllEpic());
        System.out.println("Подзадачи");
        System.out.println(fileBackedTasksManager2.getAllSubtask());
        System.out.println("История");
        System.out.println(fileBackedTasksManager2.getHistory());
        fileBackedTasksManager2.save();
    }


    public void loadFromFile(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.equals("")) {
                    break;
                }

                Task task = fromString(line);

                if (task instanceof Epic) {
                    createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    createSubtask((Subtask) task);
                } else {
                    createTask(task);
                }
            }

            for(Integer fromId : historyFromString(bufferedReader.readLine())){
                addToHistory(fromId);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
    }
    
    private void addToHistory(int id) {
        for (Task task : getAllTask()) {
            if (task.getId() == id) {
                historyManager.add(task);
            }
        }
        for (Epic epic : getAllEpic()) {
            if (epic.getId() == id) {
                historyManager.add(epic);
            }
        }
        for (Subtask subtask : getAllSubtask()) {
            if (subtask.getId() == id) {
                historyManager.add(subtask);
            }
        }
    }

    // Метод сохранения
    public void save() {

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(HEADER_CSV_FILE);

            for (Task task : getAllTask()) {
                writer.write(toStringTask(task) + "\n");
            }

            for (Epic epic : getAllEpic()) {
                writer.write(toStringEpic(epic) + "\n");
            }

            for (Subtask subtask : getAllSubtask()) {
                writer.write(toStringSubtask(subtask) + "\n");
            }

            writer.println();

            writer.println(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }
    }

    String toStringTask(Task task) {
        return String.format("%s,%s,%s,%s,%s", task.getId(), "TASK",
                task.getName(), task.getStatus(), task.getDescription());
    }

    String toStringEpic(Epic epic) {
        return String.format("%s,%s,%s,%s,%s", epic.getId(), "EPIC", epic.getName(), epic.getStatus(), epic.getDescription());

    }

    String toStringSubtask(Subtask subtask) {
        return String.format("%s,%s,%s,%s,%s,%s", subtask.getId(), "SUBTASK", subtask.getName(), subtask.getStatus(),
                subtask.getDescription(), subtask.getEpicId());
    }

    // Метод создания задачи из строки
    private Task fromString(String value) {
        String[] params = value.split(",");
        if (params[1].equals("EPIC")) {
            Epic epic = new Epic(params[4], params[2], Status.valueOf(params[3].toUpperCase()));
            epic.setId(Integer.parseInt(params[0]));
            epic.setStatus(Status.valueOf(params[3].toUpperCase()));
            return epic;
        } else if (params[1].equals("SUBTASK")) {
            Subtask subtask = new Subtask(params[4], params[2], Status.valueOf(params[3].toUpperCase()),
                    Integer.parseInt(params[5]));
            subtask.setId(Integer.parseInt(params[0]));
            return subtask;
        } else {
            Task task = new Task(params[4], params[2], Status.valueOf(params[3].toUpperCase()));
            task.setId(Integer.parseInt(params[0]));
            return task;
        }
    }

    // Метод для сохранения истории в CSV
    static String historyToString(HistoryManager manager) {
        StringBuilder str = new StringBuilder();

        if (manager.getHistory().isEmpty()) {
            return "";
        }

        for (Task task : manager.getHistory()) {
            str.append(task.getId()).append(",");
        }

        if (str.length() != 0) {
            str.deleteCharAt(str.length() - 1);
        }

        return str.toString();
    }

    // Метод восстановления менеджера истории из CSV
    static List<Integer> historyFromString(String value) {
        List<Integer> toReturn = new ArrayList<>();
        if (value != null) {
            String[] id = value.split(",");

            for (String number : id) {
                toReturn.add(Integer.parseInt(number));
            }

            return toReturn;
        }
        return toReturn;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }


    @Override
    public void clearByIdTask(int id) {
        super.clearByIdTask(id);
        save();
    }

    @Override
    public void clearByIdSubtask(int id) {
        super.clearByIdSubtask(id);
        save();
    }

    @Override
    public void clearByIdEpic(int id) {
        super.clearByIdEpic(id);
        save();
    }


    @Override
    public void clearAllTask() {
        super.clearAllTask();
        save();
    }

    @Override
    public void clearAllSubtask() {
        super.clearAllSubtask();
        save();
    }

    @Override
    public void clearAllEpic() {
        super.clearAllEpic();
        save();
    }

    @Override
    public Task receiveByIdTask(int id) {
        Task task = super.receiveByIdTask(id);
        save();
        return task;
    }

    @Override
    public Subtask receiveByIdSubtask(int id) {
        Subtask subtask = super.receiveByIdSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic receiveByIdEpic(int id) {
        Epic epic = super.receiveByIdEpic(id);
        save();
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }
}