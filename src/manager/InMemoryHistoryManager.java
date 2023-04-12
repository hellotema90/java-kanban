package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    //Указатель на первый элемент списка
    private Node<Task> head;
    //Указатель на последний элемент списка
    private Node<Task> tail;
    private HashMap<Integer, Node<Task>> idByNode = new HashMap<>();
    private CustomLinkedList customLinkedList = new CustomLinkedList();

    public class CustomLinkedList {
        public void linkLast(Task task) {
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.setNext(newNode); //начали
            }
            if (idByNode.containsKey(task.getId())) {
                removeNode(idByNode.get(task.getId()));
            }

            idByNode.put(task.getId(), newNode);
        }

        public void removeNode(Node<Task> node) {
            if (node != null) {
                final Node<Task> next = node.getNext();
                final Node<Task> prev = node.getPrev();

                if (prev == null) {
                    head = next;
                } else {
                    prev.setNext(next);
                    node.setPrev(null);
                }
                if (next == null) {
                    tail = prev;
                } else {
                    next.setPrev(prev);
                    node.setNext(null);
                }
                idByNode.remove(node.getData().getId());
            }
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> tasks = new ArrayList<>();
            Node<Task> temp = head;
            while (temp != null) {
                tasks.add(temp.getData());
                temp = temp.getNext();
            }
            return tasks;
        }
    }

    @Override
    public void add(Task task) {
        //customLinkedList.linkLast(task);
        if (idByNode.containsKey(task.getId())) {
            remove(task.getId());
        }
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

