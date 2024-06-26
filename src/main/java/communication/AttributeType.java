package communication;

/**
 * An interface for the type of attribute.
 */
public interface AttributeType {
    /**
     * Retrieve the attribute of type AttributeType from a given source.
     *
     * @param source The source of the attribute.
     * @return The attribute of the given type.
     */
    Attribute<AttributeType> retrieveFrom(Source<AttributeType> source);
}
