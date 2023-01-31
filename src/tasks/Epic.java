package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskId;

    //конструктор на создание новой задачи
    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subtaskId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    public void addSubtaskId(int id) {
        subtaskId.add(id);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", subtaskId='" + subtaskId + '\'' +
                '}';
        boolean isNotEmpty = subtaskId != null && !subtaskId.isEmpty();
        if (isNotEmpty) {
            result += "subtaskId='" + subtaskId + '\'' +
                    '}';
        }
        return result;
    }
}