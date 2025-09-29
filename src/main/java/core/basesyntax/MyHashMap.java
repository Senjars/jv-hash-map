package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    int capacity = 16;
    int size = 0;
    final double loadFactor = 0.75;
    final int treshold = (int) (capacity * loadFactor);
    Entry<K, V>[] bucket = new Entry[capacity];

    @Override
    public void put(K key, V value) { // put accepts keys and values
        int index = Math.abs(key.hashCode() % capacity); // index equals the remainder (abs = absolute / liczba bezwzględna)
        Entry<K,V> newEntry = new Entry<>(key, value); // creates new entries

        if (size >= treshold) {
            resize();
        }

        if (bucket[index] == null) { // checks whether bucket a index .. is null
           bucket[index] = newEntry; // if it is then bucket at index becomes a new entry
        } else {
            Entry<K, V> current = bucket[index]; // if bucket != null then current = bucket[index]
            while (true) { // as long as it's true current must be current next (not null > null > null(the previous one becomes this one))
                if (current.key.equals((key))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) break;
                current = current.next;
            }
            current.next = newEntry; //now current next is a newEntry
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(key.hashCode() % capacity);
        Entry<K, V> current = bucket[index]; // start with the first element in the bucket

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;  // if the key was found
            }
            current = current.next; // next on the list
        }
        return null; // key not found so we return null
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        capacity = capacity * 2;
        Entry<K, V>[] newBucket = new Entry[capacity];

        for (Entry<K, V> entry : bucket) {
            while (entry != null) {
                Entry<K, V> nextEntry = entry.next;

                int newIndex = Math.abs(entry.key.hashCode() % capacity);
                entry.next = newBucket[newIndex];
                newBucket[newIndex] = entry;

                entry = nextEntry;
            }
        }
        bucket = newBucket;
    }
}
