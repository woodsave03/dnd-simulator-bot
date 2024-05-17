package mechanics.actions;

import java.io.Serializable;
import java.util.Optional;

public class Range implements Serializable {
    private int shortRange;
    private Optional<Integer> longRange = Optional.empty();
    private Optional<Integer> reach = Optional.empty();

    public Range(int reach) {
        this.shortRange = reach;
    }

    public Range(int rShort, int rLong) {
        this(rShort);
        this.longRange = Optional.of(rLong);
    }

    public boolean hasLongRange() {
        return longRange.isPresent();
    }

    public Range withReach(int reach) {
        this.reach = Optional.of(reach);
        return this;
    }

    public void setReach(int reach) {
        this.reach = Optional.of(reach);
    }
    public int getReach() {
        return reach.orElse(shortRange);
    }

    public boolean isMelee() {
        return reach.isPresent() || shortRange == 1;
    }

    public int getLongRange() {
        return longRange.get();
    }
    public void setLongRange(int longRange) {
        this.longRange = Optional.of(longRange);
    }

    public int getShortRange() {
        return shortRange;
    }
    public void setShortRange(int shortRange) {
        this.shortRange = shortRange;
    }

    @Override
    public String toString() {
        return "Range{" +
                "shortRange=" + shortRange +
                ", longRange=" + longRange +
                ", reach=" + reach +
                '}';
    }
}
