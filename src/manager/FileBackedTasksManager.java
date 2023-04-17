package manager;

import http.KVServer;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;



public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;
    private static final String HEADER_CSV_FILE = "id,type,name,status,description,epic" + System.lineSeparator();


    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public FileBackedTasksManager(){
        this.file = new File("http://localhost:" + KVServer.PORT);
    }


    public static void main(String[] args) {
        Path path = Path.of("taskdata.csv");
        File file = new File(String.valueOf(path));



        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File(String.valueOf("taskdata.csv")));
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

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);
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


    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.isEmpty()) {
                    break;
                }

                Task task = manager.fromString(line);

                if (task instanceof Epic) {
                    manager.createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.createSubtask((Subtask) task);
                } else {
                    manager.createTask(task);
                }
            }

            for(Integer fromId : historyFromString(bufferedReader.readLine())){
                manager.addToHistory(fromId);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
        return manager;
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
                writer.write(toStringTask(task) + System.lineSeparator());
            }

            for (Epic epic : getAllEpic()) {
                writer.write(toStringEpic(epic) + System.lineSeparator());
            }

            for (Subtask subtask : getAllSubtask()) {
                writer.write(toStringSubtask(subtask) + System.lineSeparator());
            }

            writer.println();

            writer.println(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл");
        }
    }

    String toStringTask(Task task) {
        return String.format("%s,%s,%s,%s,%s,%s,%s", task.getId(), "TASK",
                task.getName(), task.getStatus(), task.getDescription(), task.getStartTime(),
                task.getDuration()); // String.valueOf(task.getStartTime()),String.valueOf(task.getDuration()),
    }

    String toStringEpic(Epic epic) {
        return String.format("%s,%s,%s,%s,%s,%s,%s", epic.getId(), "EPIC", epic.getName(),
                epic.getStatus(), epic.getDescription(), epic.getStartTime(),
                epic.getDuration());

    }

    String toStringSubtask(Subtask subtask) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", subtask.getId(), "SUBTASK", subtask.getName(), subtask.getStatus(),
                subtask.getDescription(), subtask.getEpicId(), subtask.getStartTime(),
                subtask.getDuration());
    }

    // Метод создания задачи из строки
    private Task fromString(String value) {
        String[] params = value.split(",");
        int id = Integer.parseInt(params[0]);
        String name = params[2];
        Status status = Status.valueOf(params[3].toUpperCase());
        String description = params[4];
        Instant startTime = Instant.parse(params[6]);
        long duration = Long.parseLong(params[7]);
        switch (TaskType.valueOf(params[1])){
            case EPIC:
                Epic epic = new Epic(description, name, status, startTime, duration);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(description, name, status, Integer.parseInt(params[5]), startTime, duration);
                subtask.setId(id);
                return subtask;
            case TASK:
                Task task = new Task(description, name, status, startTime, duration);
                task.setId(id);
                return task;
        }
        return null;
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

        if(value == null || value.isEmpty()){
            return toReturn;
        }

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
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
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