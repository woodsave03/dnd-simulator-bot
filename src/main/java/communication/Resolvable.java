package communication;

import game.entities.Ability;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A class that represents a resolvable request for an attribute.
 * This is compatible with Sources of the same AttributeType.
 *
 * @param <T> The type of AttributeType that this Resolvable can resolve.
 */
public class Resolvable<T extends AttributeType> {
    // The options that this Resolvable can resolve to.
    private final LinkedList<T> options = new LinkedList<>();
    // The result of the resolution.
    private Attribute<T> result;

    /**
     * Creates a new Resolvable with the given options.
     *
     * @param options The options that this Resolvable can resolve to.
     */
    @SafeVarargs
    public Resolvable(T... options) {
        this.options.addAll(Arrays.asList(options));
    }

    /**
     * Creates a new Resolvable with the given options.
     *
     * @param options The options that this Resolvable can resolve to.
     */
    public Resolvable(Set<T> options) {
        this.options.addAll(options);
    }

    /**
     * Uses the given Source to calculate the result of this Resolvable.
     *
     * @param source The Source to use to resolve this Resolvable.
     * @return The Attribute that this Resolvable resolves to.
     */
    public Attribute<T> resolve(Source<T> source) {
        set(source.resolve(options.iterator()));
        return result;
    }

    /**
     * Returns an iterator over the options that this Resolvable can resolve to.
     * @return An iterator over the options that this Resolvable can resolve to.
     */
    public Iterator<T> options() {
        return options.iterator();
    }

    /**
     * Adds the given options to this Resolvable.
     *
     * @param options The options to add to this Resolvable.
     * @return This Resolvable.
     */
    public Resolvable<T> addOptions(Set<T> options) {
        this.options.addAll(options);
        return this;
    }

    /**
     * Adds the given option to this Resolvable.
     *
     * @param option The option to add to this Resolvable.
     * @return This Resolvable.
     */
    public Resolvable<T> addOption(T option) {
        this.options.add(option);
        return this;
    }

    /**
     * Returns the result of this Resolvable.
     *
     * @return The result of this Resolvable.
     * @throws IllegalStateException If the request has not been resolved.
     */
    public Attribute<T> get() {
        if (isUnresolved())
            throw new IllegalStateException("Request has not been resolved");
        return result;
    }

    /**
     * Returns whether this Resolvable is empty.
     *
     * @return Whether this Resolvable is empty.
     */
    public boolean isEmpty() {
        return options.isEmpty();
    }

    /**
     * Sets the result of this Resolvable.
     *
     * @param result The result of this Resolvable.
     */
    public void set(Attribute<T> result) {
        this.result = result;
    }

    /**
     * Returns the type of the result of this Resolvable.
     *
     * @return The type of the result of this Resolvable.
     * @throws IllegalStateException If the request has not been resolved.
     */
    public T type() {
        if (isUnresolved())
            throw new IllegalStateException("Request has not been resolved");
        return (T) result.type();
    }

    /**
     * Returns the options that this Resolvable can resolve to.
     *
     * @return The options that this Resolvable can resolve to.
     */
    public List<T> types() {
        return options;
    }

    /**
     * Returns whether this Resolvable has been resolved.
     *
     * @return Whether this Resolvable has been resolved.
     */
    public boolean isUnresolved() {
        return result == null;
    }

    /**
     * Adds the given option to this Resolvable if the given condition is true.
     *
     * @param option The option to add to this Resolvable.
     * @param condition The condition to check.
     * @return This Resolvable.
     */
    public Resolvable<T> addIf(T option, boolean condition) {
        if (condition)
            options.add(option);
        return this;
    }

    /**
     * Creates a new Resolvable with the given options.
     *
     * @param options The options that this Resolvable can resolve to.
     * @return A new Resolvable with the given options.
     */
    public static Resolvable<Ability.Type> of(Set<Ability.Type> options) {
        return new Resolvable<>(options);
    }

    /**
     * Creates a new Resolvable with the given options.
     *
     * @param result The options that this Resolvable can resolve to.
     * @return A new Resolvable with the given options.
     */
    public static Resolvable<AttributeType> of(Attribute<AttributeType> result) {
        Resolvable<AttributeType> resolvable = new Resolvable<>();
        resolvable.set(result);
        return resolvable;
    }
}
