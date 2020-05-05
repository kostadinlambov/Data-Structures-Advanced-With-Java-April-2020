import java.util.*;

public class HashTable<K, V> implements Iterable<KeyValue<K, V>> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.8d;

    private LinkedList<KeyValue<K, V>>[] slots;

    private int count;
    private int capacity;

    public HashTable() {
        this.slots = new LinkedList[INITIAL_CAPACITY];
        this.capacity = INITIAL_CAPACITY;
        this.count = 0;
    }

    public HashTable(int capacity) {
        this.slots = new LinkedList[capacity];
        this.capacity = capacity;
        this.count = 0;
    }

    public void add(K key, V value) {
        this.growIfNeeded();
        int slotNumber = this.findSlotNumber(key);

        LinkedList<KeyValue<K, V>> currentSlot = this.slots[slotNumber];

        if (currentSlot == null) {
            currentSlot = new LinkedList<>();
        }

        for (KeyValue<K, V> element : currentSlot) {
            if (element.getKey().equals(key)) {
                throw new IllegalArgumentException("Key already exists: " + key);
            }
        }

        KeyValue<K, V> kvp = new KeyValue<>(key, value);
        currentSlot.addLast(kvp);

        this.slots[slotNumber] = currentSlot;

        this.count++;
    }

    private int findSlotNumber(K key) {
        return Math.abs(key.hashCode() % this.slots.length);
    }

    private void growIfNeeded() {
        if ((double) (this.size() + 1) / this.capacity > LOAD_FACTOR) {
            this.grow();
        }
    }

    private void grow() {
        this.capacity *= 2;

        HashTable<K, V> newHashTable = new HashTable<>(this.capacity);

        for (LinkedList<KeyValue<K, V>> slot : slots) {
            if (slot != null) {
                for (KeyValue<K, V> kvp : slot) {
                    newHashTable.add(kvp.getKey(), kvp.getValue());
                }
            }
        }

        this.slots = newHashTable.slots;
        this.count = newHashTable.count;
    }

    public int size() {
        return this.count;
    }

    public int capacity() {
        return this.capacity;
    }

    public boolean addOrReplace(K key, V value) {
        this.growIfNeeded();
        int slotNumber = this.findSlotNumber(key);

        LinkedList<KeyValue<K, V>> currentSlot = this.slots[slotNumber];

        if (currentSlot == null) {
            currentSlot = new LinkedList<>();
        }

        boolean replaced = false;
        for (KeyValue<K, V> element : currentSlot) {
            if (element.getKey().equals(key)) {
                element.setValue(value);
                replaced = true;
            }
        }

        if(!replaced){
            KeyValue<K, V> kvp = new KeyValue<>(key, value);
            currentSlot.addLast(kvp);
            this.count++;
        }

        this.slots[slotNumber] = currentSlot;

        return replaced;
    }

    public V get(K key) {
        KeyValue<K, V> kvp = this.find(key);

        if (kvp == null) {
            throw new IllegalArgumentException();
        }

        return kvp.getValue();
    }

    public KeyValue<K, V> find(K key) {
        int slotNumber = findSlotNumber(key);

        LinkedList<KeyValue<K, V>> elements = this.slots[slotNumber];

        if (elements != null) {
            for (KeyValue<K, V> kvp : elements) {
                if (kvp.getKey().equals(key)) {
                    return kvp;
                }
            }
        }

        return null;
    }

    public boolean containsKey(K key) {
        return find(key) != null;
    }

    public boolean remove(K key) {
        int slotNumber = findSlotNumber(key);

        LinkedList<KeyValue<K, V>> elements = this.slots[slotNumber];

        boolean removed = false;

        if (elements == null) {
            return false;
        }

        removed = elements.removeIf(kvp -> kvp.getKey().equals(key));

        if (removed) {
            this.count--;
        }

        return removed;
    }

    public void clear() {
        this.slots = new LinkedList[INITIAL_CAPACITY];
    }

    public Iterable<K> keys() {
        List<K> keys = new ArrayList<>();

        for (KeyValue<K, V> pair : this) {
            keys.add(pair.getKey());
        }

        return keys;
    }

    public Iterable<V> values() {
        List<V> values = new ArrayList<>();

        for (KeyValue<K, V> pair : this) {
            values.add(pair.getValue());
        }

        return values;
    }

    @Override
    public Iterator<KeyValue<K, V>> iterator() {
        return new HashIterator();
    }

    private class HashIterator implements Iterator<KeyValue<K, V>> {
        Deque<KeyValue<K, V>> deque;

        private HashIterator() {
            this.deque = new ArrayDeque<>();

            for (LinkedList<KeyValue<K, V>> slot : slots) {
                if (slot != null) {
                    deque.addAll(slot);
                }
            }
        }

        @Override
        public boolean hasNext() {
            return !deque.isEmpty();
        }

        @Override
        public KeyValue<K, V> next() {
            if (!hasNext()) {
                return deque.poll();
            }

            return null;
        }
    }
}
