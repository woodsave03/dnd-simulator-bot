package communication;

public abstract class Attribute<T extends AttributeType> implements Comparable {
    private T type;
    public Attribute(T type) {
        this.type = type;
    }
    public boolean over(Attribute other) {
        return compareTo(other) > 0;
    }
    public T type() { return type; }

}
