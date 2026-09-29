package lab3.model;

import java.util.function.Consumer;

public class StudentBinaryTree {
    private class Node {
        Student data;
        Node left, right;

        Node(Student data) { this.data = data; }
    }

    private Node root;

    public void insert(Student student) {
        root = insertRecursive(root, student);
    }

    private Node insertRecursive(Node current, Student student) {
        if (current == null) {
            return new Node(student);
        }
        if (student.getId().compareTo(current.data.getId()) < 0) {
            current.left = insertRecursive(current.left, student);
        } else if (student.getId().compareTo(current.data.getId()) > 0) {
            current.right = insertRecursive(current.right, student);
        }
        return current;
    }

    // Обхід In-Order
    public void inOrderTraversal(Consumer<Student> action) {
        inOrderRecursive(root, action);
    }

    private void inOrderRecursive(Node node, Consumer<Student> action) {
        if (node != null) {
            inOrderRecursive(node.left, action);
            action.accept(node.data);
            inOrderRecursive(node.right, action);
        }
    }
}