package manager;


import http.KVTaskClient;
import http.adapters.InstantAdapter;
import com.google.gson.*;
import manager.FileBackedTasksManager;
import manager.HistoryManager;
import manager.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    final static String KEY_TASKS = "tasks";
    final static String KEY_SUBTASKS = "subtasks";
    final static String KEY_EPICS = "epics";
    final static String KEY_HISTORY = "history";
    final KVTaskClient client;
    private static final Gson gson =
            new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    public HTTPTaskManager(String path){
        super();
        client = new KVTaskClient(path);

        JsonElement jsonTasks = JsonParser.parseString(client.load(KEY_TASKS));
        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();
            for (JsonElement jsonTask : jsonTasksArray) {
                Task task = gson.fromJson(jsonTask, Task.class);
                this.createTask(task);
            }
        }

        JsonElement jsonEpics = JsonParser.parseString(client.load(KEY_EPICS));
        if (!jsonEpics.isJsonNull()) {
            JsonArray jsonEpicsArray = jsonEpics.getAsJsonArray();
            for (JsonElement jsonEpic : jsonEpicsArray) {
                Epic task = gson.fromJson(jsonEpic, Epic.class);
                this.createEpic(task);
            }
        }

        JsonElement jsonSubtasks = JsonParser.parseString(client.load(KEY_SUBTASKS));
        if (!jsonSubtasks.isJsonNull()) {
            JsonArray jsonSubtasksArray = jsonSubtasks.getAsJsonArray();
            for (JsonElement jsonSubtask : jsonSubtasksArray) {
                Subtask task = gson.fromJson(jsonSubtask, Subtask.class);
                this.createSubtask(task);
            }
        }

        JsonElement jsonHistoryList = JsonParser.parseString(client.load(KEY_HISTORY));
        if (!jsonHistoryList.isJsonNull()) {
            JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
            for (JsonElement jsonTaskId : jsonHistoryArray) {
                int taskId = jsonTaskId.getAsInt();
                if (this.subtaskMap.containsKey(taskId)) {
                    this.receiveByIdSubtask(taskId);
                } else if (this.epicMap.containsKey(taskId)) {
                    this.receiveByIdEpic(taskId);
                } else if (this.taskMap.containsKey(taskId)) {
                    this.receiveByIdTask(taskId);
                }
            }
        }
    }

    @Override
    public void save() {
        client.put(KEY_TASKS, gson.toJson(taskMap.values()));
        client.put(KEY_SUBTASKS, gson.toJson(subtaskMap.values()));
        client.put(KEY_EPICS, gson.toJson(epicMap.values()));
        client.put(KEY_HISTORY, gson.toJson(this.getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList())));
    }

}
