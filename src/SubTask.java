public class SubTask extends Task {
    int idEpic;
    //конструктор на создание новой задачи
    public SubTask(String name, String description, int idEpic) {
        super(name, description);
        this.idEpic = idEpic;
    }
    //конструктор по умолчанию
    public SubTask(){
    }
    // переопределение toString
    @Override
    public String toString() {
        return "SubTask{" +
                "idEpic=" + idEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
