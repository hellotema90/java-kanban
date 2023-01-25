public class Epic extends Task {
    //конструктор на создание новой задачи
    public Epic(String name, String description) {
        super(name, description);
    }
    //конструктор по умолчанию
    public Epic() {
    }
    // переопределение toString
    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}