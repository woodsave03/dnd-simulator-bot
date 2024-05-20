package communication;

/**
 * A class that represents an attribute of a given type.
 *
 * @param <T> The type of AttributeType that this Attribute is.
 */
public abstract class Attribute<T extends AttributeType> implements Comparable {
    // The type of this Attribute.
    private final T type;
    /**
     * Creates a new Attribute with the given type.
     *
     * @param type The type of this Attribute.
     */
    public Attribute(T type) {
        this.type = type;
    }

    /**
     * Compares this Attribute to another Attribute.
     *
     * @param other The other Attribute to compare to.
     * @return A negative integer, zero, or a positive integer as this Attribute is less than, equal to, or greater than the other Attribute.
     */
    public boolean over(Attribute<T> other) {
        return compareTo(other) > 0;
    }

    /**
     * Returns the type of this Attribute.
     *
     * @return The type of this Attribute.
     */
    public T type() { return type; }

    /**
     * Compares this Attribute to another Attribute.
     *
     * @param other The other Attribute to compare to.
     * @return A negative integer, zero, or a positive integer as this Attribute is less than, equal to, or greater than the other Attribute.
     */
    @Override
    abstract public int compareTo(Object other);
}
