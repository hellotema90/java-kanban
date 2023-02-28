package manager;

import tasks.Task;
import tasks.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    //Указатель на первый элемент списка
    private Node<Task> head;
    //Указатель на последний элемент списка
    private Node<Task> tail;
    private HashMap<Integer, Node<Task>> idByNode = new HashMap<>();
    int size = 0;
    public class CustomLinkedList {
        public void linkLast(Task task) {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            if (idByNode.containsKey(task.getId())) {
                removeNode(idByNode.get(task.getId()));
            }

            idByNode.put(task.getId(), newNode);
            size++;
        }

        public void removeNode(Node<Task> node) {
            if (node != null) {
                final Node<Task> next = node.next;
                final Node<Task> prev = node.prev;

                if (prev == null) {
                    head = next;
                } else {
                    prev.next = next;
                    node.prev = null;
                }
                if (next == null) {
                    tail = prev;
                } else {
                    next.prev = prev;
                    node.next = null;
                }
                idByNode.remove(node.data.getId());
                size--;
            }
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> tasks = new ArrayList<>();
            Node<Task> temp = head;
            while (temp != null) {
                tasks.add(temp.data);
                temp = temp.next;
            }
            return tasks;
        }
    }

    CustomLinkedList customLinkedList = new CustomLinkedList();
    @Override
    public void add(Task task) {
        customLinkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeNode(idByNode.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }
}

