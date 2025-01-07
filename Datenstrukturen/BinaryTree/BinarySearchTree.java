package BinaryTree;

import java.util.*;

// Tree-Interface-Definition
interface Tree<T extends Comparable<T>> {
    boolean isEmpty();

    int countNodes();

    boolean search(T value);

    T getMin();

    T getMax();

    int countNodesWithValue(T value);

    void insert(T value);

    void clear();

    boolean deleteOne(T value);

    int deleteAll(T value);

    T[] toArray();

    void print();

    int depth();

    void balance();
}

// Implementierung der Tree-Schnittstelle
public class BinarySearchTree<T extends Comparable<T>> implements Tree<T> {
    private Node root;

    // Innere Klasse Node
    private class Node {
        T value;
        Node leftNode;
        Node rightNode;

        Node(T value) {
            this.value = value;
        }
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public int countNodes() {
        return countNodesRecursive(root);
    }

    private int countNodesRecursive(Node node) {
        if (node == null)
            return 0;
        return 1 + countNodesRecursive(node.leftNode) + countNodesRecursive(node.rightNode);
    }

    @Override
    public boolean search(T value) {
        return searchRecursive(root, value);
    }

    private boolean searchRecursive(Node node, T value) {
        if (node == null)
            return false;
        int comparison = value.compareTo(node.value);
        if (comparison == 0)
            return true;
        return comparison < 0 ? searchRecursive(node.leftNode, value) : searchRecursive(node.rightNode, value);
    }

    @Override
    public T getMin() {
        if (isEmpty())
            throw new NoSuchElementException("Der Baum ist leer.");
        Node current = root;
        while (current.leftNode != null) {
            current = current.leftNode;
        }
        return current.value;
    }

    @Override
    public T getMax() {
        if (isEmpty())
            throw new NoSuchElementException("Der Baum ist leer.");
        Node current = root;
        while (current.rightNode != null) {
            current = current.rightNode;
        }
        return current.value;
    }

    @Override
    public int countNodesWithValue(T value) {
        return countNodesWithValueRecursive(root, value);
    }

    private int countNodesWithValueRecursive(Node node, T value) {
        if (node == null)
            return 0;
        int count = node.value.equals(value) ? 1 : 0;
        return count + countNodesWithValueRecursive(node.leftNode, value)
                + countNodesWithValueRecursive(node.rightNode, value);
    }

    @Override
    public void insert(T value) {
        root = insertRecursive(root, value);
    }

    private Node insertRecursive(Node node, T value) {
        if (node == null)
            return new Node(value);
        if (value.compareTo(node.value) < 0) {
            node.leftNode = insertRecursive(node.leftNode, value);
        } else {
            node.rightNode = insertRecursive(node.rightNode, value);
        }
        return node;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean deleteOne(T value) {
        DeleteResult result = deleteOneRecursive(root, value);
        root = result.node;
        return result.deleted;
    }

    private DeleteResult deleteOneRecursive(Node node, T value) {
        if (node == null)
            return new DeleteResult(null, false);
        if (value.compareTo(node.value) < 0) {
            DeleteResult result = deleteOneRecursive(node.leftNode, value);
            node.leftNode = result.node;
            return new DeleteResult(node, result.deleted);
        } else if (value.compareTo(node.value) > 0) {
            DeleteResult result = deleteOneRecursive(node.rightNode, value);
            node.rightNode = result.node;
            return new DeleteResult(node, result.deleted);
        } else {
            // Node to delete found
            if (node.leftNode == null)
                return new DeleteResult(node.rightNode, true);
            if (node.rightNode == null)
                return new DeleteResult(node.leftNode, true);

            Node successor = getMinNode(node.rightNode);
            node.value = successor.value;
            DeleteResult result = deleteOneRecursive(node.rightNode, successor.value);
            node.rightNode = result.node;
            return new DeleteResult(node, true);
        }
    }

    private Node getMinNode(Node node) {
        while (node.leftNode != null) {
            node = node.leftNode;
        }
        return node;
    }

    private class DeleteResult {
        Node node;
        boolean deleted;

        DeleteResult(Node node, boolean deleted) {
            this.node = node;
            this.deleted = deleted;
        }
    }

    @Override
    public int deleteAll(T value) {
        int count = 0;
        while (deleteOne(value)) {
            count++;
        }
        return count;
    }

    @Override
    public T[] toArray() {
        List<T> list = new ArrayList<>();
        inOrderTraversal(root, list);
        return (T[]) list.toArray();
    }

    private void inOrderTraversal(Node node, List<T> list) {
        if (node != null) {
            inOrderTraversal(node.leftNode, list);
            list.add(node.value);
            inOrderTraversal(node.rightNode, list);
        }
    }

    @Override
    public void print() {
        printRecursive(root, "", true);
    }

    private void printRecursive(Node node, String indent, boolean isRight) {
        if (node != null) {
            System.out.println(indent + (isRight ? "\u2514-- " : "\u251C-- ") + node.value);
            printRecursive(node.leftNode, indent + (isRight ? "    " : "|   "), false);
            printRecursive(node.rightNode, indent + (isRight ? "    " : "|   "), true);
        }
    }

    @Override
    public int depth() {
        return calculateDepth(root);
    }

    private int calculateDepth(Node node) {
        if (node == null)
            return 0;
        return 1 + Math.max(calculateDepth(node.leftNode), calculateDepth(node.rightNode));
    }

    @Override
    public void balance() {
        List<T> sortedValues = new ArrayList<>();
        inOrderTraversal(root, sortedValues);
        root = buildBalancedTree(sortedValues, 0, sortedValues.size() - 1);
    }

    private Node buildBalancedTree(List<T> values, int start, int end) {
        if (start > end)
            return null;
        int mid = (start + end) / 2;
        Node node = new Node(values.get(mid));
        node.leftNode = buildBalancedTree(values, start, mid - 1);
        node.rightNode = buildBalancedTree(values, mid + 1, end);
        return node;
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(3);
        bst.insert(7);
        bst.insert(12);
        bst.insert(20);
        bst.insert(4);

        System.out.println("Baum ist leer: " + bst.isEmpty());
        System.out.println("Knotenanzahl: " + bst.countNodes());
        System.out.println("Suche nach 7: " + bst.search(7));
        System.out.println("Min: " + bst.getMin());
        System.out.println("Max: " + bst.getMax());
        bst.print();
        bst.balance();
        System.out.println("Nach Balance:");
        bst.print();
    }
}
