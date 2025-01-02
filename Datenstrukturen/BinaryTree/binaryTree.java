package BinaryTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class binaryTree {

    private Node root = null;
    private Node tmp = null;

    public static void main(String[] args) {
        System.out.println(14 / 2);
        System.exit(0);
        binaryTree bst = new binaryTree();
        bst.insert(6);
        // for (int i = 0; i < 20; i++) {
        bst.insert(10);
        // }
        bst.insert(5);
        // bst.insert(6);
        bst.insert(4);
        // bst.insert(14);
        bst.insert(11);
        bst.insert(20);
        bst.insert(14);
        bst.insert(2);
        bst.insert(3);
        bst.insert(1);
        bst.insert(7);
        System.out.println(Arrays.toString(bst.toArray()));
        bst.print();
        System.out.println(bst.searchNodeWithChildNodewithValue(bst.getRoot(), 4).getValue());
        // for (int i = 0; i < 2; i++) {
        // bst.deleteAll(10);
        // }
        bst.balance();
        // bst.deleteOne(6);
        // bst.deleteOne(6);
        bst.print();
        System.out.println(bst.depth());
        // System.out.println();
    }

    public Node getRoot() {
        return root;
    }

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

    public void insert(int value) {
        if (root == null) {
            root = new Node(value);
            return;
        }
        insertHelper(root, value);
    }

    private void insertHelper(Node node, int value) {
        if (node.getValue() > value) {
            if (node.getLeft() == null) {
                node.setLeft(new Node(value));
                return;
            } else {
                insertHelper(node.getLeft(), value);
            }
        } else {
            if (node.getRight() == null) {
                node.setRight(new Node(value));
            } else {
                insertHelper(node.getRight(), value);
            }
        }
    }

    public void clear() {
        root = null;
    }

    public void deleteOne(int value) {

        if (!this.search(value))
            return;
        Node parent = null;
        Node possibleParent = searchNodeWithChildNodewithValue(root, value);
        while (possibleParent != null) {
            parent = possibleParent;
            if (value < parent.getValue()) {
                possibleParent = searchNodeWithChildNodewithValue(possibleParent.getLeft(), value);
            } else {
                possibleParent = searchNodeWithChildNodewithValue(possibleParent.getRight(), value);
            }
        }
        System.out.println("while schleife zu ende");
        deleteOneHelper(parent, value);

    }

    private void deleteOneHelper(Node parent, int value) {
        if (parent == null && value == root.getValue()) {
            tmp = root.getRight();
            root = root.getLeft();
            insertNode(tmp);

        } else {
            if (parent.getLeft() != null && parent.getLeft().getValue() == value) {
                tmp = parent.getLeft().getRight();
                parent.setLeft(parent.getLeft().getLeft());
                insertNode(tmp);

            } else {
                tmp = parent.getRight().getRight();
                parent.setRight(parent.getRight().getLeft());
                insertNode(tmp);
            }
        }
    }

    public void deleteAll(int value) {
        Node parent = searchNodeWithChildNodewithValue(root, value);
        while (parent != null) {
            deleteOneHelper(parent, value);
            parent = searchNodeWithChildNodewithValue(root, value);
        }
    }

    private Node searchNodeWithChildNodewithValue(Node node, int value) {
        if (node == null) {
            return null;
        }
        if (value < node.getValue()) {
            if (node.getLeft() != null && node.getLeft().getValue() == value) {
                return node;
            } else {
                return searchNodeWithChildNodewithValue(node.getLeft(), value);
            }
        } else {
            if (node.getRight() != null && node.getRight().getValue() == value) {
                return node;
            } else {
                return searchNodeWithChildNodewithValue(node.getRight(), value);
            }
        }
    }

    private void insertNode(Node node) {
        if (root == null) {
            root = node;
            return;
        }
        if (node != null) {
            insertNodeHelper(root, node);
        }
    }

    private void insertNodeHelper(Node node, Node insertNode) {
        if (node.getValue() > insertNode.getValue()) {
            if (node.getLeft() == null) {
                node.setLeft(insertNode);
                return;
            } else {
                insertNodeHelper(node.getLeft(), insertNode);
            }
        } else {
            if (node.getRight() == null) {
                node.setRight(insertNode);
            } else {
                insertNodeHelper(node.getRight(), insertNode);
            }
        }
    }

    public int[] toArray() {
        List<Integer> sortedValues = new ArrayList<>();
        toArrayHelper(root, sortedValues);

        // Konvertiere die Liste in ein Array
        return sortedValues.stream().mapToInt(i -> i).toArray();
    }

    private void toArrayHelper(Node node, List<Integer> list) {
        if (node == null) {
            return;
        }
        // Besuche den linken Teilbaum
        toArrayHelper(node.getLeft(), list);

        // Füge den aktuellen Knotenwert hinzu
        list.add(node.getValue());

        // Besuche den rechten Teilbaum
        toArrayHelper(node.getRight(), list);
    }

    public void print() {
        printHelper(root, "", true);
    }

    private void printHelper(Node node, String indent, boolean isRight) {
        if (node != null) {
            System.out.println(indent + (isRight ? "\u2514-- " : "\u251C-- ") + node.value);
            printHelper(node.leftNode, indent + (isRight ? "    " : "|   "), false);
            printHelper(node.rightNode, indent + (isRight ? "    " : "|   "), true);
        }
    }

    public int depth() {
        return depthHelper(root);
    }

    private int depthHelper(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(depthHelper(node.getLeft()), depthHelper(node.getRight()));
    }

    public void balance() {
        int[] lst = toArray();
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        int element = lst[0];
        int count = 1;
        ArrayList<Integer> lstWithoutDuplicates = new ArrayList<>();
        for (int i = 1; i < lst.length; i++) {
            if (lst[i] == element) {
                count++;
            } else {
                lstWithoutDuplicates.add(element);
                map.put(element, count);
                element = lst[i];
                count = 1;
            }
        }
        root = balanceHelper(lstWithoutDuplicates.stream().mapToInt(i -> i).toArray(), 0, lst.length - 1, map);

        // map.put(,);
        // map.get();
    }

    public Node balanceHelper(int[] values, int start, int end, Map<Integer, Integer> map) {

        if (start > end)
            return null;
        int mid = (start + end) / 2;
        Node node = new Node(values[mid]);
        node.leftNode = balanceHelper(values, start, mid - 1, map);
        node.rightNode = balanceHelper(values, mid + 1, end, map);
        return node;
    }
}

class Node {
    Node leftNode = null;
    Node rightNode = null;
    int value;

    Node(int value) {
        this.value = value;
    }

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

    public String toString() {
        return String.valueOf(value);
    }
}
