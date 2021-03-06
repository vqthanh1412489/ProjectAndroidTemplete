package com.badlogic.gdx.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Predicate.PredicateIterable;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Array<T> implements Iterable<T> {
    public T[] items;
    private ArrayIterable iterable;
    public boolean ordered;
    private PredicateIterable<T> predicateIterable;
    public int size;

    public static class ArrayIterable<T> implements Iterable<T> {
        private final boolean allowRemove;
        private final Array<T> array;
        private ArrayIterator iterator1;
        private ArrayIterator iterator2;

        public ArrayIterable(Array<T> array) {
            this(array, true);
        }

        public ArrayIterable(Array<T> array, boolean allowRemove) {
            this.array = array;
            this.allowRemove = allowRemove;
        }

        public Iterator<T> iterator() {
            if (this.iterator1 == null) {
                this.iterator1 = new ArrayIterator(this.array, this.allowRemove);
                this.iterator2 = new ArrayIterator(this.array, this.allowRemove);
            }
            if (this.iterator1.valid) {
                this.iterator2.index = 0;
                this.iterator2.valid = true;
                this.iterator1.valid = false;
                return this.iterator2;
            }
            this.iterator1.index = 0;
            this.iterator1.valid = true;
            this.iterator2.valid = false;
            return this.iterator1;
        }
    }

    public static class ArrayIterator<T> implements Iterator<T>, Iterable<T> {
        private final boolean allowRemove;
        private final Array<T> array;
        int index;
        boolean valid;

        public ArrayIterator(Array<T> array) {
            this(array, true);
        }

        public ArrayIterator(Array<T> array, boolean allowRemove) {
            this.valid = true;
            this.array = array;
            this.allowRemove = allowRemove;
        }

        public boolean hasNext() {
            return this.index < this.array.size;
        }

        public T next() {
            if (this.index >= this.array.size) {
                throw new NoSuchElementException(String.valueOf(this.index));
            } else if (this.valid) {
                Object[] objArr = this.array.items;
                int i = this.index;
                this.index = i + 1;
                return objArr[i];
            } else {
                throw new GdxRuntimeException("#iterator() cannot be used nested.");
            }
        }

        public void remove() {
            if (this.allowRemove) {
                this.index--;
                this.array.removeIndex(this.index);
                return;
            }
            throw new GdxRuntimeException("Remove not allowed.");
        }

        public void reset() {
            this.index = 0;
        }

        public Iterator<T> iterator() {
            return this;
        }
    }

    public Array() {
        this(true, 16);
    }

    public Array(int capacity) {
        this(true, capacity);
    }

    public Array(boolean ordered, int capacity) {
        this.ordered = ordered;
        this.items = new Object[capacity];
    }

    public Array(boolean ordered, int capacity, Class arrayType) {
        this.ordered = ordered;
        this.items = (Object[]) ArrayReflection.newInstance(arrayType, capacity);
    }

    public Array(Class arrayType) {
        this(true, 16, arrayType);
    }

    public Array(Array<? extends T> array) {
        this(array.ordered, array.size, array.items.getClass().getComponentType());
        this.size = array.size;
        System.arraycopy(array.items, 0, this.items, 0, this.size);
    }

    public Array(T[] array) {
        this(true, array, 0, array.length);
    }

    public Array(boolean ordered, T[] array, int start, int count) {
        this(ordered, count, array.getClass().getComponentType());
        this.size = count;
        System.arraycopy(array, 0, this.items, 0, this.size);
    }

    public void add(T value) {
        T[] items = this.items;
        if (this.size == items.length) {
            items = resize(Math.max(8, (int) (((float) this.size) * 1.75f)));
        }
        int i = this.size;
        this.size = i + 1;
        items[i] = value;
    }

    public void addAll(Array<? extends T> array) {
        addAll((Array) array, 0, array.size);
    }

    public void addAll(Array<? extends T> array, int offset, int length) {
        if (offset + length > array.size) {
            throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size);
        }
        addAll(array.items, offset, length);
    }

    public void addAll(T[] array) {
        addAll((Object[]) array, 0, array.length);
    }

    public void addAll(T[] array, int offset, int length) {
        T[] items = this.items;
        int sizeNeeded = this.size + length;
        if (sizeNeeded > items.length) {
            items = resize(Math.max(8, (int) (((float) sizeNeeded) * 1.75f)));
        }
        System.arraycopy(array, offset, items, this.size, length);
        this.size += length;
    }

    public T get(int index) {
        if (index < this.size) {
            return this.items[index];
        }
        throw new IndexOutOfBoundsException(String.valueOf(index));
    }

    public void set(int index, T value) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        this.items[index] = value;
    }

    public void insert(int index, T value) {
        if (index > this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        T[] items = this.items;
        if (this.size == items.length) {
            items = resize(Math.max(8, (int) (((float) this.size) * 1.75f)));
        }
        if (this.ordered) {
            System.arraycopy(items, index, items, index + 1, this.size - index);
        } else {
            items[this.size] = items[index];
        }
        this.size++;
        items[index] = value;
    }

    public void swap(int first, int second) {
        if (first >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(first));
        } else if (second >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(second));
        } else {
            T[] items = this.items;
            T firstValue = items[first];
            items[first] = items[second];
            items[second] = firstValue;
        }
    }

    public boolean contains(T value, boolean identity) {
        T[] items = this.items;
        int i = this.size - 1;
        int i2;
        if (identity || value == null) {
            do {
                i2 = i;
                if (i2 >= 0) {
                    i = i2 - 1;
                }
            } while (items[i2] != value);
            return true;
        }
        do {
            i2 = i;
            if (i2 >= 0) {
                i = i2 - 1;
            }
        } while (!value.equals(items[i2]));
        return true;
        return false;
    }

    public int indexOf(T value, boolean identity) {
        T[] items = this.items;
        int n;
        int i;
        if (identity || value == null) {
            n = this.size;
            for (i = 0; i < n; i++) {
                if (items[i] == value) {
                    return i;
                }
            }
        } else {
            n = this.size;
            for (i = 0; i < n; i++) {
                if (value.equals(items[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(T value, boolean identity) {
        T[] items = this.items;
        int i;
        if (identity || value == null) {
            for (i = this.size - 1; i >= 0; i--) {
                if (items[i] == value) {
                    return i;
                }
            }
        } else {
            for (i = this.size - 1; i >= 0; i--) {
                if (value.equals(items[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean removeValue(T value, boolean identity) {
        T[] items = this.items;
        int n;
        int i;
        if (identity || value == null) {
            n = this.size;
            for (i = 0; i < n; i++) {
                if (items[i] == value) {
                    removeIndex(i);
                    return true;
                }
            }
        } else {
            n = this.size;
            for (i = 0; i < n; i++) {
                if (value.equals(items[i])) {
                    removeIndex(i);
                    return true;
                }
            }
        }
        return false;
    }

    public T removeIndex(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        T[] items = this.items;
        T value = items[index];
        this.size--;
        if (this.ordered) {
            System.arraycopy(items, index + 1, items, index, this.size - index);
        } else {
            items[index] = items[this.size];
        }
        items[this.size] = null;
        return value;
    }

    public boolean removeAll(Array<? extends T> array, boolean identity) {
        int size = this.size;
        int startSize = size;
        T[] items = this.items;
        int n;
        int i;
        T item;
        int ii;
        if (identity) {
            n = array.size;
            for (i = 0; i < n; i++) {
                item = array.get(i);
                for (ii = 0; ii < size; ii++) {
                    if (item == items[ii]) {
                        removeIndex(ii);
                        size--;
                        break;
                    }
                }
            }
        } else {
            n = array.size;
            for (i = 0; i < n; i++) {
                item = array.get(i);
                for (ii = 0; ii < size; ii++) {
                    if (item.equals(items[ii])) {
                        removeIndex(ii);
                        size--;
                        break;
                    }
                }
            }
        }
        return size != startSize;
    }

    public T pop() {
        if (this.size == 0) {
            throw new IllegalStateException("Array is empty.");
        }
        this.size--;
        T item = this.items[this.size];
        this.items[this.size] = null;
        return item;
    }

    public T peek() {
        if (this.size != 0) {
            return this.items[this.size - 1];
        }
        throw new IllegalStateException("Array is empty.");
    }

    public T first() {
        if (this.size != 0) {
            return this.items[0];
        }
        throw new IllegalStateException("Array is empty.");
    }

    public void clear() {
        T[] items = this.items;
        int n = this.size;
        for (int i = 0; i < n; i++) {
            items[i] = null;
        }
        this.size = 0;
    }

    public void shrink() {
        if (this.items.length != this.size) {
            resize(this.size);
        }
    }

    public T[] ensureCapacity(int additionalCapacity) {
        int sizeNeeded = this.size + additionalCapacity;
        if (sizeNeeded >= this.items.length) {
            resize(Math.max(8, sizeNeeded));
        }
        return this.items;
    }

    protected T[] resize(int newSize) {
        T[] items = this.items;
        Object[] newItems = (Object[]) ((Object[]) ArrayReflection.newInstance(items.getClass().getComponentType(), newSize));
        System.arraycopy(items, 0, newItems, 0, Math.min(this.size, newItems.length));
        this.items = newItems;
        return newItems;
    }

    public void sort() {
        Sort.instance().sort(this.items, 0, this.size);
    }

    public void sort(Comparator<T> comparator) {
        Sort.instance().sort(this.items, comparator, 0, this.size);
    }

    public T selectRanked(Comparator<T> comparator, int kthLowest) {
        if (kthLowest >= 1) {
            return Select.instance().select(this.items, comparator, kthLowest, this.size);
        }
        throw new GdxRuntimeException("nth_lowest must be greater than 0, 1 = first, 2 = second...");
    }

    public int selectRankedIndex(Comparator<T> comparator, int kthLowest) {
        if (kthLowest >= 1) {
            return Select.instance().selectIndex(this.items, comparator, kthLowest, this.size);
        }
        throw new GdxRuntimeException("nth_lowest must be greater than 0, 1 = first, 2 = second...");
    }

    public void reverse() {
        T[] items = this.items;
        int lastIndex = this.size - 1;
        int n = this.size / 2;
        for (int i = 0; i < n; i++) {
            int ii = lastIndex - i;
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    public void shuffle() {
        T[] items = this.items;
        for (int i = this.size - 1; i >= 0; i--) {
            int ii = MathUtils.random(i);
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    public Iterator<T> iterator() {
        if (this.iterable == null) {
            this.iterable = new ArrayIterable(this);
        }
        return this.iterable.iterator();
    }

    public Iterable<T> select(Predicate<T> predicate) {
        if (this.predicateIterable == null) {
            this.predicateIterable = new PredicateIterable(this, predicate);
        } else {
            this.predicateIterable.set(this, predicate);
        }
        return this.predicateIterable;
    }

    public void truncate(int newSize) {
        if (this.size > newSize) {
            for (int i = newSize; i < this.size; i++) {
                this.items[i] = null;
            }
            this.size = newSize;
        }
    }

    public T random() {
        if (this.size == 0) {
            return null;
        }
        return this.items[MathUtils.random(0, this.size - 1)];
    }

    public T[] toArray() {
        return toArray(this.items.getClass().getComponentType());
    }

    public <V> V[] toArray(Class type) {
        Object[] result = (Object[]) ((Object[]) ArrayReflection.newInstance(type, this.size));
        System.arraycopy(this.items, 0, result, 0, this.size);
        return result;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Array)) {
            return false;
        }
        Array array = (Array) object;
        int n = this.size;
        if (n != array.size) {
            return false;
        }
        Object[] items1 = this.items;
        Object[] items2 = array.items;
        for (int i = 0; i < n; i++) {
            Object o1 = items1[i];
            Object o2 = items2[i];
            if (o1 == null) {
                if (o2 != null) {
                    return false;
                }
            } else if (!o1.equals(o2)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        if (this.size == 0) {
            return "[]";
        }
        T[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append('[');
        buffer.append(items[0]);
        for (int i = 1; i < this.size; i++) {
            buffer.append(", ");
            buffer.append(items[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }

    public String toString(String separator) {
        if (this.size == 0) {
            return "";
        }
        T[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append(items[0]);
        for (int i = 1; i < this.size; i++) {
            buffer.append(separator);
            buffer.append(items[i]);
        }
        return buffer.toString();
    }
}
