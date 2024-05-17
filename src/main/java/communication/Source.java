package communication;

import java.util.Iterator;

public interface Source<T extends AttributeType> {
     Attribute resolve(Iterator<T> options);
     Attribute provide(T type);
     T type(Iterator<T> options);
}
