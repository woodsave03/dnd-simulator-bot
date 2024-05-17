package communication;

import game.entities.Ability;

import java.util.*;

public class Resolvable<T extends AttributeType> {
    private LinkedList<T> options = new LinkedList<>();
    private Attribute result;

    public Resolvable(T... options) {
        this.options.addAll(Arrays.asList(options));
    }

    public Resolvable(Set<T> options) {
        this.options.addAll(options);
    }

    public Attribute resolve(Source<T> source) {
        result = source.resolve(options.iterator());
        return result;
    }

    public Iterator<T> options() {
        return options.iterator();
    }

    public Resolvable<T> addOptions(Set<T> options) {
        this.options.addAll(options);
        return this;
    }
    public Resolvable<T> addOption(T option) {
        this.options.add(option);
        return this;
    }
    public Attribute get() {
        if (!isResolved())
            throw new IllegalStateException("Request has not been resolved");
        return result;
    }

    public boolean isEmpty() {
        return options.isEmpty();
    }

    public void set(Attribute result) {
        this.result = result;
    }

    public T type() {
        if (!isResolved())
            throw new IllegalStateException("Request has not been resolved");
        return (T) result.type();
    }

    public List<T> types() {
        return options;
    }

    public boolean isResolved() {
        return result != null;
    }
    public Resolvable<T> addIf(T option, boolean condition) {
        if (condition)
            options.add(option);
        return this;
    }
    public static Resolvable<Ability.Type> of(Set<Ability.Type> options) {
        return new Resolvable<>(options);
    }

    public static Resolvable<AttributeType> of(Attribute result) {
        Resolvable<AttributeType> resolvable = new Resolvable<>();
        resolvable.set(result);
        return resolvable;
    }
}
