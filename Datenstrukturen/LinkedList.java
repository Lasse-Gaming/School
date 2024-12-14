class LinkedList {
    Node first;
    Node current;
    Node last;
    int length;

    public static void main(String[] args) {
        LinkedList lst = new LinkedList(new Node(9));
        lst.add(new Node(2));
        lst.add(new Node(4));

        lst.print();
        System.out.println();

        lst.mergeSort();
        System.out.println(lst.getIndex(9));
        System.out.println();
        lst.print();
    }

    LinkedList(Node node) {
        first = node;
        last = node;
        length = 1;
    }

    LinkedList(Node first, Node last, int length) {
        this.first = first;
        this.last = last;
        this.length = length;
        this.current = first;
    }

    public void sort() {
        for (int i = 0; i < length - 1; i++) {
            current = first;
            for (int j = 0; j < length - 1; j++) {
                if (current.getValue() > current.getChild().getValue()) {
                    int tmp = current.getValue();
                    current.setValue(current.getChild().getValue());
                    current.getChild().setValue(tmp);
                }
                current = current.getChild();
            }
        }

    }

    public void removeFirst() {
        this.first = this.first.getChild();
        this.length -= 1;
    }

    public LinkedList[] split() {

        Node mid = getMid();
        LinkedList firstHalf;
        LinkedList secondHalf;

        if (this.length % 2 == 0) {
            firstHalf = new LinkedList(first, mid, length / 2);
            secondHalf = new LinkedList(mid.getChild(), last, length / 2);
        } else {
            firstHalf = new LinkedList(first, mid, length / 2);
            secondHalf = new LinkedList(mid.getChild(), last, length / 2 + 1);
        }
        mid.setChild(null);
        return new LinkedList[] { firstHalf, secondHalf };
    }

    public Node getMid(Node start) {
        Node slow = start;
        Node fast = start;

        while (fast != null && fast.getChild() != null && fast.getChild().getChild() != null) {
            slow = slow.getChild();
            fast = fast.getChild().getChild();
        }

        return slow;
    }

    public Node getMid() {
        Node fast = first;
        Node slow = first;
        if (fast != null && fast.getChild() != null) {
            fast = fast.getChild().getChild();
        }
        while (fast != null && fast.getChild() != null) {
            slow = slow.getChild();
            fast = fast.getChild().getChild();
        }

        return slow;
    }

    public void difSort() {
        this.first = difMergeSort(first);
        // current = first;
        // int count = 1;
        /*
         * while (current != null && current.getChild() != null) {
         * current = current.getChild();
         * count++;
         * }
         * this.last = current;
         * this.length = count;
         */
    }

    public Node difMergeSort(Node start) {
        if (start == null || start.getChild() == null) {
            return start;
        } else {
            Node mid = getMid(start);
            Node nextToMid = mid.getChild();
            mid.setChild(null);

            Node left = difMergeSort(start);
            Node right = difMergeSort(nextToMid);
            return difMerge(left, right);
        }

    }

    public Node difMerge(Node left, Node right) {
        Node sorted = new Node();
        current = sorted;

        while (left != null && right != null) {
            if (left.getValue() <= right.getValue()) {
                current.setChild(left);
                left = left.getChild();
            } else {
                current.setChild(right);
                right = right.getChild();
            }
            current = current.getChild();
        }
        if (left != null) {
            current.setChild(left);
        } else if (right != null) {
            current.setChild(right);
        }
        return sorted.getChild();
    }

    public void mergeSort() {
        if (this.length <= 1) {
            return;
        } else {
            LinkedList[] Linkedlstlst = this.split();
            LinkedList left = Linkedlstlst[0];
            LinkedList right = Linkedlstlst[1];

            left.mergeSort();
            right.mergeSort();
            this.merge(left, right);
        }
    }

    public void merge(LinkedList left, LinkedList right) {
        if (left.getFirst().getValue() < right.getFirst().getValue()) {
            this.first = new Node(left.getFirst().getValue());
            left.removeFirst();
        } else {
            this.first = new Node(right.getFirst().getValue());
            right.removeFirst();
        }

        last = first;
        length = 1;

        while (left.getLength() > 0 && right.getLength() > 0) {
            if (left.getFirst().getValue() > right.getFirst().getValue()) {
                add(right.getFirst().getValue());
                right.removeFirst();
            } else {
                add(left.getFirst().getValue());
                left.removeFirst();
            }
        }

        if (left.getLength() > 0) {
            this.addList(left);
            left.deleteAll();
        } else if (right.getLength() > 0) {
            this.addList(right);
            right.deleteAll();
        }
    }

    public void print() {
        current = first;
        while (current != null) {
            System.out.print(current.getValue());
            current = current.getChild();
        }
    }

    public void deleteAll() {
        first = null;
        last = null;
        current = null;
        length = 0;
    }

    public void add(Node node) {
        last.setChild(node);
        last = node;
        length += 1;
    }

    public void addList(LinkedList lst) {
        if (lst.getFirst() == null) {
            return;
        }
        this.last.setChild(lst.getFirst());

        this.length += lst.getLength();

        this.last = lst.getLast();
    }

    public void add(int value) {
        Node node = new Node(value);
        last.setChild(node);
        last = node;
        length += 1;
    }

    public void insert(Node node, int index) {
        setCurrentToIndex(index);
        node.setChild(current.getChild());
        current.setChild(node);
        length += 1;
    }

    public void removeAt(int index) {
        setCurrentToIndex(index - 1);
        current.setChild(current.getChild().getChild());
        length -= 1;
    }

    public void setCurrentToIndex(int index) {
        if (index > length) {
            System.out.println("der index existiert nicht!");
            return;
        }
        int count = 0;
        current = first;
        while (count < index) {
            current = current.getChild();
        }
    }

    public int getIndex(int value) {
        current = first;
        int count = 0;
        while (count < length) {
            if (current.getValue() != value) {
                current = current.getChild();
            } else {
                return count;
            }
            count += 1;
        }
        return -1;

    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Node getFirst() {
        return first;
    }

    public void setFirst(Node first) {
        this.first = first;
    }

    public Node getLast() {
        return last;
    }

    public void setLast(Node last) {
        this.last = last;
    }

}

class Node {
    private Node child = null;
    private int value = 0;

    Node() {
    }

    Node(int value) {
        this.value = value;
    }

    Node(int value, Node node) {
        this.value = value;
        this.child = node;

    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}