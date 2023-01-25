public class Task {
    protected String name; //имя задачи
    protected String description; //описание задачи
    protected String status; //статус задачи

    //конструктор на создание новой задачи
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
    }

    //конструктор по умолчанию
    public Task() {
    }

    //возвращает клас объекта
    public Object getClas() {
        return this.getClass();
    }

    // переопределение toString
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}