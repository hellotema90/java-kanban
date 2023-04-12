package tasks;

import java.time.Instant;
import java.util.Objects;

public class Subtask extends Task {
    protected int epicId;

    //конструктор на создание новой задачи
    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description,status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, int epicId, Instant startTime, long duration) {
        super(description, name, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }



    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", epicId'" + epicId + '\'' +
                ", startTime='" + getStartTime().toEpochMilli() + '\'' +
                ", endTime='" + getEndTime().toEpochMilli() + '\'' +
                ", duration='" + getDuration() +
                '}' + System.lineSeparator();
    }
}