package communication;

import java.util.Iterator;

/**
 * An interface for the source of an attribute.
 * The type of Attribute is determined by the AttributeType.
 */
public interface Source<T extends AttributeType> {
    /**
     * Return the best attribute from the given AttributeType options by comparing them from the source.
     *
     * @param options The options to choose from.
     * @return The best attribute from the given options.
     */
    Attribute<T> resolve(Iterator<T> options);

    /**
     * Provide the attribute of the given type from the source.
     *
     * @param type The type of attribute to provide.
     * @return The attribute of the given type.
     */
    Attribute<T> provide(T type);

    /**
     * Return the type of the best attribute from the given AttributeType options by comparing them from the source.
     *
     * @param options The options to choose from.
     * @return The type of the best attribute from the given options.
     */
    T type(Iterator<T> options);
}
