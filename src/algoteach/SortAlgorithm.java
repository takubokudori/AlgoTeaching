package algoteach;

enum SortName {
    BUBBLE_SORT, INSERTION_SORT
}

public abstract class SortAlgorithm {
    // ソートアルゴリズム
    SortName sortname;
    int[] array;

    abstract public void init();

    abstract public void step();

    abstract public void execute();

    abstract public boolean isCompleted();

    abstract public int[] getArray();

    public SortName getSortMethod() {
        return sortname;
    }

    public void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}

class BubbleSort extends SortAlgorithm {
    int i, j;
    int[] a;
    int NUM;
    boolean isCompleted = false;

    public BubbleSort(int[] a) {
        this.a = a.clone();
        NUM = this.a.length;
        init();
    }

    public void init() {
        i = 0;
        j = NUM - 1;
    }

    public void step() {
        if (a[j] < a[j - 1]) {
            swap(a, j, j - 1);
        }
        j--;
        if (j <= i) {
            i++;
            j = NUM - 1;
        }
        if (i >= NUM - 1) isCompleted = true;
    }

    @Override
    public void execute() {
        for (int i = 0; i < NUM; i++) {
            for (int j = NUM - 1; j > i; j--) {
                if (a[j] < a[j - 1]) {
                    swap(a, j, j - 1);
                }
            }
        }
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public int[] getArray() {
        return a;
    }
}
