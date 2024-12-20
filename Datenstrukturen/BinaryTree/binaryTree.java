package BinaryTree;

public class binaryTree {

    Node root = null;
    Node tmp;

    public boolean isEmpty() {
        return root == null;
    }

    public int countNodes() {
        return countNodesHelper(root);

    }

    private int countNodesHelper(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodesHelper(node.getLeft()) + countNodesHelper(node.getRight());
    }

    public boolean search(int value) {
        return searchHelper(root, value);
    }

    private boolean searchHelper(Node node, int value) {
        if (node == null) {
            return false; // Basisfall: Kein Knoten vorhanden
        }
        if (node.getValue() == value) {
            return true; // Zielwert gefunden
        }
        // Suche im linken und rechten Teilbaum
        return searchHelper(node.getLeft(), value) || searchHelper(node.getRight(), value);
    }

    public int getMin() {
        return getMinHelper(root);
    }

    private int getMinHelper(Node node) {
        if (node == null) {
            return Integer.MAX_VALUE; // Rückgabe eines großen Werts für leere Teilbäume
        }
        // Finde den kleinsten Wert im linken und rechten Teilbaum
        int leftMin = getMinHelper(node.getLeft());
        int rightMin = getMinHelper(node.getRight());
        // Der kleinste Wert ist der aktuelle Wert oder der kleinste Wert in den
        // Teilbäumen
        return Math.min(node.getValue(), Math.min(leftMin, rightMin));
    }

    public int getMax() {
        return getMaxHelper(root);
    }

    private int getMaxHelper(Node node) {
        if (node == null) {
            return Integer.MIN_VALUE; // Rückgabe eines kleinen Werts für leere Teilbäume
        }
        // Finde den größten Wert im linken und rechten Teilbaum
        int leftMax = getMaxHelper(node.getLeft());
        int rightMax = getMaxHelper(node.getRight());
        // Der größte Wert ist der aktuelle Wert oder der größte Wert in den
        // Teilbäumen
        return Math.max(node.getValue(), Math.max(leftMax, rightMax));
    }

    public int countNodesWithValue(int value) {
        return countNodesWithValueHelper(root, value);
    }

    private int countNodesWithValueHelper(Node node, int value) {
        if (node == null) {
            return 0;
        }

        return (node.getValue() == value ? 1 : 0) + countNodesWithValueHelper(node.getLeft(), value)
                + countNodesWithValueHelper(node.getRight(), value);
    }

    // to balance out use the median
}

class Node {
    Node leftNode = null;
    Node rightNode = null;
    int value;

    public Node getLeft() {
        return leftNode;
    }

    public Node getRight() {
        return rightNode;
    }

    public int getValue() {
        return value;
    }

    public void setLeft(Node left) {
        this.leftNode = left;
    }

    public void setRight(Node right) {
        this.rightNode = right;
    }
}
