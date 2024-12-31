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

    /*
     * private boolean searchHelper(Node node, int value) {
     * if (node == null) {
     * return false; // Basisfall: Kein Knoten vorhanden
     * }
     * if (node.getValue() == value) {
     * return true; // Zielwert gefunden
     * }
     * // Suche im linken und rechten Teilbaum
     * return searchHelper(node.getLeft(), value) || searchHelper(node.getRight(),
     * value);
     * }
     */

    private boolean searchHelper(Node node, int value) {
        if (node == null) {
            return false; // Basisfall: Wert nicht gefunden
        }

        if (value == node.getValue()) {
            return true; // Wert gefunden
        }

        if (value < node.getValue()) {
            return searchHelper(node.getLeft(), value); // Links weitersuchen
        } else {
            return searchHelper(node.getRight(), value); // Rechts weitersuchen
        }
    }

    public int getMin() {
        return getMinHelper(root);
    }

    private int getMinHelper(Node node) {
        return node.getLeft() == null ? node.getValue() : getMaxHelper(node.getLeft());
    }

    public int getMax() {
        return getMaxHelper(root);
    }

    private int getMaxHelper(Node node) {
        return node.getRight() == null ? node.getValue() : getMaxHelper(node.getRight());
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
