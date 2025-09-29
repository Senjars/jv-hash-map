package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private int capacity = 16;
    private int size = 0;
    private final double loadFactor = 0.75;
    private int treshold = (int) (capacity * loadFactor);
    private Entry<K, V>[] bucket = new Entry[capacity];

    @Override
    public void put(K key, V value) {

        Entry<K,V> newEntry = new Entry<>(key, value);
        int index;

        if (size >= treshold) {
            resize();
        }

        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode() % capacity);
        }

        if (bucket[index] == null) {
            bucket[index] = newEntry;
        } else {
            Entry<K, V> current = bucket[index];
            while (true) {
                if ((current.getKey() == null && key == null)
                        || (current.getKey() != null && current.getKey().equals(key))) {
                    current.setValue(value);
                    return;
                }
                if (current.getNext() == null) {
                    break;
                }
                current = current.getNext();
            }
            current.setNext(newEntry);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode() % capacity);
        }
        Entry<K, V> current = bucket[index];

        while (current != null) {
            if ((current.getKey() == null && key == null)
                    || (current.getKey() != null && current.getKey().equals(key))) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        capacity = capacity * 2;
        treshold = (int) (capacity * loadFactor);
        Entry<K, V>[] newBucket = new Entry[capacity];

        for (Entry<K, V> entry : bucket) {
            while (entry != null) {
                Entry<K, V> nextEntry = entry.getNext();

                int newIndex = (entry.getKey() == null ? 0 : Math.abs(entry.getKey().hashCode() % capacity));
                entry.setNext(newBucket[newIndex]);
                newBucket[newIndex] = entry;

                entry = nextEntry;
            }
        }
        bucket = newBucket;
    }
}
