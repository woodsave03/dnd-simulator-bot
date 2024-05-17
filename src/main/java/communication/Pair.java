package communication;

import java.util.Optional;

public class Pair<T, U> {
    private Optional<T> key = Optional.empty();
    private Optional<U> value = Optional.empty();

    public Pair() {}

    public Pair(T key, U value) {
        this.key = Optional.of(key);
        this.value = Optional.of(value);
    }

    public T key() { return key.get(); }

    public U value() { return value.get(); }

    public void setKey(T key) { this.key = Optional.of(key); }
    public void setValue(U value) { this.value = Optional.of(value); }
    public void setPair(T key, U value) {
        setKey(key);
        setValue(value);
    }
}
