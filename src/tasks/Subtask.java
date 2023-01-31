package tasks;

public class Subtask extends Task {
    protected int epicId;

    //конструктор на создание новой задачи
    public Subtask(String name, String description, Status status,int epicId) {
        super(name, description,status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }



    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", epicId'" + epicId + '\'' +
                '}';
    }
}