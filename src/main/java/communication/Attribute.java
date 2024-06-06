package communication;

/**
 * A class that represents an attribute of a given type.
 *
 * @param <T> The type of AttributeType that this Attribute is.
 */
public interface Attribute<T extends AttributeType> extends Comparable {

    /**
     * Compares this Attribute to another Attribute.
     *
     * @param other The other Attribute to compare to.
     * @return A negative integer, zero, or a positive integer as this Attribute is less than, equal to, or greater than the other Attribute.
     */
    boolean over(Attribute<T> other);

    /**
     * Returns the type of this Attribute.
     *
     * @return The type of this Attribute.
     */
    T type();

    /**
     * Compares this Attribute to another Attribute.
     *
     * @param other The other Attribute to compare to.
     * @return A negative integer, zero, or a positive integer as this Attribute is less than, equal to, or greater than the other Attribute.
     */
    @Override
    int compareTo(Object other);
}
