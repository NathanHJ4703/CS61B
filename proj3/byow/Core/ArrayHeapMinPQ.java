package byow.Core;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/** Used by A* Solver to determine the shortest path to take.
 * @author Nathan Pak
 * @param <T>
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /** A minheap array for storing the items in order of priority. */
    private T[] minheap;
    /** The size of the array. */
    private int size;
    /** A mapping of the item to its priority value. */
    private Map<T, Double> priorityValues;
    /** A mapping of the item to its indices. */
    private Map<T, Integer> indices;

    /** Constructs the arrayheapMinPQ. */
    public ArrayHeapMinPQ() {
        minheap = (T[]) new Object[9];
        size = 0;
        priorityValues = new HashMap<>();
        indices = new HashMap<>();
    }

    /** Adds the item to the Priority queue.
     * @param item An item
     * @param priority The priority of the item
     */
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
            resize(size * 2);
        }
    }

    /** Resizes the priority queue if the priority queue is full.
     * @param newSize The new size of the array. */
    private void resize(int newSize) {
        T[] resizedArray = (T[]) new Object[newSize + 1];
        int k = 0;
        for (T ob : minheap) {
            resizedArray[k] = ob;
            k++;
        }
        minheap = resizedArray;
    }
    /** Moves the item to a higher position if the item has a higher priority
     * value than its parent.
     * @param item The item
     * @param indexItem The index of that item */
    private void swim(T item, int indexItem) {
        if (size() == 0 || size() == 1 || indexItem == 1) {
            return;
        }
        int parentIndex = parentOf(indexItem);
        if (priorityValues.get(minheap[parentIndex])
                > priorityValues.get(item)) {
            T oldParent = minheap[parentIndex];
            minheap[parentIndex] = item;
            minheap[indexItem] = oldParent;
            indices.replace(oldParent, indexItem);
            indices.replace(item, parentIndex);
            swim(item, parentIndex);
        }
    }

    /** The parent of that item.
     * @param index Index of the item
     * @return The index of the parent */
    private int parentOf(int index) {
        return index / 2;
    }

    /** Checks if the priority queue contains the item.
     *
     * @param item The item
     * @return Whether the item is inside
     */
    @Override
    public boolean contains(T item) {
        return priorityValues.containsKey(item);
    }

    /** Gets the smallest.
     *
     * @return The smallest item from the priority queue.
     */
    @Override
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return minheap[1];
    }

    /** Removes the smallest item.
     *
     * @return The item with the least priority.
     */
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

    /** If there are too many indices with null values,
     * the array size is resized down. */
    private void downSize() {
        T[] downSizedArray = (T[]) new Object[(minheap.length / 2) + 1];
        for (int i = 0; i <= size(); i++) {
            downSizedArray[i] = minheap[i];
        }
        minheap = downSizedArray;
    }

    /** Checks if the index is out of bounds.
     *
     * @param itemIndex Index of the item
     * @return Whether index is out of bounds
     */
    private boolean isOutOfBounds(int itemIndex) {
        return itemIndex > size;
    }

    /** Moves the item to a lower position if it has a greater priority value
     * than its children.
     *
     * @param itemIndex Index of the item
     */
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

    /** Used to switch the item with one of its children.
     *
     * @param itemIndex index of item
     * @param childIndex index of child
     * @param item the item
     */
    private void sinkHelper(int itemIndex, int childIndex, T item) {
        T switchItem = minheap[childIndex];
        minheap[itemIndex] = switchItem;
        minheap[childIndex] = item;
        indices.replace(item, childIndex);
        indices.replace(switchItem, itemIndex);
    }

    /** Checks if both of its children is less than the given item.
     *
     * @param itemIndex index of item
     * @return Whether or not both the item's children is smaller
     */
    private boolean bothChildrenLess(int itemIndex) {
        double cPriority = priorityValues.get(minheap[itemIndex]);
        int lcIndex = leftChild(itemIndex);
        int rcIndex = rightChild(itemIndex);
        double lPriority = priorityValues.get(minheap[lcIndex]);
        double rPriority = priorityValues.get(minheap[rcIndex]);
        return lPriority < cPriority && rPriority < cPriority;
    }

    /** Checks if one of its children is less than the given item.
     *
     * @param itemIndex index of item
     * @return whether one of its children is less than the given item
     */
    private boolean eitherChildrenLess(int itemIndex) {
        double cPriority = priorityValues.get(minheap[itemIndex]);
        int lcIndex = leftChild(itemIndex);
        int rcIndex = rightChild(itemIndex);
        double lPriority = priorityValues.get(minheap[lcIndex]);
        double rPriority = priorityValues.get(minheap[rcIndex]);
        return lPriority < cPriority || rPriority < cPriority;
    }


    /** Item's left child.
     *
     * @param itemIndex index of item
     * @return the index of left child of the item
     */
    private int leftChild(int itemIndex) {
        return itemIndex * 2;
    }

    /** Item's right child.
     *
     * @param itemIndex index of item
     * @return the index of right child of the item
     */
    private int rightChild(int itemIndex) {
        return itemIndex * 2 + 1;
    }

    /** Size of priority queue.
     *
     * @return number of elements inside
     */
    @Override
    public int size() {
        return size;
    }

    /** Changes the priority of the item, potentially causing a sink or swim.
     *
     * @param item The item to have its priority changed.
     * @param priority The new priority to be changed to.
     */
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
