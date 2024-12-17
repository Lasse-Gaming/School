import java.util.Arrays;

public class DynamicArray {

    Object[] data;

    public DynamicArray() {
        data = new Object[0];
    }

    public void add(Object o) {
        Object[] new_data = new Object[data.length + 1];
        for (int i = 0; i < data.length; i++) {
            new_data[i] = data[i];
        }
        new_data[data.length] = o;
        data = new_data;
    }

    public void remove(Object o) {
        int positionOfO = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == o)
                positionOfO = i;
        }

        if (positionOfO >= 0) {
            Object[] new_data = new Object[data.length - 1];
            for (int i = 0; i < data.length; i++) {
                if (i < positionOfO)
                    new_data[i] = data[i];
                if (i > positionOfO)
                    new_data[i - 1] = data[i];
            }
            data = new_data;
        }
    }

    public void show() {
        System.out.printf("%nAnzahl der Datenobjekte: %d%n", data.length);
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }
    }

    public static int[] mergeSort(int[] lst) {
        if (lst.length <= 1) {
            return lst;
        } else {
            int[] leftList = Arrays.copyOfRange(lst, 0, (int) lst.length / 2);
            int[] rightList = Arrays.copyOfRange(lst, (int) lst.length / 2, lst.length);

            leftList = mergeSort(leftList);
            rightList = mergeSort(rightList);
            return merge(leftList, rightList);
        }
    }

    public static int[] merge(int[] leftList, int[] rightList) {
        int[] lst = new int[leftList.length + rightList.length];
        int count = 0;
        int index = 0;
        for (int i = 0; i < leftList.length - 1; i++) {
            for (int j = count; j < lst.length - 1; j++) {
                if (leftList[i] <= rightList[j]) {
                    lst[index] = leftList[i];
                    index++;
                    count = j;
                    break;
                } else {
                    lst[index] = rightList[j];
                    index++;
                }
            }
        }

        if (count >= rightList.length - 1) { // Hier könnte eine Fehlerquelle sein. einfach mit -1 herumspielen
            return lst;
        } else {
            for (int i = count; i < rightList.length; i++) {
                lst[index] = rightList[i];
                index++;
            }
            return lst;
        }
    }

    public static void main(String[] args) {
        int[] elements = { 1, 5, 10, 50, 100, 500, 1000, 5000, 10000, 20000, 40000, 60000, 80000, 100000, 1000000,
                10000000 };
        int inhalt = 0;

        System.out.printf("%nElemente    ; dynamisch/µs; statisch/µs");

        /*
         * for (int j=0; j<elements.length; j++) {
         * 
         * LinkedList d = new LinkedList(new Node(inhalt));
         * long t1 = System.nanoTime();
         * for (int i=0; i<elements[j]; i++) {
         * d.add(inhalt);
         * }
         * t1 = System.nanoTime() - t1;
         * 
         * long t2 = System.nanoTime();
         * int staticArray[] = new int[elements[j]];
         * for (int i=0; i<elements[j]; i++) {
         * staticArray[i] = inhalt;
         * }
         * t2 = System.nanoTime() - t2;
         * 
         * System.out.printf("%n%-12d; %-12d; %-12d", elements[j], t1/1000, t2/1000);
         * }
         * 
         * System.out.println();
         */

        for (int j = 0; j < elements.length; j++) {

            //
            LinkedList d = new LinkedList(new Node(inhalt));
            for (int i = 0; i < elements[j]; i++) {
                d.add(inhalt);
            }
            long t1 = System.nanoTime();
            long count1 = 0;
            t1 = System.nanoTime();
            d.difSort();
            t1 = System.nanoTime() - t1;
            count1 = t1;
            for (int i = 0; i < 10; i++) {
                t1 = System.nanoTime();
                d.difSort();
                t1 = System.nanoTime() - t1;
                count1 = (count1 + t1) / 2;
            }
            //
            int staticArray[] = new int[elements[j]];
            for (int i = 0; i < elements[j]; i++) {
                staticArray[i] = inhalt++;
            }
            long count2 = 0;
            long t2 = System.nanoTime();

            staticArray = mergeSort(staticArray);

            t2 = System.nanoTime() - t2;
            count2 = t2;
            for (int i = 0; i < 10; i++) {
                t2 = System.nanoTime();

                staticArray = mergeSort(staticArray);

                t2 = System.nanoTime() - t2;
                count2 = (count2 + t2) / 2;
            }

            System.out.printf("%n%-12d; %-12d; %-12d", elements[j], count1 / 1000, count2 / 1000);
        }

    }
}