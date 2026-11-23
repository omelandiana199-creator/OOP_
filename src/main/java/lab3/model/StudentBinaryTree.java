package lab3.model;

import java.util.function.Consumer;

// ЛР 8: Структура бінарного дерева пошуку
public class StudentBinaryTree {
    private static class Node {
        Student student;
        Node left;
        Node right;

        Node(Student student) {
            this.student = student;
        }
    }

    private Node root;

    // Вставка на основі середнього балу
    public void insert(Student student) {
        root = insertRecursive(root, student);
    }

    private Node insertRecursive(Node current, Student student) {
        if (current == null) {
            return new Node(student);
        }

        if (student.getAverageGrade() < current.student.getAverageGrade()) {
            current.left = insertRecursive(current.left, student);
        } else if (student.getAverageGrade() > current.student.getAverageGrade()) {
            current.right = insertRecursive(current.right, student);
        }
        return current;
    }

    // Обхід In-Order (від найменшого балу до найбільшого)
    public void inOrderTraversal(Consumer<Student> action) {
        inOrderRecursive(root, action);
    }

    private void inOrderRecursive(Node node, Consumer<Student> action) {
        if (node != null) {
            inOrderRecursive(node.left, action);
            action.accept(node.student);
            inOrderRecursive(node.right, action);
        }
    }
}