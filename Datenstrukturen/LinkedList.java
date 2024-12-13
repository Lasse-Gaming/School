

class LinkedList{
    Node first;
    Node current;
    Node last;
    int length;

    public static void main(String[] args){
        LinkedList lst = new LinkedList(new Node(9));
        lst.add(new Node(2));
        lst.add(new Node(4));
        lst.add(new Node(1));
        lst.add(new Node(2));
        lst.add(new Node(4));
        lst.add(new Node(1));
        lst.add(new Node(1));
        lst.print();
        System.out.println();
        
        lst.LinkMergeSort();
        System.out.println(lst.getIndex(9));
        lst.print();
        System.out.println("");
        System.out.println("");
        lst.print();
        System.out.println("");
        for (LinkedList elem : lst.split()) {
            elem.print();
            System.out.println("");
        }
    }

    public LinkedList() {
    }

    

    LinkedList(Node node){
        first = node;
        last = node;
        length = 1;
    }
    LinkedList(Node first, Node last,int length){
        this.first = first;
        this.last = last;
        this.length = length;
        this.current = this.first;
    }

    public void addList(LinkedList lst){
        this.last.setChild(lst.getFirst());
        this.last = lst.getLast();
    }

    public void removeFirst(){
        this.first = this.first.getChild();
        length -=1;
    }


    public LinkedList[] split() {
        
        Node mid = getMid();
        LinkedList firstHalf = new LinkedList(first, mid, length/2);
        LinkedList secondHalf = new LinkedList(mid.getChild(), last, length/2);
        mid.setChild(null);

    
        return new LinkedList[] { firstHalf, secondHalf };
    }

    public Node getMid(){
        Node fast = first;
        Node slow = first;
        if(fast != null && fast.getChild() != null){
            fast = fast.getChild().getChild(); 
        }
        while(fast != null && fast.getChild() != null){
            slow = slow.getChild();
            fast = fast.getChild().getChild();
        }
        return slow;
    }

    public void LinkMergeSort() {
        if (this.length <= 1) {
            return;
        } else {
            LinkedList[] linklst = this.split();
            LinkedList leftList = linklst[0];
            LinkedList rightList = linklst[1];
    
            if (this.length % 2 == 0) {
                leftList.setLength(length / 2);
                rightList.setLength(length / 2);
            } else {
                leftList.setLength(length / 2);
                rightList.setLength(length / 2 + 1);
            }
    
            leftList.LinkMergeSort();
            rightList.LinkMergeSort();
            this.LinkMerge(leftList, rightList);
        }
    }
    
    public void LinkMerge(LinkedList leftList, LinkedList rightList) {
        if (leftList.getFirst().getValue() < rightList.getFirst().getValue()) {
            this.first = new Node(leftList.getFirst().getValue());
            leftList.removeFirst();
        } else {
            this.first = new Node(rightList.getFirst().getValue());
            rightList.removeFirst();
        }
    
        current = first;
    
        while (leftList.length > 0 && rightList.length > 0) {
            if (leftList.getFirst().getValue() < rightList.getFirst().getValue()) {
                current.setChild(new Node(leftList.getFirst().getValue()));
                leftList.removeFirst();
            } else {
                current.setChild(new Node(rightList.getFirst().getValue()));
                rightList.removeFirst();
            }
            current = current.getChild();
        }
    
        while (leftList.length > 0) {
            current.setChild(new Node(leftList.getFirst().getValue()));
            leftList.removeFirst();
            current = current.getChild();
        }
    
        while (rightList.length > 0) {
            current.setChild(new Node(rightList.getFirst().getValue()));
            rightList.removeFirst();
            current = current.getChild();
        }
    }
    



    public void sort(){
        for (int i = 0; i<length-1;i++){
            current = first;
            for(int j = 0; j< length-1; j++){
                if (current.getValue() > current.getChild().getValue()){
                    int tmp = current.getValue();
                    current.setValue(current.getChild().getValue());
                    current.getChild().setValue(tmp);
                }
                current = current.getChild();
            }
        }

    }

    public void print(){
        current = first;
        while(current != null){
            System.out.print(current.getValue());
            current = current.getChild();
        }
    }

    public void add(Node node){
        last.setChild(node);
        last = last.getChild();
        length +=1;
    }

    public void add(int value){
        last.setChild(new Node(value));
        last = last.getChild();
        length +=1;
    }

    public void insert(Node node, int index){
        setCurrentToIndex(index);
        node.setChild(current.getChild());
        current.setChild(node);
        length += 1;
    }

    public void removeAt(int index){
        setCurrentToIndex(index-1);
        current.setChild(current.getChild().getChild());
        length -= 1;
    }

    public void setCurrentToIndex(int index){
        if (index > length){
            System.out.println("der index existiert nicht!");
            return;  
        }
        int count = 0;
        current = first;
        while (count < index) {
            current = current.getChild();
        }
    }

    public int getIndex(int value){
        current = first;
        int count = 0;
        while (count < length){
            if (current.getValue() != value){
                current = current.getChild();
            }else{
                return count;
            }
            count +=1;
        }
        return -1;
        
    }

    public void setCurrent(Node current) {
        this.current = current;
    }

    public Node getCurrent() {
        return current;
    }

    public void setLast(Node last) {
        this.last = last;
    }

    public Node getLast() {
        return last;
    }

    public void setFirst(Node first) {
        this.first = first;
    } 
    
    public Node getFirst(){
        return first;
    }
    
    public int getLength(){
        return length;
    }
    
    public void setLength(int length) {
        this.length = length;
    }
    

}


class Node{
    private Node child = null;
    private int value = 0;
    
    Node(){}
    
    Node(int value){
        this.value = value;
    }

    Node(int value, Node child){
        this.value = value;
        this.child = child;

    }
    
    public Node getChild(){
        return child;
    }

    public void setChild(Node child){
        this.child = child;
    }

    public int getValue(){
        return value;
    }
    
    public void setValue(int value){
        this.value = value;
    }
}