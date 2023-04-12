package tasks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class
Epic extends Task {
    protected ArrayList<Integer> subtaskId;
    private Instant endTime;

    //конструктор на создание новой задачи
    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subtaskId = new ArrayList<>();
    }
    public Epic(String name, String description, Status status, Instant startTime, long duration) {
        super(description, name, status, startTime, duration);
        this.endTime = super.getEndTime();
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
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskId, epic.subtaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskId);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", subtaskId='" + subtaskId + '\'' +
                ", startTime='" + getStartTime().toEpochMilli() + '\'' +
                ", endTime='" + getEndTime().toEpochMilli() + '\'' +
                ", duration='" + getDuration() +
                '}' + System.lineSeparator();
        boolean isNotEmpty = subtaskId != null && !subtaskId.isEmpty();
        if (isNotEmpty) {
            result += "subtaskId='" + subtaskId + '\'' +
                    '}' + System.lineSeparator();
        }
        return result;
    }
}