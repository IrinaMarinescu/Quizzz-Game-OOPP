package commons;

import java.util.Objects;

public class Pair<T, V> {

    private T key;
    private V value;

    public Pair() {
    }

    /**
     * constructor of the Pair class
     *
     * @param key   - the key of the pair
     * @param value - the value of the pair
     */
    public Pair(T key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Getter of the Key
     *
     * @return key
     */
    public T getKey() {
        return key;
    }

    /**
     * Getter of the Value
     *
     * @return value
     */
    public V getValue() {
        return value;
    }

    /**
     * Setter of the Key
     *
     * @param key - the new key
     */
    public void setKey(T key) {
        this.key = key;
    }

    /**
     * Setter of the Value
     *
     * @param value - the new value
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * Checks if two keys are the same
     *
     * @param o - object to be compared to
     * @return - true if they are the same/have the same parameters; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair pair = (Pair) o;
        return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }
}
