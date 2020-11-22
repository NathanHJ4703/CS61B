package byow.Core;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private T[] minheap;
    private int size;
    private Map<T, Double> priorityValues;
    private Map<T, Integer> indices;

    public ArrayHeapMinPQ() {
        minheap = (T[]) new Object[9];
        size = 0;
        priorityValues = new HashMap<>();
        indices = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        priorityValues.put(item, priority);
        indices.put(item, size + 1);
        minheap[size + 1] = item;
        size++;
        swim(item, size);
        if (size == minheap.length - 1) {
            resize(size*2);
        }
    }

    private void resize(int newSize) {
        T[] resizedArray = (T[]) new Object[newSize + 1];
        int k = 0;
        for (T ob : minheap) {
            resizedArray[k] = ob;
            k++;
        }
        minheap = resizedArray;
    }

    private void swim(T item, int indexItem) {
        if (size() == 0 || size() == 1 || indexItem == 1) {
            return;
        }
        int parentIndex = parentOf(indexItem);
        if (priorityValues.get(minheap[parentIndex]) > priorityValues.get(item)) {
            T oldParent = minheap[parentIndex];
            minheap[parentIndex] = item;
            minheap[indexItem] = oldParent;
            indices.replace(oldParent, indexItem);
            indices.replace(item, parentIndex);
            swim(item, parentIndex);
        }
    }

    private int parentOf(int index) {
        return index / 2;
    }

    @Override
    public boolean contains(T item) {
        return priorityValues.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if(size() == 0) {
            throw new NoSuchElementException();
        }
        return minheap[1];
    }

    @Override
    public T removeSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        double loadFactor = (size - 1) / (float) (minheap.length - 1);
        if (loadFactor < 0.25 && size() > 16) {
            downSize();
        }
        T smallest = minheap[1];
        priorityValues.remove(smallest);
        indices.remove(smallest);
        indices.replace(minheap[size], 1);
        minheap[1] = minheap[size];
        minheap[size] = null;
        size--;
        sink(1);
        return smallest;
    }

    private void downSize() {
        T[] downSizedArray = (T[]) new Object[(minheap.length / 2) + 1];
        for (int i = 0; i <= size(); i++) {
            downSizedArray[i] = minheap[i];
        }
        minheap = downSizedArray;
    }

    private boolean isOutOfBounds(int itemIndex) {
        return itemIndex > size;
    }

    private void sink(int itemIndex) {
        if (size() == 0) {
            return;
        }
        T item = minheap[itemIndex];
        double cPriority = priorityValues.get(item);
        int lcIndex = leftChild(itemIndex);
        int rcIndex = rightChild(itemIndex);
        if (isOutOfBounds(lcIndex)) {
            return;
        }
        double lPriority = priorityValues.get(minheap[lcIndex]);
        if (isOutOfBounds(rcIndex)) {
            if (lPriority < cPriority) {
                sinkHelper(itemIndex, lcIndex, item);
            }
            return;
        }
        double rPriority = priorityValues.get(minheap[rcIndex]);
        if (bothChildrenLess(itemIndex)) {
            if (lPriority < rPriority) {
                sinkHelper(itemIndex, lcIndex, item);
                sink(lcIndex);
            } else {
                sinkHelper(itemIndex, rcIndex, item);
                sink(rcIndex);
            }
        } else if (eitherChildrenLess(itemIndex)) {
            if (lPriority < cPriority) {
                sinkHelper(itemIndex, lcIndex, item);
                sink(lcIndex);
            } else {
                sinkHelper(itemIndex, rcIndex, item);
                sink(rcIndex);
            }
        }
    }

    private void sinkHelper(int itemIndex, int childIndex, T item) {
        T switchItem = minheap[childIndex];
        minheap[itemIndex] = switchItem;
        minheap[childIndex] = item;
        indices.replace(item, childIndex);
        indices.replace(switchItem, itemIndex);
    }

    private boolean bothChildrenLess(int itemIndex) {
        double cPriority = priorityValues.get(minheap[itemIndex]);
        int lcIndex = leftChild(itemIndex);
        int rcIndex = rightChild(itemIndex);
        double lPriority = priorityValues.get(minheap[lcIndex]);
        double rPriority = priorityValues.get(minheap[rcIndex]);
        return lPriority < cPriority && rPriority < cPriority;
    }

    private boolean eitherChildrenLess(int itemIndex) {
        double cPriority = priorityValues.get(minheap[itemIndex]);
        int lcIndex = leftChild(itemIndex);
        int rcIndex = rightChild(itemIndex);
        double lPriority = priorityValues.get(minheap[lcIndex]);
        double rPriority = priorityValues.get(minheap[rcIndex]);
        return lPriority < cPriority || rPriority < cPriority;
    }



    private int leftChild(int itemIndex) {
        return itemIndex*2;
    }

    private int rightChild(int itemIndex) {
        return itemIndex*2 + 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!priorityValues.containsKey(item)) {
            throw new NoSuchElementException();
        }
        if (priority > priorityValues.get(item)) {
            priorityValues.replace(item, priority);
            sink(indices.get(item));
        }
        if (priority < priorityValues.get(item)) {
            priorityValues.replace(item, priority);
            swim(item, indices.get(item));
        }
    }
}
