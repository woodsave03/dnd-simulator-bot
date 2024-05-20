package communication;

import java.util.Optional;

/**
 * A simple class to store a pair of objects.
 * @param <T> The type of the first object.
 * @param <U> The type of the second object.
 */
public class Pair<T, U> {
    // The key and value of the pair.
    private Optional<T> key;
    private Optional<U> value;

    /**
     * Default constructor.
     */
    public Pair() {
        key = Optional.empty();
        value = Optional.empty();
    }

    /**
     * Constructor with key and value.
     * @param key The key of the pair.
     * @param value The value of the pair.
     */
    public Pair(T key, U value) {
        this.key = Optional.of(key);
        this.value = Optional.of(value);
    }

    /**
     * Get the key of the pair.
     *
     * @return The key of the pair.
     */
    public T key() { return key.get(); }

    /**
     * Get the value of the pair.
     *
     * @return The value of the pair.
     */
    public U value() { return value.get(); }

    /**
     * Set the key of the pair.
     *
     * @param key The key of the pair.
     */
    public void setKey(T key) { this.key = Optional.of(key); }

    /**
     * Set the value of the pair.
     *
     * @param value The value of the pair.
     */
    public void setValue(U value) { this.value = Optional.of(value); }

    /**
     * Set the key and value of the pair.
     *
     * @param key The key of the pair.
     * @param value The value of the pair.
     */
    public void setPair(T key, U value) {
        setKey(key);
        setValue(value);
    }
}
