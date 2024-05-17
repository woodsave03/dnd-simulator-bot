package communication;

public interface AttributeType<T extends Attribute> {
    T retrieve(Source<AttributeType> source);
}
